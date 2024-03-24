package org.yproject.pet.core.application.transaction;

public record CreateTransactionDTO(
        String description,
        Double amount,
        String currency,
        Long createTime,
        String categoryId
) {
}
