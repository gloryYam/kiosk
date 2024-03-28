package sample.cafekiosk.spring.exception.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sample.cafekiosk.spring.exception.CustomException;
import sample.cafekiosk.spring.exception.custom.Custom404Exception;

@Getter
public class ErrorResponse {

    private String code;
    private String message;
    private HttpStatus status;

    @Builder
    public ErrorResponse(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }


    public static ResponseEntity<ErrorResponse> error(CustomException e) {
        return ResponseEntity
            .status(e.getStatus())
            .body(ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .status(e.getStatus())
                .build());
    }
}
