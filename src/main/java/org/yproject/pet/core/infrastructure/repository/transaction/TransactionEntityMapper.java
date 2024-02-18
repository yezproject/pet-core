package org.yproject.pet.core.infrastructure.repository.transaction;

import org.yproject.pet.core.domain.transaction.Currency;
import org.yproject.pet.core.domain.transaction.Transaction;

import java.math.BigDecimal;
import java.time.Instant;

record TransactionEntityMapper() {
    public static TransactionEntity toEntity(Transaction domain) {
        return new TransactionEntity(
                domain.id(),
                domain.description(),
                domain.amount().doubleValue(),
                domain.currency().name(),
                domain.creatorUserId(),
                domain.createTime().toEpochMilli(),
                domain.type().name()
        );
    }

    public static Transaction fromEntity(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getDescription(),
                BigDecimal.valueOf(entity.getAmount()),
                Currency.valueOf(entity.getCurrency()),
                entity.getCreatorId(),
                Instant.ofEpochMilli(entity.getCreateTime())
        );
    }
}
