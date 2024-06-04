package org.yezproject.pet.repository.transaction;

import org.springframework.stereotype.Component;
import org.yezproject.pet.transaction.Currency;
import org.yezproject.pet.transaction.Transaction;
import org.yezproject.pet.transaction.TransactionBuilder;
import org.yezproject.pet.transaction.driving.TransactionRepository;

import java.util.*;

@Component
record TransactionRepositoryImpl(
        TransactionJpaRepository transactionJpaRepository
) implements TransactionRepository {

    @Override
    public void save(Transaction domain) {
        transactionJpaRepository.save(toEntity(domain));
    }

    @Override
    public void save(Collection<Transaction> transactions) {
        transactionJpaRepository.saveAll(transactions.stream().map(this::toEntity).toList());
    }

    @Override
    public Optional<Transaction> retrieveOneByIdAndUserId(String transactionId, String userId) {
        return transactionJpaRepository.findByIsDeleteFalseAndIdAndCreatorUserId(transactionId, userId)
                .map(this::toDomain);
    }

    @Override
    public List<Transaction> retrieveAllByIdsAndUserId(Set<String> transactionIds, String userId) {
        return transactionJpaRepository.findAllByIsDeleteFalseAndIdInAndCreatorUserId(transactionIds, userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> retrieveAllByUserId(String userId) {
        return transactionJpaRepository.findAllByIsDeleteFalseAndCreatorUserId(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> retrieveAllByUserId(String userId, int limit) {
        return transactionJpaRepository.findAllByIsDeleteFalseAndCreatorUserId(userId, limit)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private TransactionEntity toEntity(Transaction domain) {
        return TransactionEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .amount(domain.getAmount())
                .currency(domain.getCurrency().name())
                .creatorUserId(domain.getCreatorUserId())
                .transactionDate(domain.getTransactionDate())
                .createDate(domain.getCreateDate())
                .updateDate(domain.getUpdateDate())
                .type(domain.getType().name())
                .categoryId(domain.getCategoryId())
                .isDelete(domain.isDelete())
                .deleteInfo(domain.isDelete() ? toDeleteInfoEntity(domain) : null)
                .build();
    }

    private Transaction toDomain(TransactionEntity entity) {
        return new TransactionBuilder(entity.getId())
                .creatorUserId(entity.getCreatorUserId())
                .categoryId(entity.getCategoryId())
                .name(entity.getName())
                .amount(entity.getAmount())
                .currency(Currency.valueOf(entity.getCurrency()))
                .transactionDate(entity.getTransactionDate())
                .createDate(entity.getCreateDate())
                .updateDate(entity.getUpdateDate())
                .deleteId(Optional.ofNullable(entity.getDeleteInfo()).map(it -> it.getId().toString()).orElse(null))
                .deleteDate(Optional.ofNullable(entity.getDeleteInfo()).map(DeleteInfoEntity::getDate).orElse(null))
                .deleteReason(Optional.ofNullable(entity.getDeleteInfo()).map(DeleteInfoEntity::getReason).orElse(null))
                .build();
    }

    private DeleteInfoEntity toDeleteInfoEntity(Transaction domain) {
        final var id = Optional.ofNullable(domain.getDeleteInfo().id()).map(UUID::fromString).orElse(null);
        return new DeleteInfoEntity(id, domain.getDeleteInfo().date(), domain.getDeleteInfo().reason());
    }
}