package sample.cafekiosk.spring.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.cafekiosk.spring.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}
