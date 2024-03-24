package org.yproject.pet.core.infrastructure.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    Optional<CategoryEntity> findByUserIdAndId(String userId, String categoryId);

    void deleteAllByUserIdAndIdIn(String userId, Set<String> categoryId);

    List<CategoryEntity> findAllByUserId(String userId);
}
