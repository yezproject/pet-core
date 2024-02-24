package org.yproject.pet.core.application.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yproject.pet.core.domain.transaction.Currency;
import org.yproject.pet.core.domain.transaction.Transaction;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
class TransactionServiceImpl implements TransactionService {
    private final IdGenerator idGenerator;
    private final TransactionStorage transactionStorage;

    @Override
    @Transactional
    public String create(String userId, String description, double amount, String currency, long createTime) {
        return transactionStorage.save(new Transaction(
                idGenerator.get(),
                description,
                BigDecimal.valueOf(amount),
                Currency.valueOf(currency),
                userId,
                Instant.ofEpochMilli(createTime)
        ));
    }

    @Override
    @Transactional
    public void modify(
            final String userId,
            final String transactionId,
            final String description,
            final double amount,
            final String currency,
            final long createTime
    ) {
        final var transactionOptional = transactionStorage.retrieveOneByIdAndUserId(transactionId, userId);
        if (transactionOptional.isEmpty()) {
            throw new TransactionNotExisted();
        }
        Transaction oldTransaction = transactionOptional.get();
        Transaction modifedTransaction = new Transaction(
                oldTransaction.id(),
                description,
                BigDecimal.valueOf(amount),
                Currency.valueOf(currency),
                oldTransaction.creatorUserId(),
                Instant.ofEpochMilli(createTime)
        );
        transactionStorage.save(modifedTransaction);
    }

    @Override
    @Transactional
    public void delete(List<String> transactionIds, String userId) {
        transactionStorage.deleteByIdsAndUserId(transactionIds, userId);
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
