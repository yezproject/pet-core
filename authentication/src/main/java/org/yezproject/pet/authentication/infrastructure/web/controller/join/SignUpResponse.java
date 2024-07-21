package org.yezproject.pet.authentication.infrastructure.web.controller.join;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

record SignUpResponse(
        @JsonProperty(value = "userId") String userId) {
    SignUpResponse {
        Objects.requireNonNull(userId);
    }
}
