package org.yproject.pet.category.driving;

import org.yproject.pet.Dao;

import java.util.List;
import java.util.Optional;

@Dao
public interface CategoryDao {
    String save(CategoryDto category);

    Optional<CategoryDto> retrieveOne(String userId, String categoryId);

    void deleteAll(String userId, List<String> categoryIds);

    List<CategoryDto> retrieveAll(String userId);
}
