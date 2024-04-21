package org.yproject.pet.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yproject.pet.category.entities.Category;
import org.yproject.pet.category.entities.CategoryBuilder;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryStorage {
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
                .id(domain.getId().value())
                .createUserId(domain.getCreateUserId().value())
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
