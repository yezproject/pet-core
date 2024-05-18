package org.yproject.pet.transaction;

import lombok.EqualsAndHashCode;
import org.yproject.pet.common.error.DomainException;
import org.yproject.pet.common.models.AggregateRoot;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
public final class Transaction extends AggregateRoot<String> {
    private static final int NAME_MAX_LENGTH = 100;
    private static final Currency DEFAULT_CURRENCY = Currency.VND;
    private final String creatorUserId;
    private final Instant createDate;
    private String categoryId;
    private String name;
    private double amount;
    private Currency currency;
    private Instant transactionDate;
    private Instant updateDate;
    private TransactionType type;

    Transaction(TransactionBuilder builder) {
        super(builder.transactionId);
        categoryId = builder.categoryId;
        name = nameValidated(builder.name);
        amount = amountValidated(builder.amount);
        creatorUserId = Objects.requireNonNull(builder.creatorUserId);
        transactionDate = Optional.ofNullable(builder.transactionDate).orElse(Instant.now());
        currency = Optional.ofNullable(builder.currency).orElse(DEFAULT_CURRENCY);
        createDate = Optional.ofNullable(builder.createDate).orElse(Instant.now());
        updateDate = Optional.ofNullable(builder.updateDate).orElse(Instant.now());
        transactionClassify();
    }

    public void modifyCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void modifyName(String name) {
        this.name = nameValidated(name);
        postModifyAction();
    }

    public void modifyAmount(Double amount) {
        this.amount = amountValidated(amount);
        transactionClassify();
        postModifyAction();
    }

    public void modifyCurrency(Currency currency) {
        this.currency = Objects.requireNonNull(currency);
        postModifyAction();
    }

    public void modifyTransactionDate(Instant transactionDate) {
        this.transactionDate = Objects.requireNonNull(transactionDate);
        postModifyAction();
    }

    private void transactionClassify() {
        type = amount < 0 ? TransactionType.EXPENSE : TransactionType.INCOME;
    }

    private void postModifyAction() {
        this.updateDate = Instant.now();
    }

    private Double amountValidated(final Double amount) {
        Objects.requireNonNull(amount);
        if (amount == 0) throw new DomainException("Transaction amount must not be zero");
        return amount;
    }

    private String nameValidated(final String name) {
        Objects.requireNonNull(name);
        if (name.isBlank() || name.length() > NAME_MAX_LENGTH) {
            throw new DomainException("Name must be from 0 to %d characters"
                    .formatted(NAME_MAX_LENGTH));
        }
        return name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getName() {
        return name;
    }

    public Instant getCreateDate() {
        return createDate;
    }
}
