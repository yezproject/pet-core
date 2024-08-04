package org.yezproject.pet.authentication.infrastructure.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    SecurityFilterChain publicSecurityFilterChain(
            final HttpSecurity http,
            final AuthenticationEntryPoint authenticationEntryPoint
    ) throws Exception {
        return applyDefaultConfig(http, authenticationEntryPoint)
                .securityMatcher("/public/**")
                .authorizeHttpRequests(req ->
                        req.anyRequest().permitAll()
                )
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain internalSecurityFilterChain(
            final HttpSecurity http,
            final AuthenticationEntryPoint authenticationEntryPoint,
            final InternalAuthProperties internalAuthProperties
    ) throws Exception {
        return applyDefaultConfig(http, authenticationEntryPoint)
                .securityMatcher("/internal/**")
                .addFilterBefore(new InternalAuthenticationFilter(internalAuthProperties), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers("/internal/tokens/**").authenticated()
                                .anyRequest().denyAll()
                )
                .build();
    }

    @Bean
    @Order(3)
    SecurityFilterChain securityFilterChain(
            final HttpSecurity http,
            final AuthenticationEntryPoint authenticationEntryPoint
    ) throws Exception {
        return applyDefaultConfig(http, authenticationEntryPoint)
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers("/tokens/**").authenticated()
                                .requestMatchers("/auth/**").authenticated()
                                .anyRequest().denyAll()
                )
                .build();
    }

    private HttpSecurity applyDefaultConfig(HttpSecurity http, AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .exceptionHandling(handling -> handling.authenticationEntryPoint(authenticationEntryPoint));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtGenerator jwtGenerator() {
        var defaultSecret = "ZWE0MWM1NTI1MjczNjM0YmJjNDhmYjU1YTg0N2E4YWU3ZWJkNjQ4ZDJiMmExYjVlMTVkMDUwYjU3NjU0ZDliYw==";
        var defaultExpiration = 21L;
        return new JwtGenerator(
                defaultSecret,
                defaultExpiration
        );
    }

}
