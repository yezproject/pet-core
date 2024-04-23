package org.yproject.pet.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yproject.pet.Storage;
import org.yproject.pet.transaction.driving.TransactionDao;
import org.yproject.pet.transaction.driving.TransactionDto;
import org.yproject.pet.transaction.entities.Transaction;
import org.yproject.pet.transaction.entities.TransactionBuilder;
import org.yproject.pet.transaction.enums.Currency;

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
        return TransactionDto.builder()
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

    private Transaction toDomain(TransactionDto dto) {
        return new TransactionBuilder(dto.getId())
                .creatorUserId(dto.getCreatorUserId())
                .categoryId(dto.getCategoryId())
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .currency(Currency.valueOf(dto.getCurrency()))
                .createTime(dto.getCreateTime())
                .build();
    }
}
