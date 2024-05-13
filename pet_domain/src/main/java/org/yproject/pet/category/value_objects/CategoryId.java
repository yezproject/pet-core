package org.yproject.pet.category.value_objects;

import org.yproject.pet.common.models.EntityId;

import java.util.Objects;

public record CategoryId(
        String value
) implements EntityId {
    public CategoryId {
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
