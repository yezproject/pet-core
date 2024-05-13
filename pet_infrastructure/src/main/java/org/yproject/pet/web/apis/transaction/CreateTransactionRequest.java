package org.yproject.pet.web.apis.transaction;

record CreateTransactionRequest(
        String categoryId,
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
