package org.yezproject.pet.authentication.infrastructure.web.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
