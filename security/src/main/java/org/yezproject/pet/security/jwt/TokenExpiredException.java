package org.yezproject.pet.security.jwt;

public class TokenExpiredException extends Exception {

    public TokenExpiredException(String message) {
        this(message, null);
    }

    public TokenExpiredException(Throwable cause) {
        this(cause != null ? cause.getMessage() : null, cause);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message);
        if (cause != null) super.initCause(cause);
    }
}
