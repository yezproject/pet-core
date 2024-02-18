package org.yproject.pet.core.infrastructure.web.apis.transaction;

import org.yproject.pet.core.application.transaction.RetrieveTransactionDto;

record RetrieveTransactionResponse(
        String id,
        String description,
        double amount,
        String currency,
        long createTime
) {

    static RetrieveTransactionResponse toResponse(RetrieveTransactionDto dto) {
        return new RetrieveTransactionResponse(
                dto.id(),
                dto.description(),
                dto.amount().doubleValue(),
                dto.currency().name(),
                dto.createTime().toEpochMilli()
        );
    }
}
