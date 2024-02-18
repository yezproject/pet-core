package org.yproject.pet.core.infrastructure.web.apis.transaction;

record ModifyTransactionRequest(
        String id,
        String description,
        double amount,
        String currency
) {
}
