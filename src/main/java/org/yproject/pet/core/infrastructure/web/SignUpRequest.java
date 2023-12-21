package org.yproject.pet.core.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record SignUpRequest(
        @JsonProperty(value = "fullName") String fullName,
        @JsonProperty(value = "email") String email,
        @JsonProperty(value = "password") String password
        ) {
    public SignUpRequest {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
