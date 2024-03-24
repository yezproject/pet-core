package org.yproject.pet.core.domain.category;

import org.yproject.pet.core.domain.exception.DomainCreateException;

import java.util.Objects;

public record UserCategory(
        String id,
        String userId,
        String name
) implements Category {

    public UserCategory {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(userId);

        if (name.isBlank() || name.length() > 100) {
            throw new DomainCreateException("Category name must be from 0 to 100 characters");
        }
    }
}
