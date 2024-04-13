package sample.cafekiosk.spring.config.security.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import sample.cafekiosk.spring.domain.member.Member;

import java.util.Collection;

@Getter
public class AccountContext extends User {

    private final Long id;

    public AccountContext(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getEmail(), member.getPassword(), authorities);

        this.id = member.getId();
    }
}
