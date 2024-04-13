package sample.cafekiosk.spring.config.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sample.cafekiosk.spring.auth.JwtTokenExtractor;
import sample.cafekiosk.spring.auth.JwtTokenProvider;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    private static final List<String> EXEMPTED_PATHS = List.of("/api/auth/new", "/api/login");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isExempted(request.getRequestURI())) {
            filterChain.doFilter(request,response);
            return;
        }

        try {
            proessAuthenticaation(request);
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExempted(String requestURI) {
        return EXEMPTED_PATHS.stream()
            .anyMatch(requestURI::startsWith);
    }

    private void proessAuthenticaation(HttpServletRequest request) {
        String token = jwtTokenExtractor.extractAccessToken(request);
        if (token != null && jwtTokenProvider.validateAccessToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }
}
