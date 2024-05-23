package org.yezproject.pet.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yezproject.pet.Storage;
import org.yezproject.pet.transaction.driving.TransactionDao;
import org.yezproject.pet.transaction.driving.TransactionDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Storage
class TransactionStorage {
    private final TransactionDao dao;

    void save(Transaction domain) {
        dao.save(toDto(domain));
    }

    void save(Collection<Transaction> transactions) {
        dao.saveAll(transactions.stream().map(this::toDto).toList());
    }

    Optional<Transaction> retrieveOneByIdAndUserId(String transactionId, String userId) {
        return dao.retrieveOneByIdAndUserId(transactionId, userId)
                .map(this::toDomain);
    }

    List<Transaction> retrieveAllByIdsAndUserId(Set<String> transactionIds, String userId) {
        return dao.retrieveAllByIdsAndUserId(transactionIds, userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    List<Transaction> retrieveAllByUserId(String userId) {
        return dao.retrieveAllByUserId(userId)
                .stream().map(this::toDomain)
                .toList();
    }

    private TransactionDto toDto(Transaction domain) {
        return new TransactionDto(
                domain.getId(),
                domain.getName(),
                domain.getAmount(),
                domain.getCurrency().name(),
                domain.getCreatorUserId(),
                domain.getTransactionDate(),
                domain.getCreateDate(),
                domain.getUpdateDate(),
                domain.getType().name(),
                domain.getCategoryId(),
                domain.isDelete(),
                Optional.ofNullable(domain.getDeleteInfo()).map(DeleteInfo::id).orElse(null),
                Optional.ofNullable(domain.getDeleteInfo()).map(DeleteInfo::date).orElse(null),
                Optional.ofNullable(domain.getDeleteInfo()).map(DeleteInfo::reason).orElse(null)
        );
    }

    private Transaction toDomain(TransactionDto dto) {
        return new TransactionBuilder(dto.id())
                .creatorUserId(dto.creatorUserId())
                .categoryId(dto.categoryId())
                .name(dto.name())
                .amount(dto.amount())
                .currency(Currency.valueOf(dto.currency()))
                .transactionDate(dto.transactionDate())
                .createDate(dto.createDate())
                .updateDate(dto.updateDate())
                .deleteId(dto.deleteId())
                .deleteDate(dto.deleteDate())
                .deleteReason(dto.deleteReason())
                .build();
    }
}
