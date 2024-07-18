package org.yezproject.pet.transaction.application.transaction.driven;

public record CreateTransactionDto(
        String userId,
        String categoryId,
        String name,
        Double amount,
        Long transactionDate
) {
}
