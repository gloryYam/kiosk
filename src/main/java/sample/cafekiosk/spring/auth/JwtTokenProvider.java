package sample.cafekiosk.spring.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sample.cafekiosk.spring.auth.exception.AuthenticationException;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtTokenProvider {

    private static final String EXPIRED_ACCESS_TOKEN_MESSAGE = "EXPIRED_ACCESS_TOKEN";
    private static final String EXPIRED_REFRESH_TOKEN_MESSAGE = "EXPIRED_REFRESH_TOKEN";
    private final String EMAIL_KEY = "email";
    private final String AUTH_KEY = "auth";
    private final String ACCESS_TOKEN = "AccessToken";
    private final String REFRESH_TOKEN = "RefreshToken";

    @Value("${jwt.access.expiration}")
    private long jwtAccessTokenExpirationInMs;

    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshTokenExpirationInMs;

    private final Key jwtAccessSecretKey;
    private final Key jwtRefreshSecretKey;


    public JwtTokenProvider(@Value("${jwt.access.secret}") String jwtAccessSecretKey, @Value("${jwt.refresh.secret}") String jwtRefreshSecretKey) {
        this.jwtAccessSecretKey = Keys.hmacShaKeyFor(jwtAccessSecretKey.getBytes());
        this.jwtRefreshSecretKey = Keys.hmacShaKeyFor(jwtRefreshSecretKey.getBytes());
    }

    public String generateAccessToken(final String email, final List<String> roles) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtAccessTokenExpirationInMs);

        return Jwts.builder()
            .setSubject(ACCESS_TOKEN)
            .claim(EMAIL_KEY, email)    // 추가
            .claim(AUTH_KEY, roles)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(jwtAccessSecretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public String generateRefreshToken(final String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshTokenExpirationInMs);

        return Jwts.builder()
            .setSubject(REFRESH_TOKEN)
            .claim(EMAIL_KEY, email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(jwtRefreshSecretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractEmailFromRefreshToken(final String token) {
        validateRefreshToken(token);
        final Jws<Claims> claimsJws = Jwts.parserBuilder()
            .setSigningKey(jwtRefreshSecretKey)     // 검증
            .build()
            .parseClaimsJws(token);                // 클레임 추출

        String extractedEmail = claimsJws.getBody().get(EMAIL_KEY, String.class);   // 이메일 클레임 가져오기
        if (extractedEmail == null) {
            throw new AuthenticationException.FailAuthenticationException("인증 실패(JWT 리프레시 토큰 Payload 이메일 누락) - 토큰 : " + token);
        }
        return extractedEmail;
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtAccessSecretKey).build().parseClaimsJws(token);
            return true;
            // 유효한 경우 추가 처리 없음
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new AuthenticationException.FailAuthenticationException("잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, EXPIRED_ACCESS_TOKEN_MESSAGE);
        }
    }

    public void validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtRefreshSecretKey).build().parseClaimsJws(token);
            // 유효한 경우 추가 처리 없음
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new AuthenticationException.FailAuthenticationException("잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, EXPIRED_REFRESH_TOKEN_MESSAGE);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(jwtAccessSecretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();

        // 권한 정보 추출
        List<String> roles = claims.get(AUTH_KEY, List.class);
        List<SimpleGrantedAuthority> authorities = roles != null ? roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList()) : Collections.emptyList();


        // 사용자 식별자 확인
        String username = claims.getSubject();
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("JWT 토큰에 사용자 식별자가 없습니다.");
        }

        User principal = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
