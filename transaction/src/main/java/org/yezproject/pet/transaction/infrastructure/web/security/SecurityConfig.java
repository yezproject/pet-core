package org.yezproject.pet.transaction.infrastructure.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.yezproject.pet.security.jwt.JwtAuthConfig;
import org.yezproject.pet.security.jwt.JwtAuthenticationFilter;
import org.yezproject.pet.security.jwt.JwtAuthenticationProvider;
import org.yezproject.pet.security.token.ApiTokenAuthConfig;
import org.yezproject.pet.security.token.ApiTokenAuthenticationFilter;
import org.yezproject.pet.security.token.ApiTokenAuthenticationProvider;

@Configuration
@EnableWebSecurity
@Import({JwtAuthConfig.class, ApiTokenAuthConfig.class})
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            final HttpSecurity http,
            final AuthenticationManager authenticationManager,
            final AuthenticationEntryPoint authenticationEntryPoint
    ) throws Exception {
        return applyDefaultConfig(http, authenticationEntryPoint)
                .addFilterBefore(new ApiTokenAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager), ApiTokenAuthenticationFilter.class)
                .authorizeHttpRequests(req ->
                        req
                                .anyRequest().authenticated()
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
    AuthenticationManager authenticationManager(
            JwtAuthenticationProvider jwtAuthenticationProvider,
            ApiTokenAuthenticationProvider apiTokenAuthenticationProvider
    ) {
        return new ProviderManager(jwtAuthenticationProvider, apiTokenAuthenticationProvider);
    }
}
