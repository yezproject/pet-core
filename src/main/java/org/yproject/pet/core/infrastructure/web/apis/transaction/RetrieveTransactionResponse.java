package org.yproject.pet.core.infrastructure.web.apis.transaction;

import org.yproject.pet.core.application.transaction.RetrieveTransactionDTO;

record RetrieveTransactionResponse(
        String id,
        String categoryId,
        String description,
        Double amount,
        RetrieveTransactionCurrencyResponse currency,
        Long createTime
) {

    static RetrieveTransactionResponse fromDTO(RetrieveTransactionDTO dto) {
        return new RetrieveTransactionResponse(
                dto.id(),
                dto.categoryId(),
                dto.description(),
                dto.amount(),
                new RetrieveTransactionCurrencyResponse(dto.currency().name(), dto.currency().symbol()),
                dto.createTime().toEpochMilli()
        );
    }

    record RetrieveTransactionCurrencyResponse(
            String name,
            String symbol
    ) {
    }
}
