package sample.cafekiosk.spring.exception.user;

import sample.cafekiosk.spring.exception.ErrorCode_409;
import sample.cafekiosk.spring.exception.custom.Custom409Exception;

public class EmailDuplicateException extends Custom409Exception {

    public EmailDuplicateException() {
        super(ErrorCode_409.EMAIL_DUPLICATED_CHECK);
    }
}
