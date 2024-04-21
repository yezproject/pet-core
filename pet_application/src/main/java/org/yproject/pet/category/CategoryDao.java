package org.yproject.pet.category;

import java.util.List;
import java.util.Optional;

public interface CategoryDao {
    String save(CategoryDto category);

    Optional<CategoryDto> retrieveOne(String userId, String categoryId);

    void deleteAll(String userId, List<String> categoryIds);

    List<CategoryDto> retrieveAll(String userId);
}
