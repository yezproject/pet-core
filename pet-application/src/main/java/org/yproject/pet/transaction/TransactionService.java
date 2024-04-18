package org.yproject.pet.transaction;

import java.util.List;

public interface TransactionService {
    final class TransactionNotExisted extends RuntimeException {
    }

    String create(String userId, CreateTransactionDTO createTransactionDTO);

    void modify(String userId, String transactionId, ModifyTransactionDTO modifyTransactionDTO);

    void delete(List<String> transactionIds, String userId);

    List<RetrieveTransactionDTO> retrieveAll(String userId);

    RetrieveTransactionDTO retrieve(String userId, String transactionId);
}
