package org.yproject.pet.core.infrastructure.web.apis.transaction;

record CreateTransactionRequest(
        String description,
        double amount,
        String currency
) {
}
