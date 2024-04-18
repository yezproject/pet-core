package org.yproject.pet.web.apis.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

record CreateTransactionRequest(
        String categoryId,
        @NotBlank String description,
        @NotNull Double amount,
        @NotBlank String currency,
        @NotNull Long createTime
) {
}
