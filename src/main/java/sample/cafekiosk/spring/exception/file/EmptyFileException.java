package sample.cafekiosk.spring.exception.file;

import sample.cafekiosk.spring.exception.custom.Custom400Exception;
import sample.cafekiosk.spring.exception.ErrorCode_400;

public class EmptyFileException extends Custom400Exception {


    public EmptyFileException() {
        super(ErrorCode_400.EMPTY_FILE_EXCEPTION);
    }
}
