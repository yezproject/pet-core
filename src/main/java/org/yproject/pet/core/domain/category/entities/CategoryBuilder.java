package org.yproject.pet.core.domain.category.entities;

import lombok.RequiredArgsConstructor;
import org.yproject.pet.core.domain.category.value_objects.CategoryId;
import org.yproject.pet.core.domain.user.value_objects.UserId;

@RequiredArgsConstructor
public class CategoryBuilder {
    final CategoryId categoryId;
    UserId createUserId;
    String name;

    public CategoryBuilder createUserId(UserId createUserId) {
        this.createUserId = createUserId;
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
