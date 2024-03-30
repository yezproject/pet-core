package org.yproject.pet.core.domain.user.value_objects;

import java.util.Objects;

public record UserId(
        String value
) {
    public UserId {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
