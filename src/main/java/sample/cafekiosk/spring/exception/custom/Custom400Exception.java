package sample.cafekiosk.spring.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import sample.cafekiosk.spring.exception.CustomException;
import sample.cafekiosk.spring.exception.ErrorCode_400;

@Getter
public abstract class Custom400Exception extends CustomException {

    private ErrorCode_400 errorCode400;

    public Custom400Exception(ErrorCode_400 errorCode400) {
        super(errorCode400.getStatus(), errorCode400.getErrorMessage());
        this.errorCode400 = errorCode400;
    }

    @Override
    public String getErrorCode() {
        return errorCode400.name();
    }
}
