package org.yproject.pet.core.application.transaction;

public record CreateTransactionDTO(
        String categoryId,
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
