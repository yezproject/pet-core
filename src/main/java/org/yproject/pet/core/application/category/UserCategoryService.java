package org.yproject.pet.core.application.category;

import java.util.List;

public interface UserCategoryService {
    final class CategoryNotExisted extends RuntimeException {
    }

    List<UserCategoryDTO> retrieveAll(String userId);

    String create(String userId, String name);

    void modify(String userId, String id, String modifiedName);

    void delete(String userId, String categoryId);
}
