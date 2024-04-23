package org.yproject.pet.repository.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "transactions")
class TransactionEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String creatorUserId;

    @Column(nullable = false)
    private Instant createTime;

    @Column(nullable = false)
    private String type;

    private String categoryId;
}
