package sample.cafekiosk.spring.api.controller.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.member.request.SignupServiceRequest;

@Getter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "핸드포 번호를 입력해주세요.")
    private String phone;

    @Builder
    public SignupRequest(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public SignupServiceRequest toServiceRequest() {
        return SignupServiceRequest.builder()
            .name(name)
            .email(email)
            .password(password)
            .phone(phone)
            .build();
    }
}
