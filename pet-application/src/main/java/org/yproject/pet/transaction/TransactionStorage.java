package org.yproject.pet.transaction;

import org.yproject.pet.transaction.entities.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionStorage {

    String save(Transaction domain);

    Optional<Transaction> retrieveOneByIdAndUserId(String transactionId, String userId);

    void deleteByIdsAndUserId(List<String> transactionIds, String userId);

    List<Transaction> retrieveAllByUserId(String userId);
}
