package org.yproject.pet.transaction.driven;

import java.util.List;

public interface TransactionService {
    final class TransactionNotExisted extends RuntimeException {
    }

    String create(CreateTransactionDto createTransactionDTO);

    void modify(ModifyTransactionDto modifyTransactionDTO);

    void delete(List<String> transactionIds, String userId);

    List<RetrieveTransactionDto> retrieveAll(String userId);

    RetrieveTransactionDto retrieve(String userId, String transactionId);
}
