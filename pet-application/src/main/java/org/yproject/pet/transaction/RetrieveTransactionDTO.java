package org.yproject.pet.transaction;

import org.yproject.pet.transaction.entities.Transaction;

import java.time.Instant;

public record RetrieveTransactionDTO(
        String id,
        String categoryId,
        String description,
        Double amount,
        RetrieveTransactionCurrencyDTO currency,
        Instant createTime
) {

    public static RetrieveTransactionDTO fromDomain(Transaction domain) {
        return new RetrieveTransactionDTO(
                domain.getId().value(),
                domain.getCategoryId() != null ? domain.getCategoryId().value() : null,
                domain.getDescription(),
                domain.getAmount(),
                new RetrieveTransactionCurrencyDTO(domain.getCurrency().name(), domain.getCurrency().getSymbol()),
                domain.getCreateTime()
        );
    }

    public record RetrieveTransactionCurrencyDTO(
            String name,
            String symbol
    ) {
    }
}
