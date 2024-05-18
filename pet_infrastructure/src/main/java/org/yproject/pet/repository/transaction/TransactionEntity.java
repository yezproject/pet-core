package org.yproject.pet.repository.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "transactions")
class TransactionEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String creatorUserId;

    @Column(nullable = false)
    private Instant transactionDate;

    @Column(nullable = false)
    private String type;

    private String categoryId;

    @Column(nullable = false)
    private Instant createDate;

    @Column(nullable = false)
    private Instant updateDate;
}
