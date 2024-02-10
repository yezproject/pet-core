package org.yproject.pet.core.infrastructure.web.config.jwt;

import org.yproject.pet.core.infrastructure.web.config.security.UserInfo;

public interface JwtService {
    String extractEmail(String token);

    String generateToken(String email);

    boolean isTokenValid(String token, UserInfo userDetails);
}
