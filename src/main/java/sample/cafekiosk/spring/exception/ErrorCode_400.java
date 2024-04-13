package sample.cafekiosk.spring.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode_400 {

    EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST.value(), "파일 비어있습니다.");

    private final int status;
    private final String errorMessage;

}
