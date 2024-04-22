package org.yproject.pet.transaction.driving;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class TransactionDto {
    String id;
    String description;
    Double amount;
    String currency;
    String creatorUserId;
    Instant createTime;
    String type;
    String categoryId;
}
