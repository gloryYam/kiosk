package sample.cafekiosk.spring.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

import static jakarta.persistence.EnumType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String phone;

    @Enumerated(STRING)
    private MemberRole memberRole;

    @Builder
    public Member(String name, String email, String password, String phone, MemberRole memberRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.memberRole = memberRole;
    }

    public static Member of(String name, String email, String password, String phone) {
        return new Member(name, email, password, phone, MemberRole.ADMIN);
    }
}
