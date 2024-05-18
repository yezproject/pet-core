package org.yproject.pet.web.apis.transaction;

record CreateTransactionRequest(
        String categoryId,
        String name,
        Double amount,
        String currency,
        Long transactionDate
) {
}
