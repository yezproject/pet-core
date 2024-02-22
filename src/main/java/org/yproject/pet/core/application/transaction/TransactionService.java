package org.yproject.pet.core.application.transaction;

import java.util.List;

public interface TransactionService {
    String create(String userId, String description, double amount, String currency, long createTime);

    void modify(String userId, String transactionId, String description, double amount, String currency, long createTime);

    void delete(List<String> transactionIds, String userId);

    List<RetrieveTransactionDto> retrieveAll(String userId);

    final class TransactionNotExisted extends RuntimeException {}
}
