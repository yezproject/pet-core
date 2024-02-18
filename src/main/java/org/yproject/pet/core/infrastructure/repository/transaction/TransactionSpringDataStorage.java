package org.yproject.pet.core.infrastructure.repository.transaction;

import org.springframework.stereotype.Repository;
import org.yproject.pet.core.application.transaction.TransactionStorage;
import org.yproject.pet.core.domain.transaction.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
record TransactionSpringDataStorage(
        TransactionRepository repository
) implements TransactionStorage {

    @Override
    public String save(Transaction domain) {
        return repository.save(TransactionEntityMapper.toEntity(domain)).getId();
    }

    @Override
    public Optional<Transaction> getByIdAndUserId(String transactionId, String userId) {
        return repository.findByIdAndCreatorId(transactionId, userId)
                .map(TransactionEntityMapper::fromEntity);
    }

    @Override
    public void deleteByIdsAndUserId(List<String> transactionIds, String userId) {
        repository.deleteAllByIdInAndCreatorId(new HashSet<>(transactionIds), userId);
    }

    @Override
    public List<Transaction> retrieveAllByUserId(String userId) {
        return repository.findAllByCreatorId(userId).stream()
                .map(TransactionEntityMapper::fromEntity)
                .toList();
    }
}
