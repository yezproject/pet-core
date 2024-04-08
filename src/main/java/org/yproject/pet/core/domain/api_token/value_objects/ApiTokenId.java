package org.yproject.pet.core.domain.api_token.value_objects;

import org.springframework.lang.NonNull;

import java.util.Objects;

public record ApiTokenId(
        @NonNull String value
) {

    public ApiTokenId {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
