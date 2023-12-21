package org.yproject.pet.core.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record SignInResponse(
        @JsonProperty(value = "token") String token
        ) {
    public SignInResponse {
        Objects.requireNonNull(token);
    }
}
