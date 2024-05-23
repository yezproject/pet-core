package org.yezproject.pet.transaction.driven;

public record CreateTransactionDto(
        String userId,
        String categoryId,
        String name,
        Double amount,
        Long transactionDate
) {
}
