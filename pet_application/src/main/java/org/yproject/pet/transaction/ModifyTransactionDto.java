package org.yproject.pet.transaction;

import org.springframework.lang.Nullable;

public record ModifyTransactionDto(
        @Nullable String categoryId,
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
