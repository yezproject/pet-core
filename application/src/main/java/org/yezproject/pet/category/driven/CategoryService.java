package org.yezproject.pet.category.driven;

import java.util.List;

public interface CategoryService {
    final class CategoryNotExisted extends RuntimeException {
    }

    List<RetrieveCategoryDto> retrieveAll(String userId);

    String create(String userId, String name);

    void modify(String userId, String id, String modifiedName);

    void delete(String userId, String categoryId);
}
