package org.yproject.pet.core.domain.transaction;

import org.yproject.pet.core.domain.exception.DomainCreateException;

import java.time.Instant;
import java.util.Objects;

public record Transaction(
        String id,
        String description,
        Double amount,
        Currency currency,
        String creatorUserId,
        Instant createTime
) {
    public Transaction {
        Objects.requireNonNull(id);
        Objects.requireNonNull(description);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);
        Objects.requireNonNull(creatorUserId);
        Objects.requireNonNull(createTime);

        if (amount.equals(0d)) {
            throw new DomainCreateException("Transaction amount must not be zero");
        }
        if (description.isEmpty() || description.length() > 100) {
            throw new DomainCreateException("Description must be from 0 to 100 characters");
        }
    }

    public TransactionType type() {
        if (amount < 0) {
            return TransactionType.EXPENSE;
        } else {
            return TransactionType.INCOME;
        }
    }
}
