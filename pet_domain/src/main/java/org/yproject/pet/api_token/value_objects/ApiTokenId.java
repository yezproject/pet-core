package org.yproject.pet.api_token.value_objects;


import org.yproject.pet.common.models.EntityId;

import java.util.Objects;

public record ApiTokenId(
        String value
) implements EntityId {

    public ApiTokenId {
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
