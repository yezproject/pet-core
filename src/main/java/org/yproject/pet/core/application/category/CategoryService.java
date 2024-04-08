package org.yproject.pet.core.application.category;

import java.util.List;

public interface CategoryService {
    final class CategoryNotExisted extends RuntimeException {
    }

    List<CategoryDTO> retrieveAll(String userId);

    String create(String userId, String name);

    void modify(String userId, String id, String modifiedName);

    void delete(String userId, String categoryId);
}
