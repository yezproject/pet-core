package org.yezproject.pet.transaction.application.transaction.driving;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.yezproject.pet.transaction.domain.transaction.Transaction;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TransactionRepository {

    void save(Transaction domain);

    void save(Collection<Transaction> transactions);

    Optional<Transaction> retrieveOneByIdAndUserId(String transactionId, String userId);

    List<Transaction> retrieveAllByIdsAndUserId(Set<String> transactionId, String userId);

    List<Transaction> retrieveAll(String userId);

    List<Transaction> retrieveLastNonAfter(String userId, int limit);

    List<Transaction> retrieveLastWithAfter(String userId, int limit, Instant after);

    Page<Transaction> retrievePage(String userId, PageRequest pageRequest);
}
