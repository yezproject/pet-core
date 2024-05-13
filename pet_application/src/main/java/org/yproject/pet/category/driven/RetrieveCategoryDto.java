package org.yproject.pet.category.driven;

import org.yproject.pet.category.entities.Category;

public record RetrieveCategoryDto(
        String id,
        String name
) {
    public static RetrieveCategoryDto fromDomain(Category category) {
        return new RetrieveCategoryDto(category.getId(), category.getName());
    }
}
