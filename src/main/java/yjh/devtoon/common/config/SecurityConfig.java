package yjh.devtoon.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import yjh.devtoon.auth.jwt.JwtAccessDeniedHandler;
import yjh.devtoon.auth.jwt.JwtAuthenticationEntryPoint;
import yjh.devtoon.auth.jwt.JwtSecurityConfig;
import yjh.devtoon.auth.jwt.TokenProvider;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/v1/auth/authenticate").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/members").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/bad-words-warning-count").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/cookie-wallets").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/webtoons").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/webtoons/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/webtoons/*/images/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/comments/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/promotions/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/promotions/*/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/policies/*").permitAll()
                        .anyRequest().authenticated())

                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .with(new JwtSecurityConfig(tokenProvider), customizer -> {});

        return http.build();
    }
}
