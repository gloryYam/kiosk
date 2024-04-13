package sample.cafekiosk.spring.auth.exception;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException(String message) {
        super(message);
    }

    public static class FailAuthenticationException extends AuthenticationException {

        public FailAuthenticationException(String logMessage) {
            super(logMessage);
        }
    }
}
