package sample.cafekiosk.spring.domain.jwt;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.member.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    public Token(final Member member, final String refreshToken) {
        this.member = member;
        this.refreshToken = refreshToken;
    }

    public static Token of(Member member, String refreshToken) {
        return new Token(member, refreshToken);
    }

    public void changeToken(String generateRefreshToken) {
        this.refreshToken = generateRefreshToken;
    }
}
