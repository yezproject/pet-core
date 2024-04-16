package org.yproject.pet.core.application.category;

import org.yproject.pet.core.domain.category.entities.Category;

public record CategoryDTO(
        String id,
        String name
) {
    public static CategoryDTO fromDomain(Category category) {
        return new CategoryDTO(category.getId().value(), category.getName());
    }
}
