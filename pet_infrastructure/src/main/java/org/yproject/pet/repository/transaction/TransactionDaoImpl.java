package org.yproject.pet.repository.transaction;

import org.springframework.stereotype.Component;
import org.yproject.pet.transaction.TransactionDao;
import org.yproject.pet.transaction.TransactionDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
record TransactionDaoImpl(
        TransactionRepository repository
) implements TransactionDao {

    private static TransactionEntity toEntity(TransactionDto dto) {
        return TransactionEntity.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .creatorUserId(dto.getCreatorUserId())
                .createTime(dto.getCreateTime())
                .type(dto.getType())
                .categoryId(dto.getCategoryId())
                .build();
    }

    private static TransactionDto fromEntity(TransactionEntity entity) {
        return TransactionDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .creatorUserId(entity.getCreatorUserId())
                .createTime(entity.getCreateTime())
                .type(entity.getType())
                .categoryId(entity.getCategoryId())
                .build();
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
