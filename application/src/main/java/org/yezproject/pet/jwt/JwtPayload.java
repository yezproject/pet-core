package org.yezproject.pet.jwt;

import java.time.Instant;
import java.util.Objects;

public record JwtPayload(
        String email,
        Instant expiration,
        String name
) implements JwtUserInfo, JwtTokenInfo {

    public JwtPayload {
        Objects.requireNonNull(email);
        Objects.requireNonNull(expiration);
        Objects.requireNonNull(name);
    }
}
