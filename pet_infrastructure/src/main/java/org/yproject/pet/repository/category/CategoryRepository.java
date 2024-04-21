package org.yproject.pet.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    Optional<CategoryEntity> findByCreateUserIdAndId(String userId, String categoryId);

    void deleteAllByCreateUserIdAndIdIn(String userId, Set<String> categoryId);

    List<CategoryEntity> findAllByCreateUserId(String userId);
}
