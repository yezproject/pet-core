package org.yezproject.pet.security.token;

import org.springframework.context.annotation.Bean;

public class ApiTokenAuthConfig {

    @Bean
    ApiTokenAuthenticationProvider apiTokenAuthenticationProvider(
            ApiTokenAuthenticationService apiTokenAuthenticationService
    ) {
        return new ApiTokenAuthenticationProvider(apiTokenAuthenticationService);
    }

}
