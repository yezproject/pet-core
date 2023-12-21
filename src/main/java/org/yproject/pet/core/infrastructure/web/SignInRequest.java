package org.yproject.pet.core.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record SignInRequest(
        @JsonProperty(value = "email") String email,
        @JsonProperty(value = "password") String password
) {
    public SignInRequest {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
