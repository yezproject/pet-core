package org.yproject.pet.core.domain.category.value_objects;

import org.springframework.lang.NonNull;

import java.util.Objects;

public record CategoryId(
        @NonNull String value
) {
    public CategoryId {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
