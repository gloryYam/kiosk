package sample.cafekiosk.spring.api.controller.member.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.member.request.LoginServiceRequest;

@Getter
@NoArgsConstructor
public class LoginRequest {

    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginServiceRequest toServiceRequest() {
        return LoginServiceRequest.builder()
            .email(email)
            .password(password)
            .build();
    }
}
