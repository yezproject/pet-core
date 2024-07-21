package org.yezproject.pet.authentication.infrastructure.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.yezproject.pet.security.token.ApiTokenAuthenticationFilter;
import org.yezproject.pet.security.token.ApiTokenAuthenticationProvider;
import org.yezproject.pet.security.token.ApiTokenAuthenticationService;

@Configuration
class ApiTokenAuthConfig {
    @Bean
    ApiTokenAuthenticationProvider apiTokenAuthenticationProvider(
            ApiTokenAuthenticationService apiTokenAuthenticationService
    ) {
        return new ApiTokenAuthenticationProvider(apiTokenAuthenticationService);
    }

    @Bean
    ApiTokenAuthenticationFilter apiTokenAuthenticationFilter(
            AuthenticationManager authenticationManager) {
        return new ApiTokenAuthenticationFilter(authenticationManager);
    }
}
