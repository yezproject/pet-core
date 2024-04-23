package org.yproject.pet.transaction.entities;

import org.yproject.pet.category.value_objects.CategoryId;
import org.yproject.pet.transaction.enums.Currency;
import org.yproject.pet.transaction.value_objects.TransactionId;
import org.yproject.pet.user.value_objects.UserId;

import java.time.Instant;
import java.util.Optional;

public final class TransactionBuilder {
    final TransactionId transactionId;
    UserId creatorUserId;
    CategoryId categoryId;
    String description;
    Double amount;
    Currency currency;
    Instant createTime;

    public TransactionBuilder(String transactionId) {
        this.transactionId = new TransactionId(transactionId);
    }

    public TransactionBuilder categoryId(String categoryId) {
        this.categoryId = Optional.ofNullable(categoryId).map(CategoryId::new).orElse(null);
        return this;
    }

    public TransactionBuilder description(String description) {
        this.description = description;
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
        this.creatorUserId = new UserId(creatorUserId);
        return this;
    }

    public TransactionBuilder createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public Transaction build() {
        return new Transaction(this);
    }
}
