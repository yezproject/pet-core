package org.yezproject.pet.authentication.infrastructure.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.yezproject.pet.authentication.application.user.driven.UserQuery;
import org.yezproject.pet.security.PetUserDetails;
import org.yezproject.pet.security.jwt.JwtAuthenticationFilter;
import org.yezproject.pet.security.jwt.JwtAuthenticationProvider;
import org.yezproject.pet.security.token.ApiTokenAuthenticationFilter;
import org.yezproject.pet.security.token.ApiTokenAuthenticationProvider;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            final HttpSecurity http,
            final JwtAuthenticationFilter jwtAuthenticationFilter,
            final ApiTokenAuthenticationFilter apiTokenAuthenticationFilter,
            final AuthenticationEntryPoint authenticationEntryPoint
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/public/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(apiTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, ApiTokenAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserQuery userQuery) {
        return email -> {
            try {
                final var userInfo = userQuery.getByEmail(email);
                return new PetUserDetails(userInfo.userId(), userInfo.email());
            } catch (UserQuery.UserNotFoundException e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        };
    }

    @Bean
    AuthenticationManager authenticationManager(
            JwtAuthenticationProvider jwtAuthenticationProvider,
            ApiTokenAuthenticationProvider apiTokenAuthenticationProvider
    ) {
        return new ProviderManager(jwtAuthenticationProvider, apiTokenAuthenticationProvider);
    }


}
