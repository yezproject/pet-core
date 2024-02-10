package org.yproject.pet.core.configuration.jwt;

import org.yproject.pet.core.configuration.security.UserInfo;

public interface JwtService {
    String extractEmail(String token);

    String generateToken(String email);

    boolean isTokenValid(String token, UserInfo userDetails);
}
