package org.yezproject.pet.transaction.infrastructure.repository.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.yezproject.pet.transaction.application.transaction.driving.TransactionRepository;
import org.yezproject.pet.transaction.domain.transaction.Currency;
import org.yezproject.pet.transaction.domain.transaction.Transaction;
import org.yezproject.pet.transaction.domain.transaction.TransactionBuilder;

import java.time.Instant;
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
    public List<Transaction> retrieveAll(String userId) {
        return transactionJpaRepository.findAll(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> retrieveLastNonAfter(String userId, int limit) {
        return transactionJpaRepository.findAll(userId, limit)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> retrieveLastWithAfter(String userId, int limit, Instant after) {
        return transactionJpaRepository.findAll(userId, limit, after)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Page<Transaction> retrievePage(final String userId, final PageRequest pageRequest) {
        final var sortPageRequest = pageRequest.withSort(Sort.by("transactionDate").descending());
        return transactionJpaRepository.findAllPageable(userId, sortPageRequest).map(this::toDomain);
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
