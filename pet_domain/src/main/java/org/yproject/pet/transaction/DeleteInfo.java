package org.yproject.pet.transaction;

import java.time.Instant;

public record DeleteInfo(
        String id,
        Instant date,
        String reason
) {
}
