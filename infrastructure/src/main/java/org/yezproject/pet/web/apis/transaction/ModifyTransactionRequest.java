package org.yezproject.pet.web.apis.transaction;

record ModifyTransactionRequest(
        String categoryId,
        String name,
        Double amount,
        Long transactionDate
) {
}