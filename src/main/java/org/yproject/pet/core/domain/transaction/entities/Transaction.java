package org.yproject.pet.core.domain.transaction.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.yproject.pet.core.domain.category.value_objects.CategoryId;
import org.yproject.pet.core.domain.common.error.DomainCreateException;
import org.yproject.pet.core.domain.common.models.AggregateRoot;
import org.yproject.pet.core.domain.transaction.enums.Currency;
import org.yproject.pet.core.domain.transaction.enums.TransactionType;
import org.yproject.pet.core.domain.transaction.value_objects.TransactionId;
import org.yproject.pet.core.domain.user.value_objects.UserId;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Transaction extends AggregateRoot<TransactionId> {
    private final UserId creatorUserId;
    private CategoryId categoryId;
    private String description;
    private Double amount;
    private Currency currency;
    private Instant createTime;
    private TransactionType type;

    Transaction(TransactionBuilder builder) {
        super(builder.transactionId);
        categoryId = builder.categoryId;
        description = descriptionValidated(builder.description);
        amount = amountValidated(builder.amount);
        creatorUserId = Objects.requireNonNull(builder.creatorUserId);
        createTime = Optional.ofNullable(builder.createTime).orElse(Instant.now());
        currency = Optional.ofNullable(builder.currency).orElse(Currency.VND);
        transactionClassify();
    }

    public void modifyCategoryId(CategoryId categoryId) {
        this.categoryId = Objects.requireNonNull(categoryId);
    }

    public void modifyDescription(String description) {
        this.description = descriptionValidated(description);
    }

    public void modifyAmount(Double amount) {
        this.amount = amountValidated(amount);
    }

    public void modifyCurrency(Currency currency) {
        this.currency = currency;
    }

    public void modifyCreateTime(Instant createTime) {
        if (createTime != null) {
            this.createTime = createTime;
        }
    }

    private void transactionClassify() {
        if (amount < 0) {
            type = TransactionType.EXPENSE;
        } else {
            type = TransactionType.INCOME;
        }
    }

    private Double amountValidated(final Double amount) {
        Objects.requireNonNull(amount);
        if (amount == 0) throw new DomainCreateException("Transaction amount must not be zero");
        return amount;
    }

    private String descriptionValidated(final String description) {
        Objects.requireNonNull(description);
        if (description.isBlank() || description.length() > 100) {
            throw new DomainCreateException("Description must be from 0 to 100 characters");
        }
        return description;
    }

}
