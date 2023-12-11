package org.yproject.pet.core.configuration.jwt;

import org.yproject.pet.core.domain.UserInfo;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserInfo userDetails);

    boolean isTokenValid(String token, UserInfo userDetails);
}
