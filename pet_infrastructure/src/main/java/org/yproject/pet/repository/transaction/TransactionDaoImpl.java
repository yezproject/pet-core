package org.yproject.pet.repository.transaction;

import org.springframework.stereotype.Component;
import org.yproject.pet.transaction.driving.TransactionDao;
import org.yproject.pet.transaction.driving.TransactionDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    public String save(TransactionDto dto) {
        return repository.save(toEntity(dto)).getId();
    }

    @Override
    public Optional<TransactionDto> retrieveOneByIdAndUserId(String transactionId, String userId) {
        return repository.findByIdAndCreatorUserId(transactionId, userId)
                .map(TransactionDaoImpl::fromEntity);
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
