package org.yezproject.pet.authentication.infrastructure.web.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
class JwtProperties {
    private String secret;
    private Long expiration;
}
