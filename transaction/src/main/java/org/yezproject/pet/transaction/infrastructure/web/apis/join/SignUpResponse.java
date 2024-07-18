package org.yezproject.pet.transaction.infrastructure.web.apis.join;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

record SignUpResponse(
        @JsonProperty(value = "userId") String userId) {
    SignUpResponse {
        Objects.requireNonNull(userId);
    }
}
