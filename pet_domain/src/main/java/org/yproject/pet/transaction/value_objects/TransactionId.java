package org.yproject.pet.transaction.value_objects;

import java.util.Objects;

public record TransactionId(
        // TODO: Add @NonNull
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