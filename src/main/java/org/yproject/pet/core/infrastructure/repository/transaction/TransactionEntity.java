package org.yproject.pet.core.infrastructure.repository.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.yproject.pet.core.infrastructure.repository.category.CategoryEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "transactions")
public class TransactionEntity {
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
