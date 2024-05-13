package org.yproject.pet.transaction.value_objects;

import org.yproject.pet.common.models.EntityId;

import java.util.Objects;

public record TransactionId(
        String value
) implements EntityId {
    public TransactionId {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String getId() {
        return value;
    }
}
