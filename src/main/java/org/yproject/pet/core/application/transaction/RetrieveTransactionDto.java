package org.yproject.pet.core.application.transaction;

import org.yproject.pet.core.domain.transaction.Currency;
import org.yproject.pet.core.domain.transaction.Transaction;

import java.time.Instant;

public record RetrieveTransactionDto(
        String id,
        String description,
        Double amount,
        Currency currency,
        Instant createTime
) {

    public static RetrieveTransactionDto fromDomain(Transaction domain) {
        return new RetrieveTransactionDto(
                domain.id(),
                domain.description(),
                domain.amount(),
                domain.currency(),
                domain.createTime()
        );
    }

}
