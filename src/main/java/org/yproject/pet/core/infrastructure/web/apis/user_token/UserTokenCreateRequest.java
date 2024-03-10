package org.yproject.pet.core.infrastructure.web.apis.user_token;

import jakarta.validation.constraints.NotBlank;

record UserTokenCreateRequest(
        @NotBlank String name
) {
}
