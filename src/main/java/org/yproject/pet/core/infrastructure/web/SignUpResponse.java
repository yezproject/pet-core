package org.yproject.pet.core.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

record SignUpResponse(
        @JsonProperty(value = "userId") String userId) {
    SignUpResponse {
        Objects.requireNonNull(userId);
    }
}
