package sample.cafekiosk.spring.api.service.jwt.dto;

public record TokenResponse(
    String accessToken,
    String refreshToken
) {
    public static TokenResponse of(final String accessToken, final String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }

    public static TokenResponse of(final String refreshToken) {
        return new TokenResponse("" , refreshToken);
    }
}
