package org.yproject.pet.transaction.driven;

import org.springframework.lang.Nullable;

public record ModifyTransactionDto(
        @Nullable String categoryId,
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
