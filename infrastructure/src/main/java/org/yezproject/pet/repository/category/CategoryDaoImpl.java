package org.yezproject.pet.repository.category;

import org.springframework.stereotype.Component;
import org.yezproject.pet.category.driving.CategoryDao;
import org.yezproject.pet.category.driving.CategoryDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
record CategoryDaoImpl(
        CategoryRepository repository
) implements CategoryDao {

    @Override
    public String save(CategoryDto category) {
        return repository.save(toEntity(category)).getId();
    }

    @Override
    public Optional<CategoryDto> retrieveOne(String userId, String categoryId) {
        return repository.findByCreateUserIdAndId(userId, categoryId)
                .map(this::fromEntity);
    }

    @Override
    public void deleteAll(String userId, List<String> categoryIds) {
        repository.deleteAllByCreateUserIdAndIdIn(userId, new HashSet<>(categoryIds));
    }

    @Override
    public List<CategoryDto> retrieveAll(String userId) {
        return repository.findAllByCreateUserId(userId).stream()
                .map(this::fromEntity)
                .toList();
    }

    private CategoryEntity toEntity(CategoryDto domain) {
        return CategoryEntity.builder()
                .id(domain.getId())
                .createUserId(domain.getCreateUserId())
                .name(domain.getName())
                .build();
    }

    private CategoryDto fromEntity(CategoryEntity entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .createUserId(entity.getCreateUserId())
                .name(entity.getName())
                .build();
    }

}
