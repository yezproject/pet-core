package org.yproject.pet.core.infrastructure.repository.category;

import org.springframework.stereotype.Component;
import org.yproject.pet.core.application.category.CategoryStorage;
import org.yproject.pet.core.domain.category.entities.Category;
import org.yproject.pet.core.domain.category.entities.CategoryBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public record CategorySpringDataStorage(
        CategoryRepository repository
) implements CategoryStorage {
    private static CategoryEntity toEntity(Category domain) {
        return CategoryEntity.builder()
                .id(domain.getId().value())
                .createUserId(domain.getCreateUserId().value())
                .name(domain.getName())
                .build();
    }

    private static Category fromEntity(CategoryEntity entity) {
        return new CategoryBuilder(entity.getId())
                .createUserId(entity.getCreateUserId())
                .name(entity.getName())
                .build();
    }

    @Override
    public String save(Category category) {
        return repository.save(toEntity(category)).getId();
    }

    @Override
    public Optional<Category> retrieveOne(String userId, String categoryId) {
        return repository.findByUserIdAndId(userId, categoryId)
                .map(CategorySpringDataStorage::fromEntity);
    }

    @Override
    public void deleteAll(String userId, List<String> categoryIds) {
        repository.deleteAllByUserIdAndIdIn(userId, new HashSet<>(categoryIds));
    }

    @Override
    public List<Category> retrieveAll(String userId) {
        return repository.findAllByUserId(userId).stream()
                .map(CategorySpringDataStorage::fromEntity)
                .toList();
    }
}
