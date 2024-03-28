package org.yproject.pet.core.domain.transaction.entities;

import lombok.RequiredArgsConstructor;
import org.yproject.pet.core.domain.category.value_objects.CategoryId;
import org.yproject.pet.core.domain.transaction.enums.Currency;
import org.yproject.pet.core.domain.transaction.value_objects.TransactionId;
import org.yproject.pet.core.domain.user.value_objects.UserId;

import java.time.Instant;

@RequiredArgsConstructor
public final class TransactionBuilder {
    final TransactionId transactionId;
    UserId creatorUserId;
    CategoryId categoryId;
    String description;
    Double amount;
    Currency currency;
    Instant createTime;

    public TransactionBuilder categoryId(CategoryId categoryId) {
        this.categoryId = categoryId;
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

    public TransactionBuilder creatorUserId(UserId creatorUserId) {
        this.creatorUserId = creatorUserId;
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
