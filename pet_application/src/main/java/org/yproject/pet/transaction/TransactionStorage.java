package org.yproject.pet.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yproject.pet.Storage;
import org.yproject.pet.transaction.driving.TransactionDao;
import org.yproject.pet.transaction.driving.TransactionDto;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Storage
class TransactionStorage {
    private final TransactionDao dao;

    String save(Transaction domain) {
        return dao.save(toDto(domain));
    }

    Optional<Transaction> retrieveOneByIdAndUserId(String transactionId, String userId) {
        return dao.retrieveOneByIdAndUserId(transactionId, userId)
                .map(this::toDomain);
    }

    void deleteByIdsAndUserId(List<String> transactionIds, String userId) {
        dao.deleteByIdsAndUserId(transactionIds, userId);
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
                domain.getCategoryId()
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
                .build();
    }
}
