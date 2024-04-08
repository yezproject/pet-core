package org.yproject.pet.core.application.transaction;

import org.yproject.pet.core.domain.transaction.entities.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionStorage {

    String save(Transaction domain);

    Optional<Transaction> retrieveOneByIdAndUserId(String transactionId, String userId);

    void deleteByIdsAndUserId(List<String> transactionIds, String userId);

    List<Transaction> retrieveAllByUserId(String userId);
}
