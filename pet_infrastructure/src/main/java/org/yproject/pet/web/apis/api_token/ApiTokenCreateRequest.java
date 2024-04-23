package org.yproject.pet.web.apis.api_token;

import jakarta.validation.constraints.NotBlank;

record ApiTokenCreateRequest(
        @NotBlank String name
) {
}
