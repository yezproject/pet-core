package org.yproject.pet.jwt;

import java.util.Set;

public interface JwtService {
    String extractEmail(String token);

    String generateToken(String email);

    String generateToken(String email, String tokenId);

    boolean isTokenValid(String token, String email, Set<String> tokenIDs);

}
