package sample.cafekiosk.spring.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sample.cafekiosk.spring.exception.custom.Custom400Exception;
import sample.cafekiosk.spring.exception.custom.Custom404Exception;
import sample.cafekiosk.spring.exception.custom.Custom409Exception;
import sample.cafekiosk.spring.exception.response.ErrorResponse;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            null);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Custom404Exception.class)
    public ResponseEntity<ErrorResponse> notFoundException(Custom404Exception e) {
        return ErrorResponse.error(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Custom400Exception.class)
    public ResponseEntity<ErrorResponse> badRequestException(Custom400Exception e) {
        return ErrorResponse.error(e);
    }

    @ExceptionHandler(Custom409Exception.class)
    public ResponseEntity<ErrorResponse> conflictException(Custom409Exception e) {
        return ErrorResponse.error(e);
    }
}
