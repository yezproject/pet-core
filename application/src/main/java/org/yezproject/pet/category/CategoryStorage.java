package org.yezproject.pet.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yezproject.pet.Storage;
import org.yezproject.pet.category.driving.CategoryDao;
import org.yezproject.pet.category.driving.CategoryDto;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Storage
class CategoryStorage {
    private final CategoryDao dao;

    String save(Category category) {
        return dao.save(fromDomain(category));
    }

    Optional<Category> retrieveOne(String userId, String categoryId) {
        return dao.retrieveOne(userId, categoryId)
                .map(this::toDomain);
    }

    void deleteAll(String userId, List<String> categoryIds) {
        dao.deleteAll(userId, categoryIds);
    }

    List<Category> retrieveAll(String userId) {
        return dao.retrieveAll(userId)
                .stream().map(this::toDomain)
                .toList();
    }

    private CategoryDto fromDomain(Category domain) {
        return CategoryDto.builder()
                .id(domain.getId())
                .createUserId(domain.getCreateUserId())
                .name(domain.getName())
                .build();
    }

    private Category toDomain(CategoryDto dto) {
        return new CategoryBuilder(dto.getId())
                .createUserId(dto.getCreateUserId())
                .name(dto.getName())
                .build();
    }
}
