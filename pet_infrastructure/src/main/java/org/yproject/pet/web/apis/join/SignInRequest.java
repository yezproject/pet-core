package org.yproject.pet.web.apis.join;

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
