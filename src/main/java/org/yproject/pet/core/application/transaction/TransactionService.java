package org.yproject.pet.core.application.transaction;

import java.util.List;

public interface TransactionService {
    final class TransactionNotExisted extends RuntimeException {
    }

    String create(String userId, String description, Double amount, String currency, Long createTime);

    void modify(String userId, String transactionId, String description, Double amount, String currency, Long createTime);

    void delete(List<String> transactionIds, String userId);

    List<RetrieveTransactionDto> retrieveAll(String userId);

    RetrieveTransactionDto retrieve(String userId, String transactionId);
}
