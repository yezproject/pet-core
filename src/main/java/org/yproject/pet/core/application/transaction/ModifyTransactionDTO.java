package org.yproject.pet.core.application.transaction;

import org.springframework.lang.Nullable;

public record ModifyTransactionDTO(
        @Nullable String categoryId,
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
