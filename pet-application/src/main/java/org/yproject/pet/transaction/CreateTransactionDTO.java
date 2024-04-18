package org.yproject.pet.transaction;

public record CreateTransactionDTO(
        String categoryId,
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
