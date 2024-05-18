package org.yproject.pet.transaction;

import java.time.Instant;

public record DeleteInfo(
        Instant date,
        String reason
) {
}
