package sample.cafekiosk.spring.api.service.member.request;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.domain.member.Member;

@Getter
public class SignupServiceRequest {

    private String name;
    private String email;
    private String password;
    private String phone;

    @Builder
    public SignupServiceRequest(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public Member toEntity() {
        return Member.builder()
            .name(name)
            .email(email)
            .password(password)
            .phone(phone)
            .build();
    }
}
