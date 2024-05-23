package org.yezproject.pet.web.apis.join;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

record SignUpRequest(
        @JsonProperty(value = "fullName") String fullName,
        @JsonProperty(value = "email") String email,
        @JsonProperty(value = "password") String password
) {
    SignUpRequest {
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
    }
}
