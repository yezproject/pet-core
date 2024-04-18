package org.yproject.pet.repository.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "categories")
class CategoryEntity {
    @Id
    private String id;

    @Column
    private String createUserId;

    @Column(nullable = false)
    private String name;
}
