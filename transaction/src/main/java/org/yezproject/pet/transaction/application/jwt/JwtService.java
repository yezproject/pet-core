package org.yezproject.pet.transaction.application.jwt;

import lombok.experimental.StandardException;

public interface JwtService {
    @StandardException
    class TokenExpiredException extends Exception {
    }

    @StandardException
    class TokenInvalidException extends Exception {
    }

    String generateToken(JwtUserRequest jwtUserRequest);

    JwtPayload extractPayload(String token) throws TokenExpiredException, TokenInvalidException;
}
