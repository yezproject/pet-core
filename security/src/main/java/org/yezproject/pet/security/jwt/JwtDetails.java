package org.yezproject.pet.security.jwt;

import java.time.Instant;

public record JwtDetails (
        String uid,
        String subject,
        Instant expiration,
        String name
) {
}
