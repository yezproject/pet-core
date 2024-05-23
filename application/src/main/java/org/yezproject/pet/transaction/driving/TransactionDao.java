package org.yezproject.pet.transaction.driving;

import org.yezproject.pet.Dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Dao
public interface TransactionDao {

    void save(TransactionDto domain);

    void saveAll(Collection<TransactionDto> transactions);

    Optional<TransactionDto> retrieveOneByIdAndUserId(String transactionId, String userId);

    List<TransactionDto> retrieveAllByIdsAndUserId(Set<String> transactionId, String userId);

    List<TransactionDto> retrieveAllByUserId(String userId);
}
