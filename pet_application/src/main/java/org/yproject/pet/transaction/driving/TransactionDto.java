package org.yproject.pet.transaction.driving;


import java.time.Instant;

public record TransactionDto(
        String id,
        String name,
        Double amount,
        String currency,
        String creatorUserId,
        Instant transactionDate,
        Instant createDate,
        Instant updateDate,
        String type,
        String categoryId,
        boolean isDelete,

        String deleteId,
        Instant deleteDate,
        String deleteReason
) {
}
