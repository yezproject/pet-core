package org.yproject.pet.category;

import lombok.EqualsAndHashCode;
import org.yproject.pet.common.error.DomainException;
import org.yproject.pet.common.models.AggregateRoot;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
public final class Category extends AggregateRoot<String> {
    String createUserId;
    String name;

    Category(CategoryBuilder builder) {
        super(builder.categoryId);
        this.createUserId = Objects.requireNonNull(builder.createUserId);
        this.name = nameValidated(builder.name);
    }

    private String nameValidated(String name) {
        Objects.requireNonNull(name);
        if (name.isBlank() || name.length() > 100) {
            throw new DomainException("Category name must be from 0 to 100 characters");
        }
        return name;
    }

    public void modifyName(String name) {
        this.name = nameValidated(name);
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public String getName() {
        return name;
    }
}
