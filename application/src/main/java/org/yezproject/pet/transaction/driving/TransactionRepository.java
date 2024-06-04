package org.yezproject.pet.transaction.driving;

import org.yezproject.pet.transaction.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TransactionRepository {

    void save(Transaction domain);

    void save(Collection<Transaction> transactions);

    Optional<Transaction> retrieveOneByIdAndUserId(String transactionId, String userId);

    List<Transaction> retrieveAllByIdsAndUserId(Set<String> transactionId, String userId);

    List<Transaction> retrieveAllByUserId(String userId);

    List<Transaction> retrieveAllByUserId(String userId, int limit);
}
