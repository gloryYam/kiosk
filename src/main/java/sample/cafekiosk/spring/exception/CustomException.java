package sample.cafekiosk.spring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException{

    private int status;
    private String message;

    public CustomException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public CustomException(int status, String errorMessage, String errorMessageArgument) {
        this(status, String.format(errorMessage, errorMessageArgument));
    }

    public abstract String getErrorCode();
}
