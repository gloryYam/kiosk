package sample.cafekiosk.spring.domain.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sample.cafekiosk.spring.domain.member.Member;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByRefreshToken(final String RefreshToken);
    Optional<Token> findByMember(final Member member);

    @Query("DELETE from Token t where t.member.id = :memberId")
    @Modifying
    void deleteByMemberId(final Long memberId);
}
