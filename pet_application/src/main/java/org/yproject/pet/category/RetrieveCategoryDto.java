package org.yproject.pet.category;

import org.yproject.pet.category.entities.Category;

public record RetrieveCategoryDto(
        String id,
        String name
) {
    public static RetrieveCategoryDto fromDomain(Category category) {
        return new RetrieveCategoryDto(category.getId().value(), category.getName());
    }
}
