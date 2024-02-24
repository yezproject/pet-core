package org.yproject.pet.core.infrastructure.web.apis.transaction;

import org.yproject.pet.core.application.transaction.RetrieveTransactionDto;

record RetrieveTransactionResponse(
        String id,
        String description,
        double amount,
        RetrieveTransactionCurrencyResponse currency,
        long createTime
) {

    record RetrieveTransactionCurrencyResponse(
            String name,
            String symbol
    ) {
    }

    static RetrieveTransactionResponse toResponse(RetrieveTransactionDto dto) {
        return new RetrieveTransactionResponse(
                dto.id(),
                dto.description(),
                dto.amount().doubleValue(),
                new RetrieveTransactionCurrencyResponse(dto.currency().name(), dto.currency().getSymbol()),
                dto.createTime().toEpochMilli()
        );
    }
}
