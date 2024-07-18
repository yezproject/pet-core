package org.yezproject.pet.transaction.application.category.driving;

import org.yezproject.pet.transaction.domain.category.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    String save(Category category);

    Optional<Category> retrieveOne(String userId, String categoryId);

    void deleteAll(String userId, List<String> categoryIds);

    List<Category> retrieveAll(String userId);
}
