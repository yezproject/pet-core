package org.yproject.pet.core.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record SignUpResponse(
        @JsonProperty(value = "userId") String userId
        ) {
    public SignUpResponse {
        Objects.requireNonNull(userId);
    }
}
