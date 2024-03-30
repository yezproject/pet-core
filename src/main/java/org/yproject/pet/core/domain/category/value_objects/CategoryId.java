package org.yproject.pet.core.domain.category.value_objects;

import java.util.Objects;

public record CategoryId(
        String value
) {
    public CategoryId {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
