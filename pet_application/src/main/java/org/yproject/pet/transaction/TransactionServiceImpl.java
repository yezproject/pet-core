package org.yproject.pet.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.yproject.pet.common.error.DomainException;
import org.yproject.pet.id.IdGenerator;
import org.yproject.pet.transaction.driven.CreateTransactionDto;
import org.yproject.pet.transaction.driven.ModifyTransactionDto;
import org.yproject.pet.transaction.driven.RetrieveTransactionDto;
import org.yproject.pet.transaction.driven.TransactionService;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
class TransactionServiceImpl implements TransactionService {
    private final IdGenerator idGenerator;
    private final TransactionStorage transactionStorage;

    @Override
    public String create(CreateTransactionDto dto) {
        final var newTransactionId = idGenerator.get();
        final var newTransaction = new TransactionBuilder(newTransactionId)
                .creatorUserId(dto.userId())
                .categoryId(dto.categoryId())
                .name(dto.name())
                .amount(dto.amount())
                .transactionDate(Instant.ofEpochMilli(dto.transactionDate()))
                .build();
        transactionStorage.save(newTransaction);
        return newTransactionId;
    }

    @Override
    public void modify(final ModifyTransactionDto dto) {
        final var transactionOptional = transactionStorage.retrieveOneByIdAndUserId(dto.transactionId(), dto.userId());
        if (transactionOptional.isEmpty()) {
            log.info("not existed transaction id={} userId={}", dto.transactionId(), dto.userId());
            throw new TransactionNotExisted();
        }
        Transaction transaction = transactionOptional.get();
        try {
            transaction.modifyCategoryId(dto.categoryId());
            transaction.modifyAmount(dto.amount());
            transaction.modifyName(dto.name());
            transaction.modifyTransactionDate(Instant.ofEpochMilli(dto.transactionDate()));
        } catch (DomainException e) {
            log.info("modify transaction id={} has been deleted", transaction.getId());
            throw new TransactionInvalidModify();
        }
        transactionStorage.save(transaction);
    }

    @Override
    public void delete(Collection<String> transactionIds, String userId) {
        final var transactions = transactionStorage.retrieveAllByIdsAndUserId(new HashSet<>(transactionIds), userId);
        for (final var transaction : transactions) {
            transaction.delete("");
        }
        transactionStorage.save(transactions);
    }

    @Override
    public void delete(String transactionId, String userId) {
        final var transactionOptional = transactionStorage.retrieveOneByIdAndUserId(transactionId, userId);
        if (transactionOptional.isPresent()) {
            final var transaction = transactionOptional.get();
            transaction.delete("");
            transactionStorage.save(transaction);
        }
    }

    @Override
    public List<RetrieveTransactionDto> retrieveAll(String userId) {
        return transactionStorage.retrieveAllByUserId(userId).stream()
                .map(RetrieveTransactionDto::fromDomain)
                .toList();
    }

    @Override
    public RetrieveTransactionDto retrieve(final String userId, final String transactionId) {
        final var transactionOptional = transactionStorage.retrieveOneByIdAndUserId(transactionId, userId);
        if (transactionOptional.isEmpty()) {
            throw new TransactionNotExisted();
        }
        return RetrieveTransactionDto.fromDomain(transactionOptional.get());
    }
}
