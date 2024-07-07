package org.yezproject.pet.transaction.driven;

import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface TransactionService {
    final class TransactionNotExistedException extends RuntimeException {
    }

    final class TransactionInvalidModifyException extends RuntimeException {
    }

    final class TransactionQueryParamInvalidException extends RuntimeException {
    }

    String create(CreateTransactionDto createTransactionDTO);

    void modify(ModifyTransactionDto modifyTransactionDTO);

    void delete(Collection<String> transactionIds, String userId);

    void delete(String transactionId, String userId);

    List<RetrieveTransactionDto> retrieveAll(String userId);

    List<RetrieveTransactionDto> retrieveLast(String userId, int limit, Long createDate);

    Page<RetrieveTransactionDto> retrievePage(String userId, int page, int size);

    RetrieveTransactionDto retrieve(String userId, String transactionId);
}
