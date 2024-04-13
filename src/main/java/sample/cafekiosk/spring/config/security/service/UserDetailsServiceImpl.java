package sample.cafekiosk.spring.config.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sample.cafekiosk.spring.domain.member.Member;
import sample.cafekiosk.spring.domain.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        List<SimpleGrantedAuthority> authorities = Stream.of(member.getMemberRole())
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .toList();

        return new AccountContext(member, authorities);
    }
}
