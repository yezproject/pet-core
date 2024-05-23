package org.yezproject.pet.web.apis.transaction;

record CreateTransactionRequest(
        String categoryId,
        String name,
        Double amount,
        Long transactionDate
) {
}
