package org.yproject.pet.core.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

record SignInResponse(
        @JsonProperty(value = "token") String token) {
    SignInResponse {
        Objects.requireNonNull(token);
    }
}
