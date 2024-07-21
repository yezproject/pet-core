package org.yezproject.pet.authentication.application.user.driven;

import java.util.Objects;

public record SignUpDto(
        String fullName,
        String email,
        String password
) {
    public SignUpDto {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
