package org.yezproject.pet.category.driven;

import org.yezproject.pet.category.Category;

public record RetrieveCategoryDto(
        String id,
        String name
) {
    public static RetrieveCategoryDto fromDomain(Category category) {
        return new RetrieveCategoryDto(category.getId(), category.getName());
    }
}
