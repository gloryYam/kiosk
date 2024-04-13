package sample.cafekiosk.spring.exception.custom;

import lombok.Getter;
import sample.cafekiosk.spring.exception.CustomException;
import sample.cafekiosk.spring.exception.ErrorCode_404;

@Getter
public abstract class Custom404Exception extends CustomException {

    private ErrorCode_404 errorCode404;

    public Custom404Exception(ErrorCode_404 errorCode404) {
        super(errorCode404.getStatus(), errorCode404.getErrorMessage());
        this.errorCode404 = errorCode404;
    }

    public Custom404Exception(int status, String s, String email) {
        super(status, String.format(s, email));
    }

    @Override
    public String getErrorCode() {
        return errorCode404.name();
    }
}
