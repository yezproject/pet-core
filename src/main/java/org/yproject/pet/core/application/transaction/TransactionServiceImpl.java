package org.yproject.pet.core.application.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yproject.pet.core.domain.transaction.Currency;
import org.yproject.pet.core.domain.transaction.Transaction;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
class TransactionServiceImpl implements TransactionService {
    private final IdGenerator idGenerator;
    private final TransactionStorage transactionStorage;

    @Override
    public String create(String userId, CreateTransactionDTO createTransactionDTO) {
        return transactionStorage.save(new Transaction(
                idGenerator.get(),
                createTransactionDTO.categoryId(),
                createTransactionDTO.description(),
                createTransactionDTO.amount(),
                Currency.valueOf(createTransactionDTO.currency()),
                userId,
                Instant.ofEpochMilli(createTransactionDTO.createTime())
        ));
    }

    @Override
    public void modify(
            final String userId,
            final String transactionId,
            final ModifyTransactionDTO modifyTransactionDTO
    ) {
        final var transactionOptional = transactionStorage.retrieveOneByIdAndUserId(transactionId, userId);
        if (transactionOptional.isEmpty()) {
            throw new TransactionNotExisted();
        }
        Transaction oldTransaction = transactionOptional.get();
        Transaction modifedTransaction = new Transaction(
                oldTransaction.id(),
                modifyTransactionDTO.categoryId(),
                modifyTransactionDTO.description(),
                modifyTransactionDTO.amount(),
                Currency.valueOf(modifyTransactionDTO.currency()),
                oldTransaction.creatorUserId(),
                Instant.ofEpochMilli(modifyTransactionDTO.createTime())
        );
        transactionStorage.save(modifedTransaction);
    }

    @Override
    public void delete(List<String> transactionIds, String userId) {
        transactionStorage.deleteByIdsAndUserId(transactionIds, userId);
    }

    @Override
    public List<RetrieveTransactionDTO> retrieveAll(String userId) {
        return transactionStorage.retrieveAllByUserId(userId).stream()
                .map(RetrieveTransactionDTO::fromDomain)
                .toList();
    }

    @Override
    public RetrieveTransactionDTO retrieve(final String userId, final String transactionId) {
        final var transactionOptional = transactionStorage.retrieveOneByIdAndUserId(transactionId, userId);
        if (transactionOptional.isEmpty()) {
            throw new TransactionNotExisted();
        }
        return RetrieveTransactionDTO.fromDomain(transactionOptional.get());
    }
}
