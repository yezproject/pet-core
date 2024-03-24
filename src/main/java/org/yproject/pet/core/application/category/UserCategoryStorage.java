package org.yproject.pet.core.application.category;

import org.yproject.pet.core.domain.category.UserCategory;

import java.util.List;
import java.util.Optional;

public interface UserCategoryStorage {
    String save(UserCategory userCategory);

    Optional<UserCategory> retrieveOne(String userId, String categoryId);

    void deleteAll(String userId, List<String> categoryIds);

    List<UserCategory> retrieveAll(String userId);
}
