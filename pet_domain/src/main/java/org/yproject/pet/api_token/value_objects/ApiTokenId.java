package org.yproject.pet.api_token.value_objects;


import java.util.Objects;

public record ApiTokenId(
        // TODO: Add @NonNull
        String value
) {

    public ApiTokenId {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
