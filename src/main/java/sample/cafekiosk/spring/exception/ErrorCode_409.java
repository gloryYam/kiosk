package sample.cafekiosk.spring.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode_409 {

    EMAIL_DUPLICATED_CHECK(HttpStatus.CONFLICT.value(), "중복된 이메일입니다.");

    private final int status;
    private final String errorMessage;
}
