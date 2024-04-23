package org.yproject.pet.transaction.driven;

import java.util.List;

public interface TransactionService {
    final class TransactionNotExisted extends RuntimeException {
    }

    String create(String userId, CreateTransactionDto createTransactionDTO);

    void modify(String userId, String transactionId, ModifyTransactionDto modifyTransactionDTO);

    void delete(List<String> transactionIds, String userId);

    List<RetrieveTransactionDto> retrieveAll(String userId);

    RetrieveTransactionDto retrieve(String userId, String transactionId);
}
