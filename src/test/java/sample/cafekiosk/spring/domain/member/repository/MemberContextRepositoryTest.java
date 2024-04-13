package sample.cafekiosk.spring.domain.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.member.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberContextRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("이메일로 회원 찾기")
    void findByEmail() {
        // given
        Member member = Member.builder()
            .name("글로리")
            .email("test@naver.com")
            .password("1234")
            .build();
        memberRepository.save(member);
        // when
        Member byEmail = memberRepository.findByEmail("test@naver.com")
            .orElseThrow();

        // then
        assertThat(byEmail.getEmail()).isEqualTo("test@naver.com");
        assertThat(byEmail.getName()).isEqualTo("글로리");
    }
}