package org.yproject.pet.transaction.driven;

import org.yproject.pet.transaction.entities.Transaction;

import java.time.Instant;

public record RetrieveTransactionDto(
        String id,
        String categoryId,
        String description,
        Double amount,
        RetrieveTransactionCurrencyDTO currency,
        Instant createTime
) {

    public static RetrieveTransactionDto fromDomain(Transaction domain) {
        return new RetrieveTransactionDto(
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
