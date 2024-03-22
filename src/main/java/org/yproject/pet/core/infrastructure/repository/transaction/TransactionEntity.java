package org.yproject.pet.core.infrastructure.repository.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private String creatorId;

    @Column(nullable = false)
    private Long createTime;

    @Column(nullable = false)
    private String type;

}
