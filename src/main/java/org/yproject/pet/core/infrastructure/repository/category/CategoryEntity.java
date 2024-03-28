package org.yproject.pet.core.infrastructure.repository.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "categories")
public class CategoryEntity {
    @Id
    private String id;

    @Column
    private String createUserId;

    @Column(nullable = false)
    private String name;
}
