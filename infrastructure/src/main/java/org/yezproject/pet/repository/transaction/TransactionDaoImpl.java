package org.yezproject.pet.repository.transaction;

import org.springframework.stereotype.Component;
import org.yezproject.pet.transaction.driving.TransactionDao;
import org.yezproject.pet.transaction.driving.TransactionDto;

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
                .createDate(dto.createDate())
                .updateDate(dto.updateDate())
                .type(dto.type())
                .categoryId(dto.categoryId())
                .isDelete(dto.isDelete())
                .deleteInfo(dto.isDelete() ? toDeleteInfoEntity(dto) : null)
                .build();
    }

    private static DeleteInfoEntity toDeleteInfoEntity(TransactionDto dto) {
        final var id = Optional.ofNullable(dto.deleteId()).map(UUID::fromString).orElse(null);
        return new DeleteInfoEntity(id, dto.deleteDate(), dto.deleteReason());
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
                entity.getCategoryId(),
                entity.isDelete(),
                entity.isDelete() ? entity.getDeleteInfo().getId().toString() : null,
                entity.isDelete() ? entity.getDeleteInfo().getDate(): null,
                entity.isDelete() ? entity.getDeleteInfo().getReason() : null
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
        return repository.findByIsDeleteFalseAndIdAndCreatorUserId(transactionId, userId)
                .map(TransactionDaoImpl::fromEntity);
    }

    @Override
    public List<TransactionDto> retrieveAllByIdsAndUserId(Set<String> transactionIds, String userId) {
        return repository.findAllByIsDeleteFalseAndIdInAndCreatorUserId(transactionIds, userId)
                .stream()
                .map(TransactionDaoImpl::fromEntity)
                .toList();
    }

    @Override
    public List<TransactionDto> retrieveAllByUserId(String userId) {
        return repository.findAllByIsDeleteFalseAndCreatorUserId(userId)
                .stream()
                .map(TransactionDaoImpl::fromEntity)
                .toList();
    }

    @Override
    public List<TransactionDto> retrieveAllByUserId(String userId, int limit) {
        return repository.findAllByIsDeleteFalseAndCreatorUserId(userId, limit)
                .stream()
                .map(TransactionDaoImpl::fromEntity)
                .toList();
    }
}
