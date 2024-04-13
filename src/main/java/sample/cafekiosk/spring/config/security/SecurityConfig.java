package sample.cafekiosk.spring.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sample.cafekiosk.spring.auth.JwtTokenExtractor;
import sample.cafekiosk.spring.auth.JwtTokenProvider;
import sample.cafekiosk.spring.config.security.filter.JwtAuthenticationFilter;
import sample.cafekiosk.spring.config.security.service.UserDetailsServiceImpl;
import sample.cafekiosk.spring.domain.member.repository.MemberRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .requestMatchers("/favicon.ico", "/resources/**", "/error");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter(jwtTokenProvider, jwtTokenExtractor), UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, JwtTokenExtractor jwtTokenExtractor) {
        return new JwtAuthenticationFilter(jwtTokenProvider, jwtTokenExtractor);
    }


    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl(memberRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
