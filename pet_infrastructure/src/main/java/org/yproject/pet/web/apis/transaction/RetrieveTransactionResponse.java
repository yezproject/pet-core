package org.yproject.pet.web.apis.transaction;

import org.yproject.pet.transaction.RetrieveTransactionDto;

record RetrieveTransactionResponse(
        String id,
        String categoryId,
        String description,
        Double amount,
        RetrieveTransactionCurrencyResponse currency,
        Long createTime
) {

    static RetrieveTransactionResponse fromDTO(RetrieveTransactionDto dto) {
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
