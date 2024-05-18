package org.yproject.pet.transaction;

import java.time.Instant;

public final class TransactionBuilder {
    final String transactionId;
    String creatorUserId;
    String categoryId;
    String name;
    Double amount;
    Currency currency;
    Instant transactionDate;
    Instant createDate;
    Instant updateDate;

    public TransactionBuilder(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionBuilder categoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public TransactionBuilder name(String name) {
        this.name = name;
        return this;
    }

    public TransactionBuilder amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public TransactionBuilder currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public TransactionBuilder creatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
        return this;
    }

    public TransactionBuilder transactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public TransactionBuilder updateDate(Instant updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public TransactionBuilder createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    Transaction build() {
        return new Transaction(this);
    }
}
