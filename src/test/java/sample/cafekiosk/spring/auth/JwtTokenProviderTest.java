package sample.cafekiosk.spring.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import sample.cafekiosk.spring.IntegrationTestSupport;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestPropertySource("classpath:application-test.yml")
class JwtTokenProviderTest extends IntegrationTestSupport {

    @Value("${jwt.access.secret}")
    private String testAccessSecret;

    @Value("${jwt.refresh.secret}")
    private String testRefreshSecret;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("AccessToken 이 정상 발급되었는지 확인")
    void generateAccessToken() {
        // given
        String email = "test@test.com";

        // when
        String accessToken = jwtTokenProvider.generateAccessToken(email);

        Claims claims = Jwts.parserBuilder()
            .setSigningKey(testAccessSecret.getBytes())
            .build()
            .parseClaimsJws(accessToken)
            .getBody();

        // then
        String authEmail = claims.get("email", String.class);

        assertThat(accessToken).isNotNull();
        assertThat(authEmail).isEqualTo(email);
        assertThat(claims.getExpiration()).isAfter(new Date());
    }

    @Test
    @DisplayName("RefreshToken 이 정상 발급되었는지 확인")
    void generateRefreshToken() {
        // given
        String email = "test@test.com";

        // when
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(testRefreshSecret.getBytes())
            .build()
            .parseClaimsJws(refreshToken)
            .getBody();

        // then
        String authEmail = claims.get("email", String.class);

        assertThat(refreshToken).isNotNull();
        assertThat(authEmail).isEqualTo(email);
        assertThat(claims.getExpiration()).isAfter(new Date());
    }

    @Test
    @DisplayName("RefreshToken 에서 Email 추출")
    void extractEmailFromRefreshToken() {
        // given
        String refreshToken = getRefreshToken();

        // when
        String extractEmail = jwtTokenProvider.extractEmailFromRefreshToken(refreshToken);

        // then
        assertThat(extractEmail).isNotNull();
        assertThat(extractEmail).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("잘못된 형식의 토큰으로 인한 검증 실패")
    void failedWithInvalidToken() {
        // given
        String token = "aaaaaaa.bbbbbbbb.ccccccc";

        // when // then
        assertThatThrownBy(() ->
            Jwts.parserBuilder()
                .setSigningKey(testAccessSecret.getBytes())
                .build()
                .parseClaimsJws(token))
            .isInstanceOf(MalformedJwtException.class);
    }

    @Test
    @DisplayName("잘못된 서명으로 인한 토큰 검증 실패")
    void whenTokenHasInvalidSignature() {
        // given
        String invalidSecret = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String email = "test@test.com";
        String accessToken = jwtTokenProvider.generateAccessToken(email);

        // when // then
        assertThatThrownBy(() ->
            Jwts.parserBuilder()
                .setSigningKey(invalidSecret.getBytes())
                .build()
                .parseClaimsJws(accessToken))
            .isInstanceOf(io.jsonwebtoken.security.SignatureException.class);
    }

    private String getRefreshToken() {
        String email = "test@test.com";
        return jwtTokenProvider.generateRefreshToken(email);
    }
}