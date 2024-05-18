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
    private DeleteInfo deleteInfo;

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
        if (builder.deleteDate != null && builder.deleteReason != null) {
            deleteInfo = new DeleteInfo(builder.deleteDate, builder.deleteReason);
        }
        transactionClassify();
    }

    public void modifyCategoryId(String categoryId) {
        preModify();
        this.categoryId = categoryId;
        postModify();
    }

    public void modifyName(String name) {
        preModify();
        this.name = nameValidated(name);
        postModify();
    }

    public void modifyAmount(Double amount) {
        modify(() -> {
            this.amount = amountValidated(amount);
            transactionClassify();
        });
    }

    public void modifyCurrency(Currency currency) {
        modify(() -> this.currency = Objects.requireNonNull(currency));
    }

    public void modifyTransactionDate(Instant transactionDate) {
        modify(() -> this.transactionDate = Objects.requireNonNull(transactionDate));
    }

    private void transactionClassify() {
        type = amount < 0 ? TransactionType.EXPENSE : TransactionType.INCOME;
    }

    private void postModify() {
        this.updateDate = Instant.now();
    }

    private void preModify() {
        if (this.isDelete()) throw new DomainException("Transaction already be deleted");
    }

    private void modify(Runnable modification) {
        preModify();
        modification.run();
        postModify();
    }

    public void delete(String deleteReason) {
        deleteInfo = new DeleteInfo(
                Instant.now(),
                deleteReason
        );
    }

    private Double amountValidated(final double amount) {
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

    /* Getter */

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public TransactionType getType() {
        return type;
    }

    public boolean isDelete() {
        return deleteInfo != null;
    }
}
