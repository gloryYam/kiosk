package sample.cafekiosk.spring.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode_404 {

    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "상품을 찾을 수 없습니다"),

    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST.value(),"회원을 찾을 수 없습니다."),

    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "토큰을 찾을 수 없습니다.");

    private final int status;
    private final String errorMessage;

}
