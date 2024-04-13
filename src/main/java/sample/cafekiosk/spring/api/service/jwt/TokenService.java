package sample.cafekiosk.spring.api.service.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.jwt.dto.TokenResponse;
import sample.cafekiosk.spring.auth.JwtTokenProvider;
import sample.cafekiosk.spring.domain.jwt.Token;
import sample.cafekiosk.spring.domain.jwt.TokenRepository;
import sample.cafekiosk.spring.domain.member.Member;
import sample.cafekiosk.spring.domain.member.repository.MemberRepository;
import sample.cafekiosk.spring.exception.token.TokenNotFoundException;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public void saveToken(final String refreshToken, Member member) {
        Token token = Token.of(member, refreshToken);
        tokenRepository.save(token);
    }

    public TokenResponse reissueToken(final String refreshToken) {
        String email = jwtTokenProvider.extractEmailFromRefreshToken(refreshToken);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("해당 회원을 찾을 수 없습니다."));

        List<String> authorities = Stream.of(member)
            .map(m -> m.getMemberRole().name())
            .toList();

        String generateAccessToken = jwtTokenProvider.generateAccessToken(email, authorities);
        String generateRefreshToken = jwtTokenProvider.generateRefreshToken(email);
        Token token = tokenRepository.findByRefreshToken(generateRefreshToken)
            .orElseThrow(() -> new TokenNotFoundException(email));

        token.changeToken(generateRefreshToken);
        tokenRepository.save(token);
        log.info("인증 토큰 재발급 - 재발급 받은 사용자 이메일 : {}", email);
        return TokenResponse.of(generateAccessToken, generateRefreshToken);
    }
}
