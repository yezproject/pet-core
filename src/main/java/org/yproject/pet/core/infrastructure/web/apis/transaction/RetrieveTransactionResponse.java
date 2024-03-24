package org.yproject.pet.core.infrastructure.web.apis.transaction;

import org.yproject.pet.core.application.transaction.RetrieveTransactionDTO;

record RetrieveTransactionResponse(
        String id,
        String description,
        Double amount,
        RetrieveTransactionCurrencyResponse currency,
        Long createTime
) {

    static RetrieveTransactionResponse toResponse(RetrieveTransactionDTO dto) {
        return new RetrieveTransactionResponse(
                dto.id(),
                dto.description(),
                dto.amount(),
                new RetrieveTransactionCurrencyResponse(dto.currency().name(), dto.currency().getSymbol()),
                dto.createTime().toEpochMilli()
        );
    }

    record RetrieveTransactionCurrencyResponse(
            String name,
            String symbol
    ) {
    }
}
