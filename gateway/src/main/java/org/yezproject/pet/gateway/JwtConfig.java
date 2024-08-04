package org.yezproject.pet.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    JwtHelper jwtHelper() {
        var defaultSecret = "ZWE0MWM1NTI1MjczNjM0YmJjNDhmYjU1YTg0N2E4YWU3ZWJkNjQ4ZDJiMmExYjVlMTVkMDUwYjU3NjU0ZDliYw==";
        var defaultExpiration = 21L;
        return new JwtHelper(defaultSecret, defaultExpiration);
    }
}
