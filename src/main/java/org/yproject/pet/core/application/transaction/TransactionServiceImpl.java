package org.yproject.pet.core.application.transaction;

import org.springframework.stereotype.Service;
import org.yproject.pet.core.domain.transaction.Currency;
import org.yproject.pet.core.domain.transaction.Transaction;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
record TransactionServiceImpl(
        IdGenerator idGenerator,
        TransactionStorage transactionStorage
) implements TransactionService {

    @Override
    public String create(String userId, String description, double amount, String currency) {
        return transactionStorage.save(new Transaction(
                idGenerator.get(),
                description,
                BigDecimal.valueOf(amount),
                Currency.valueOf(currency),
                userId,
                Instant.now()
        ));
    }

    @Override
    public void modify(String userId, String transactionId, String description, double amount, String currency) {
        final var transactionOptional = transactionStorage.getByIdAndUserId(transactionId, userId);
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
                oldTransaction.createTime()
        );
        transactionStorage.save(modifedTransaction);
    }

    @Override
    public void delete(List<String> transactionIds, String userId) {
        transactionStorage.deleteByIdsAndUserId(transactionIds, userId);
    }

    @Override
    public List<RetrieveTransactionDto> retrieveAll(String userId) {
        return transactionStorage.retrieveAllByUserId(userId).stream()
                .map(RetrieveTransactionDto::fromDomain)
                .toList();
    }
}
