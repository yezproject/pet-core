package org.yezproject.pet.jwt;

import java.util.Objects;

public record JwtUserRequest(
        String email,
        String name
) implements JwtUserInfo {

    public JwtUserRequest {
        Objects.requireNonNull(email);
        Objects.requireNonNull(name);
    }
}
