package org.yezproject.pet.authentication.infrastructure.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.yezproject.pet.security.jwt.JwtAuthenticationFilter;
import org.yezproject.pet.security.jwt.JwtAuthenticationProvider;
import org.yezproject.pet.security.jwt.JwtHelper;

@Configuration
class JwtAuthConfig {

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(authenticationManager);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(
            JwtHelper jwtHelper,
            UserDetailsService userDetailsService
    ) {
        return new JwtAuthenticationProvider(jwtHelper, userDetailsService);
    }

    @Bean
    JwtHelper jwtHelper(JwtProperties jwtProperties) {
        return new JwtHelper(jwtProperties.getSecret(), jwtProperties.getExpiration());
    }
}
