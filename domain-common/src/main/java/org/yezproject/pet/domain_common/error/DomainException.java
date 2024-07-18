package org.yezproject.pet.domain_common.error;

public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}
