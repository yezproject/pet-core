package org.yproject.pet.core.infrastructure.web.apis.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record ModifyTransactionRequest(
        String categoryId,
        @NotBlank String description,
        @NotNull Double amount,
        @NotBlank String currency,
        @NotNull Long createTime
) {
}
