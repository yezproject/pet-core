package org.yproject.pet.core.infrastructure.web.apis.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record ModifyTransactionRequest(
        @NotNull String id,
        @NotBlank String description,
        @NotNull Double amount,
        @NotBlank String currency,
        @NotNull Long createTime
) {
}
