package org.yproject.pet.core.infrastructure.web.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractEmail(String token);

    String generateToken(String email);

    boolean isTokenValid(String token, UserDetails userDetails);
}