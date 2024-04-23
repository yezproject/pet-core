package org.yproject.pet.transaction.driven;

public record CreateTransactionDto(
        String categoryId,
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
