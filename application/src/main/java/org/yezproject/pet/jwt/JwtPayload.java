package org.yezproject.pet.jwt;

import java.time.Instant;
import java.util.Objects;

public record JwtPayload(
        String email,
        Instant expiration
) {

    public JwtPayload {
        Objects.requireNonNull(email);
        Objects.requireNonNull(expiration);
    }
}
