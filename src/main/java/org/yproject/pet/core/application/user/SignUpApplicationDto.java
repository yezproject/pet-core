package org.yproject.pet.core.application.user;

import java.util.Objects;

public record SignUpApplicationDto(
        String fullName,
        String email,
        String password
) {
    public SignUpApplicationDto {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
