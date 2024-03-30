package org.yproject.pet.core.domain.transaction.value_objects;

import java.util.Objects;

public record TransactionId(
        String value
) {
    public TransactionId {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
