package org.yezproject.pet.jwt;

public interface JwtService {
    String extractEmail(String token);

    String generateToken(String email);

    boolean isTokenValid(String token, String email);

    class TokenExpiredException extends RuntimeException {}
}
