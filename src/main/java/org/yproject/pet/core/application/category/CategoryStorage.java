package org.yproject.pet.core.application.category;

import org.yproject.pet.core.domain.category.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryStorage {
    String save(Category category);

    Optional<Category> retrieveOne(String userId, String categoryId);

    void deleteAll(String userId, List<String> categoryIds);

    List<Category> retrieveAll(String userId);
}
