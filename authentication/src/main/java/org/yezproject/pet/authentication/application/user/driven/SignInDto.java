package org.yezproject.pet.authentication.application.user.driven;

import java.util.Objects;

public record SignInDto(
        String email,
        String password
) {
    public SignInDto {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
