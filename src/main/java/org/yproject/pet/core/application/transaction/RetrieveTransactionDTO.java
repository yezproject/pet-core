package org.yproject.pet.core.application.transaction;

import org.yproject.pet.core.domain.transaction.entities.Transaction;
import org.yproject.pet.core.domain.transaction.enums.Currency;

import java.time.Instant;

public record RetrieveTransactionDTO(
        String id,
        String description,
        Double amount,
        Currency currency,
        Instant createTime
) {

    public static RetrieveTransactionDTO fromDomain(Transaction domain) {
        return new RetrieveTransactionDTO(
                domain.getId().value(),
                domain.getDescription(),
                domain.getAmount(),
                domain.getCurrency(),
                domain.getCreateTime()
        );
    }
}
