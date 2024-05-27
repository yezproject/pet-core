package org.yezproject.pet.transaction.driven;

import java.util.Collection;
import java.util.List;

public interface TransactionService {
    final class TransactionNotExisted extends RuntimeException {
    } 
    
    final class TransactionInvalidModify extends RuntimeException {
    }

    String create(CreateTransactionDto createTransactionDTO);

    void modify(ModifyTransactionDto modifyTransactionDTO);

    void delete(Collection<String> transactionIds, String userId);

    void delete(String transactionId, String userId);

    List<RetrieveTransactionDto> retrieveAll(String userId);

    List<RetrieveTransactionDto> retrieveLast(String userId, int limit);

    RetrieveTransactionDto retrieve(String userId, String transactionId);
}
