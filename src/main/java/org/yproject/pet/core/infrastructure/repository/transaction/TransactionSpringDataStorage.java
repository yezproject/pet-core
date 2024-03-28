package org.yproject.pet.core.infrastructure.repository.transaction;

import org.springframework.stereotype.Component;
import org.yproject.pet.core.application.transaction.TransactionStorage;
import org.yproject.pet.core.domain.category.value_objects.CategoryId;
import org.yproject.pet.core.domain.transaction.entities.Transaction;
import org.yproject.pet.core.domain.transaction.entities.TransactionBuilder;
import org.yproject.pet.core.domain.transaction.enums.Currency;
import org.yproject.pet.core.domain.transaction.value_objects.TransactionId;
import org.yproject.pet.core.domain.user.value_objects.UserId;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
record TransactionSpringDataStorage(
        TransactionRepository repository
) implements TransactionStorage {

    private static TransactionEntity toEntity(Transaction domain) {
        return TransactionEntity.builder()
                .id(domain.getId().value())
                .description(domain.getDescription())
                .amount(domain.getAmount())
                .currency(domain.getCurrency().name())
                .creatorUserId(domain.getCreatorUserId().value())
                .createTime(domain.getCreateTime())
                .type(domain.getType().name())
                .categoryId(domain.getCategoryId().value())
                .build();
    }

    private static Transaction fromEntity(TransactionEntity entity) {
        return new TransactionBuilder(new TransactionId(entity.getId()))
                .creatorUserId(new UserId(entity.getCreatorUserId()))
                .categoryId(new CategoryId(entity.getCategoryId()))
                .description(entity.getDescription())
                .amount(entity.getAmount())
                .currency(Currency.valueOf(entity.getCurrency()))
                .createTime(entity.getCreateTime())
                .build();
    }

    @Override
    public String save(Transaction domain) {
        return repository.save(toEntity(domain)).getId();
    }

    @Override
    public Optional<Transaction> retrieveOneByIdAndUserId(String transactionId, String userId) {
        return repository.findByIdAndCreatorId(transactionId, userId)
                .map(TransactionSpringDataStorage::fromEntity);
    }

    @Override
    public void deleteByIdsAndUserId(List<String> transactionIds, String userId) {
        repository.deleteAllByIdInAndCreatorId(new HashSet<>(transactionIds), userId);
    }

    @Override
    public List<Transaction> retrieveAllByUserId(String userId) {
        return repository.findAllByCreatorId(userId).stream()
                .map(TransactionSpringDataStorage::fromEntity)
                .toList();
    }
}
