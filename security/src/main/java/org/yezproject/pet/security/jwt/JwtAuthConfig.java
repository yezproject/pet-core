package org.yezproject.pet.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtAuthConfig {

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(
            JwtHelper jwtHelper,
            UserDetailsService userDetailsService
    ) {
        return new JwtAuthenticationProvider(jwtHelper, userDetailsService);
    }

    @Bean
    JwtHelper jwtHelper() {
        var defaultSecret = "ZWE0MWM1NTI1MjczNjM0YmJjNDhmYjU1YTg0N2E4YWU3ZWJkNjQ4ZDJiMmExYjVlMTVkMDUwYjU3NjU0ZDliYw==";
        var defaultExpiration = 21L;
        return new JwtHelper(defaultSecret, defaultExpiration);
    }
}
