package org.yproject.pet.category;

import org.yproject.pet.category.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryStorage {
    String save(Category category);

    Optional<Category> retrieveOne(String userId, String categoryId);

    void deleteAll(String userId, List<String> categoryIds);

    List<Category> retrieveAll(String userId);
}
