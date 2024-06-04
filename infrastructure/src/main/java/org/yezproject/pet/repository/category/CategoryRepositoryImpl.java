package org.yezproject.pet.repository.category;

import org.springframework.stereotype.Component;
import org.yezproject.pet.category.Category;
import org.yezproject.pet.category.CategoryBuilder;
import org.yezproject.pet.category.driving.CategoryRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
record CategoryRepositoryImpl(
        org.yezproject.pet.repository.category.CategoryRepository repository
) implements CategoryRepository {

    @Override
    public String save(Category category) {
        return repository.save(toEntity(category)).getId();
    }

    @Override
    public Optional<Category> retrieveOne(String userId, String categoryId) {
        return repository.findByCreateUserIdAndId(userId, categoryId)
                .map(this::toDomain);
    }

    @Override
    public void deleteAll(String userId, List<String> categoryIds) {
        repository.deleteAllByCreateUserIdAndIdIn(userId, new HashSet<>(categoryIds));
    }

    @Override
    public List<Category> retrieveAll(String userId) {
        return repository.findAllByCreateUserId(userId).stream()
                .map(this::toDomain)
                .toList();
    }

    private CategoryEntity toEntity(Category domain) {
        return CategoryEntity.builder()
                .id(domain.getId())
                .createUserId(domain.getCreateUserId())
                .name(domain.getName())
                .build();
    }

    private Category toDomain(CategoryEntity entity) {
        return new CategoryBuilder(entity.getId())
                .createUserId(entity.getCreateUserId())
                .name(entity.getName())
                .build();
    }

}
