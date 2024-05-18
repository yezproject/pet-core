package org.yproject.pet.repository.transaction;

import org.springframework.stereotype.Component;
import org.yproject.pet.transaction.driving.TransactionDao;
import org.yproject.pet.transaction.driving.TransactionDto;

import java.util.*;

@Component
record TransactionDaoImpl(
        TransactionRepository repository
) implements TransactionDao {

    private static TransactionEntity toEntity(TransactionDto dto) {
        return TransactionEntity.builder()
                .id(dto.id())
                .name(dto.name())
                .amount(dto.amount())
                .currency(dto.currency())
                .creatorUserId(dto.creatorUserId())
                .transactionDate(dto.transactionDate())
                .type(dto.type())
                .categoryId(dto.categoryId())
                .build();
    }

    private static TransactionDto fromEntity(TransactionEntity entity) {
        return new TransactionDto(
                entity.getId(),
                entity.getName(),
                entity.getAmount(),
                entity.getCurrency(),
                entity.getCreatorUserId(),
                entity.getTransactionDate(),
                entity.getCreateDate(),
                entity.getUpdateDate(),
                entity.getType(),
                entity.getCategoryId()
        );
    }

    @Override
    public void save(TransactionDto dto) {
        repository.save(toEntity(dto));
    }

    @Override
    public void saveAll(Collection<TransactionDto> transactions) {
        repository.saveAll(transactions.stream().map(TransactionDaoImpl::toEntity).toList());
    }

    @Override
    public Optional<TransactionDto> retrieveOneByIdAndUserId(String transactionId, String userId) {
        return repository.findByIdAndCreatorUserId(transactionId, userId)
                .map(TransactionDaoImpl::fromEntity);
    }

    @Override
    public List<TransactionDto> retrieveAllByIdsAndUserId(Set<String> transactionIds, String userId) {
        return repository.findAllByIdInAndCreatorUserId(transactionIds, userId)
                .stream()
                .map(TransactionDaoImpl::fromEntity)
                .toList();
    }

    @Override
    public void deleteByIdsAndUserId(List<String> transactionIds, String userId) {
        repository.deleteAllByIdInAndCreatorUserId(new HashSet<>(transactionIds), userId);
    }

    @Override
    public List<TransactionDto> retrieveAllByUserId(String userId) {
        return repository.findAllByCreatorUserId(userId).stream()
                .map(TransactionDaoImpl::fromEntity)
                .toList();
    }
}
