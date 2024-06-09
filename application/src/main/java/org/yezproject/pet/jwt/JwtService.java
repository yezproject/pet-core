package org.yezproject.pet.jwt;

import lombok.experimental.StandardException;

public interface JwtService {
    @StandardException
    class TokenExpiredException extends Exception {
    }

    @StandardException
    class TokenInvalidException extends Exception {
    }

    String generateToken(String email);

    JwtPayload extractPayload(String token) throws TokenExpiredException, TokenInvalidException;
}
