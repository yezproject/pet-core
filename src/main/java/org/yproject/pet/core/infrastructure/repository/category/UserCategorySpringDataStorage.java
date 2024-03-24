package org.yproject.pet.core.infrastructure.repository.category;

import org.springframework.stereotype.Component;
import org.yproject.pet.core.application.category.UserCategoryStorage;
import org.yproject.pet.core.domain.category.UserCategory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public record UserCategorySpringDataStorage(
        CategoryRepository repository
) implements UserCategoryStorage {
    private static CategoryEntity toEntity(UserCategory domain) {
        return CategoryEntity.builder()
                .id(domain.id())
                .userId(domain.userId())
                .name(domain.name())
                .build();
    }

    private static UserCategory fromEntity(CategoryEntity entity) {
        return new UserCategory(entity.getId(), entity.getUserId(), entity.getName());
    }

    @Override
    public String save(UserCategory userCategory) {
        return repository.save(toEntity(userCategory)).getId();
    }

    @Override
    public Optional<UserCategory> retrieveOne(String userId, String categoryId) {
        return repository.findByUserIdAndId(userId, categoryId)
                .map(UserCategorySpringDataStorage::fromEntity);
    }

    @Override
    public void deleteAll(String userId, List<String> categoryIds) {
        repository.deleteAllByUserIdAndIdIn(userId, new HashSet<>(categoryIds));
    }

    @Override
    public List<UserCategory> retrieveAll(String userId) {
        return repository.findAllByUserId(userId).stream()
                .map(UserCategorySpringDataStorage::fromEntity)
                .toList();
    }
}
