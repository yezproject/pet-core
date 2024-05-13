package org.yproject.pet.user.value_objects;

import org.yproject.pet.common.models.EntityId;

import java.util.Objects;

public record UserId(
        String value
) implements EntityId {
    public UserId {
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
