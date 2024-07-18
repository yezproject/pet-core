package org.yezproject.pet.transaction.application.transaction.driven;

import org.springframework.lang.Nullable;

public record ModifyTransactionDto(
        String userId,
        String transactionId,
        @Nullable String categoryId,
        String name,
        Double amount,
        Long transactionDate
) {
}
