package org.yezproject.pet.transaction.application.category.driven;

import org.yezproject.pet.transaction.domain.category.Category;

public record RetrieveCategoryDto(
        String id,
        String name
) {
    public static RetrieveCategoryDto fromDomain(Category category) {
        return new RetrieveCategoryDto(category.getId(), category.getName());
    }
}
