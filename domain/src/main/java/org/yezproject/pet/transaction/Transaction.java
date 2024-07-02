package org.yezproject.pet.transaction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.yezproject.pet.common.error.DomainException;
import org.yezproject.pet.common.models.AggregateRoot;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
public final class Transaction extends AggregateRoot<String> implements Deletable {
    private static final int NAME_MAX_LENGTH = 100;
    private static final Currency DEFAULT_CURRENCY = Currency.VND;

    private final @Getter String creatorUserId;
    private final @Getter Instant createDate;
    private @Getter String categoryId;
    private @Getter String name;
    private @Getter double amount;
    private @Getter Currency currency;
    private @Getter Instant transactionDate;
    private @Getter Instant updateDate;
    private @Getter TransactionType type;
    private @Getter DeleteInfo deleteInfo;

    Transaction(TransactionBuilder builder) {
        super(builder.transactionId);
        categoryId = builder.categoryId;
        name = nameValidated(builder.name);
        amount = Objects.requireNonNull(builder.amount);
        creatorUserId = Objects.requireNonNull(builder.creatorUserId);
        transactionDate = Optional.ofNullable(builder.transactionDate).orElse(Instant.now());
        currency = Optional.ofNullable(builder.currency).orElse(DEFAULT_CURRENCY);
        createDate = Optional.ofNullable(builder.createDate).orElse(Instant.now());
        updateDate = Optional.ofNullable(builder.updateDate).orElse(Instant.now());
        if (builder.deleteDate != null && builder.deleteReason != null) {
            deleteInfo = new DeleteInfo(builder.deleteId, builder.deleteDate, builder.deleteReason);
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
            this.amount = Objects.requireNonNull(amount);
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

    private String nameValidated(final String name) {
        Objects.requireNonNull(name);
        if (name.isBlank() || name.length() > NAME_MAX_LENGTH) {
            throw new DomainException("Name must be from 0 to %d characters"
                    .formatted(NAME_MAX_LENGTH));
        }
        return name;
    }

    @Override
    public void delete(String deleteReason) {
        if (deleteInfo == null) {
            deleteInfo = new DeleteInfo(
                    null,
                    Instant.now(),
                    deleteReason
            );
        }
    }

    @Override
    public boolean isDelete() {
        return deleteInfo != null;
    }
}
