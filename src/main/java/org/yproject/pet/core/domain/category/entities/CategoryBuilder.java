package org.yproject.pet.core.domain.category.entities;

import org.yproject.pet.core.domain.category.value_objects.CategoryId;
import org.yproject.pet.core.domain.user.value_objects.UserId;

public class CategoryBuilder {
    final CategoryId categoryId;
    UserId createUserId;
    String name;

    public CategoryBuilder(String categoryId) {
        this.categoryId = new CategoryId(categoryId);
    }

    public CategoryBuilder createUserId(String createUserId) {
        this.createUserId = new UserId(createUserId);
        return this;
    }

    public CategoryBuilder name(String name) {
        this.name = name;
        return this;
    }

    public Category build() {
        return new Category(this);
    }
}
