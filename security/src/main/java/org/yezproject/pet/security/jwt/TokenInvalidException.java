package org.yezproject.pet.security.jwt;

public class TokenInvalidException extends Exception {
    public TokenInvalidException(String message) {
        this(message, null);
    }

    public TokenInvalidException(Throwable cause) {
        this(cause != null ? cause.getMessage() : null, cause);
    }

    public TokenInvalidException(String message, Throwable cause) {
        super(message);
        if (cause != null) super.initCause(cause);
    }
}
