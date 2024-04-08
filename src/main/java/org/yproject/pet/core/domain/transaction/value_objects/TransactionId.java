package org.yproject.pet.core.domain.transaction.value_objects;

import org.springframework.lang.NonNull;

import java.util.Objects;

public record TransactionId(
        @NonNull String value
) {
    public TransactionId {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
