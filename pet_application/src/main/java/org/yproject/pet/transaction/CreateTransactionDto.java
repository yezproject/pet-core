package org.yproject.pet.transaction;

public record CreateTransactionDto(
        String categoryId,
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
