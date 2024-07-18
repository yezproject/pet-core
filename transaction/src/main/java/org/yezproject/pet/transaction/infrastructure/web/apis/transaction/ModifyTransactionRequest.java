package org.yezproject.pet.transaction.infrastructure.web.apis.transaction;

record ModifyTransactionRequest(
        String categoryId,
        String name,
        Double amount,
        Long transactionDate
) {
}
