package org.yproject.pet.core.infrastructure.web.apis.transaction;

record CreateTransactionRequest(
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
