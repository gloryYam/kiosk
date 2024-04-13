package sample.cafekiosk.spring.api.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.jwt.TokenService;
import sample.cafekiosk.spring.api.service.jwt.dto.TokenResponse;
import sample.cafekiosk.spring.api.service.member.request.LoginServiceRequest;
import sample.cafekiosk.spring.api.service.member.request.SignupServiceRequest;
import sample.cafekiosk.spring.api.service.member.response.SignResponse;
import sample.cafekiosk.spring.auth.JwtTokenProvider;
import sample.cafekiosk.spring.domain.jwt.TokenRepository;
import sample.cafekiosk.spring.domain.member.Member;
import sample.cafekiosk.spring.domain.member.repository.MemberRepository;
import sample.cafekiosk.spring.exception.user.EmailDuplicateException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    public SignResponse signup(SignupServiceRequest request) {
        emailDuplicateCheck(request.getEmail());
        String encodePassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.of(request.getName(), request.getEmail(), encodePassword, request.getPhone());
        Member saveMember = memberRepository.save(member);

        return SignResponse.of(saveMember);
    }

    public TokenResponse login(LoginServiceRequest request) {
        Member member = findByEmail(request);

        var authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        List<String> authorities = authenticate.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        String accessToken = jwtTokenProvider.generateAccessToken(authenticate.getName(), authorities);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authenticate.getName());

        tokenService.saveToken(refreshToken, member);
        return TokenResponse.of(accessToken, refreshToken);
    }

    private void emailDuplicateCheck(String email) {
        memberRepository.findByEmail(email)
            .ifPresent(u -> {
                throw new EmailDuplicateException();
            });
    }

    private Member findByEmail(LoginServiceRequest request) {
        return memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("등록된 회원이 아닙니다."));
    }
}
