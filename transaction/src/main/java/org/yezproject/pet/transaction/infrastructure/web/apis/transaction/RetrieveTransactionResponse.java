package org.yezproject.pet.transaction.infrastructure.web.apis.transaction;

import org.yezproject.pet.transaction.application.transaction.driven.RetrieveTransactionDto;

record RetrieveTransactionResponse(
        String id,
        String categoryId,
        String name,
        Double amount,
        RetrieveTransactionCurrencyResponse currency,
        Long transactionDate
) {

    static RetrieveTransactionResponse fromDTO(RetrieveTransactionDto dto) {
        return new RetrieveTransactionResponse(
                dto.id(),
                dto.categoryId(),
                dto.name(),
                dto.amount(),
                new RetrieveTransactionCurrencyResponse(dto.currency().name(), dto.currency().symbol()),
                dto.transactionDate().toEpochMilli()
        );
    }

    record RetrieveTransactionCurrencyResponse(
            String name,
            String symbol
    ) {
    }
}
