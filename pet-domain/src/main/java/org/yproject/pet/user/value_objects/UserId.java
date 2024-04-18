package org.yproject.pet.user.value_objects;

import java.util.Objects;

public record UserId(
        // TODO: Add @NonNull
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
