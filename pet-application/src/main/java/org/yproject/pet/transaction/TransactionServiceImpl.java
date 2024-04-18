package org.yproject.pet.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yproject.pet.category.value_objects.CategoryId;
import org.yproject.pet.id.IdGenerator;
import org.yproject.pet.transaction.entities.Transaction;
import org.yproject.pet.transaction.entities.TransactionBuilder;
import org.yproject.pet.transaction.enums.Currency;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
class TransactionServiceImpl implements TransactionService {
    private final IdGenerator idGenerator;
    private final TransactionStorage transactionStorage;

    @Override
    public String create(String userId, CreateTransactionDTO createTransactionDTO) {
        final var newTransactionId = idGenerator.get();
        final var newTransaction = new TransactionBuilder(newTransactionId)
                .creatorUserId(userId)
                .categoryId(createTransactionDTO.categoryId())
                .description(createTransactionDTO.description())
                .amount(createTransactionDTO.amount())
                .currency(Currency.valueOf(createTransactionDTO.currency()))
                .createTime(Instant.ofEpochMilli(createTransactionDTO.createTime()))
                .build();
        return transactionStorage.save(newTransaction);
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
        Transaction transaction = transactionOptional.get();
        if (modifyTransactionDTO.categoryId() != null) {
            transaction.modifyCategoryId(new CategoryId(modifyTransactionDTO.categoryId()));
        }
        transaction.modifyAmount(modifyTransactionDTO.amount());
        transaction.modifyDescription(modifyTransactionDTO.description());
        transaction.modifyCurrency(Currency.valueOf(modifyTransactionDTO.currency()));
        transaction.modifyCreateTime(Instant.ofEpochMilli(modifyTransactionDTO.createTime()));
        transactionStorage.save(transaction);
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
