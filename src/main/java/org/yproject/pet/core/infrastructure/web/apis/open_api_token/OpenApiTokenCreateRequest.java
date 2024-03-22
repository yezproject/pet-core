package org.yproject.pet.core.infrastructure.web.apis.open_api_token;

import jakarta.validation.constraints.NotBlank;

record OpenApiTokenCreateRequest(
        @NotBlank String name
) {
}
