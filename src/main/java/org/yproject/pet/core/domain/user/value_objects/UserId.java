package org.yproject.pet.core.domain.user.value_objects;

import org.springframework.lang.NonNull;

import java.util.Objects;

public record UserId(
        @NonNull String value
) {
    public UserId {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
