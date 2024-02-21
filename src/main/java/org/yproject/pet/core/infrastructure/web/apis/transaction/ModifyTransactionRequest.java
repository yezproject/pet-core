package org.yproject.pet.core.infrastructure.web.apis.transaction;

record ModifyTransactionRequest(
        String id,
        String description,
        Double amount,
        String currency,
        Long createTime
) {
}
