package org.yezproject.pet.gateway;

import java.time.Instant;

public record JwtDetails(
        String uid,
        String subject,
        Instant expiration,
        String name
) {
}
