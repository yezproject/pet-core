package org.yezproject.pet.transaction.application.transaction.driven;

import org.yezproject.pet.transaction.domain.transaction.Transaction;

import java.time.Instant;

public record RetrieveTransactionDto(
        String id,
        String categoryId,
        String name,
        Double amount,
        RetrieveTransactionCurrencyDTO currency,
        Instant transactionDate
) {

    public static RetrieveTransactionDto fromDomain(Transaction domain) {
        return new RetrieveTransactionDto(
                domain.getId(),
                domain.getCategoryId(),
                domain.getName(),
                domain.getAmount(),
                new RetrieveTransactionCurrencyDTO(domain.getCurrency().name(), domain.getCurrency().getSymbol()),
                domain.getTransactionDate()
        );
    }

    public record RetrieveTransactionCurrencyDTO(
            String name,
            String symbol
    ) {
    }
}
