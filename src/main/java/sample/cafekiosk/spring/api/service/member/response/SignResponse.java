package sample.cafekiosk.spring.api.service.member.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.member.Member;
import sample.cafekiosk.spring.domain.member.MemberRole;

@Getter
public class SignResponse {

    private String name;
    private String email;
    private MemberRole role;

    @Builder
    public SignResponse(String name, String email, MemberRole role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static SignResponse of(Member member) {
        return SignResponse.builder()
            .name(member.getName())
            .email(member.getEmail())
            .role(member.getMemberRole())
            .build();
    }
}
