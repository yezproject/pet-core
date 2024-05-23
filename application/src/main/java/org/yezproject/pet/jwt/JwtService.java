package org.yezproject.pet.jwt;

import org.yezproject.pet.Dao;

import java.util.Set;

@Dao
public interface JwtService {
    String extractEmail(String token);

    String generateToken(String email);

    String generateToken(String email, String tokenId);

    boolean isTokenValid(String token, String email, Set<String> tokenIDs);

}
