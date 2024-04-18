package org.yproject.pet.category;

import org.yproject.pet.category.entities.Category;

public record CategoryDTO(
        String id,
        String name
) {
    public static CategoryDTO fromDomain(Category category) {
        return new CategoryDTO(category.getId().value(), category.getName());
    }
}
