package org.yproject.pet.web.apis.transaction;

record ModifyTransactionRequest(
        String categoryId,
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
