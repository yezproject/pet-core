package org.yproject.pet.transaction.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.yproject.pet.category.value_objects.CategoryId;
import org.yproject.pet.common.error.DomainException;
import org.yproject.pet.common.models.AggregateRoot;
import org.yproject.pet.transaction.enums.Currency;
import org.yproject.pet.transaction.enums.TransactionType;
import org.yproject.pet.transaction.value_objects.TransactionId;
import org.yproject.pet.user.value_objects.UserId;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Transaction extends AggregateRoot<TransactionId> {
    private static final int DESCRIPTION_MAX_LENGTH = 100;
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
        transactionClassify();
    }

    public void modifyCurrency(Currency currency) {
        this.currency = Objects.requireNonNull(currency);
    }

    public void modifyCreateTime(Instant createTime) {
        this.createTime = Objects.requireNonNull(createTime);
    }

    private void transactionClassify() {
        type = amount < 0 ? TransactionType.EXPENSE : TransactionType.INCOME;
    }

    private Double amountValidated(final Double amount) {
        Objects.requireNonNull(amount);
        if (amount == 0) throw new DomainException("Transaction amount must not be zero");
        return amount;
    }

    private String descriptionValidated(final String description) {
        Objects.requireNonNull(description);
        if (description.isBlank() || description.length() > DESCRIPTION_MAX_LENGTH) {
            throw new DomainException("Description must be from 0 to %d characters"
                    .formatted(DESCRIPTION_MAX_LENGTH));
        }
        return description;
    }

}
