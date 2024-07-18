package org.yezproject.pet.transaction.infrastructure.web.apis.transaction;

record CreateTransactionRequest(
        String categoryId,
        String name,
        Double amount,
        Long transactionDate
) {
}
