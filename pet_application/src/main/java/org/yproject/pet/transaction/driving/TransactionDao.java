package org.yproject.pet.transaction.driving;

import org.yproject.pet.Dao;

import java.util.List;
import java.util.Optional;

@Dao
public interface TransactionDao {

    String save(TransactionDto domain);

    Optional<TransactionDto> retrieveOneByIdAndUserId(String transactionId, String userId);

    void deleteByIdsAndUserId(List<String> transactionIds, String userId);

    List<TransactionDto> retrieveAllByUserId(String userId);
}
