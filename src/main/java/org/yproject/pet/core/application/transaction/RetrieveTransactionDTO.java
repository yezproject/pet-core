package org.yproject.pet.core.application.transaction;

import org.yproject.pet.core.domain.transaction.Currency;
import org.yproject.pet.core.domain.transaction.Transaction;

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
                domain.id(),
                domain.description(),
                domain.amount(),
                domain.currency(),
                domain.createTime()
        );
    }

}
