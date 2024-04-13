package sample.cafekiosk.spring.api.controller.member;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.ApiResponse;
import sample.cafekiosk.spring.api.controller.member.request.LoginRequest;
import sample.cafekiosk.spring.api.controller.member.request.SignupRequest;
import sample.cafekiosk.spring.api.service.jwt.dto.TokenResponse;
import sample.cafekiosk.spring.api.service.member.MemberService;
import sample.cafekiosk.spring.api.service.member.response.SignResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private static final String ACCESS_TOKEN_HEADER = HttpHeaders.AUTHORIZATION;
    private static final String REFRESH_TOKEN_HEADER = "Authorization-Refresh";

    @PostMapping("/api/auth/new")
    public ApiResponse<SignResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ApiResponse.ok(memberService.signup(request.toServiceRequest()));
    }

    @PostMapping("/api/login")
    public ApiResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse token = memberService.login(loginRequest.toServiceRequest());
        response.setHeader(ACCESS_TOKEN_HEADER, token.accessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, token.refreshToken());
        return ApiResponse.ok(token);
    }
}
