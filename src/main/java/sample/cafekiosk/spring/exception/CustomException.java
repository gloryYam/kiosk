package sample.cafekiosk.spring.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException{

    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}
