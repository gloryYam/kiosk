package sample.cafekiosk.spring.exception.token;

import sample.cafekiosk.spring.exception.ErrorCode_404;
import sample.cafekiosk.spring.exception.custom.Custom404Exception;

public class TokenNotFoundException extends Custom404Exception {

    public TokenNotFoundException(String email) {
        super(ErrorCode_404.TOKEN_NOT_FOUND.getStatus(),
            "토큰을 찾을 수 없습니다. - request info { email : %s }", email);
    }
}
