package org.yproject.pet.core.domain.category.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.yproject.pet.core.domain.category.value_objects.CategoryId;
import org.yproject.pet.core.domain.common.error.DomainCreateException;
import org.yproject.pet.core.domain.common.models.AggregateRoot;
import org.yproject.pet.core.domain.user.value_objects.UserId;

import java.util.Objects;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Category extends AggregateRoot<CategoryId> {
    UserId createUserId;
    String name;

    Category(CategoryBuilder builder) {
        super(builder.categoryId);
        this.createUserId = Objects.requireNonNull(builder.createUserId);
        this.name = nameValidated(builder.name);
    }

    private String nameValidated(String name) {
        Objects.requireNonNull(name);
        if (name.isBlank() || name.length() > 100) {
            throw new DomainCreateException("Category name must be from 0 to 100 characters");
        }
        return name;
    }

    public void modifyName(String name) {
        this.name = nameValidated(name);
    }
}
