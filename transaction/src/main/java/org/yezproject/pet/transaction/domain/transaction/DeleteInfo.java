package org.yezproject.pet.transaction.domain.transaction;

import java.time.Instant;

public record DeleteInfo(
        String id,
        Instant date,
        String reason
) {
}
