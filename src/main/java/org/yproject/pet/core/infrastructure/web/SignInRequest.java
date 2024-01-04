package org.yproject.pet.core.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

record SignInRequest(
        @JsonProperty(value = "email") String email,
        @JsonProperty(value = "password") String password) {
    SignInRequest {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
