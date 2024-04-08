package org.yproject.pet.core.application.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yproject.pet.core.domain.category.entities.CategoryBuilder;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;

import java.util.List;

@Component
@RequiredArgsConstructor
class CategoryServiceImpl implements CategoryService {
    private final IdGenerator idGenerator;
    private final CategoryStorage categoryStorage;

    @Override
    public List<CategoryDTO> retrieveAll(String userId) {
        return categoryStorage.retrieveAll(userId).stream()
                .map(category -> new CategoryDTO(category.getId().value(), category.getName()))
                .toList();
    }

    @Override
    public String create(String userId, String name) {
        final var categoryId = idGenerator.get();
        final var category = new CategoryBuilder(categoryId)
                .createUserId(userId)
                .name(name)
                .build();
        return categoryStorage.save(category);
    }

    @Override
    public void modify(String userId, String id, String newName) {
        final var categoryOptional = categoryStorage.retrieveOne(userId, id);
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotExisted();
        }
        final var category = categoryOptional.get();
        category.modifyName(newName);
        categoryStorage.save(category);
    }

    @Override
    public void delete(String userId, String categoryId) {
        categoryStorage.deleteAll(userId, List.of(categoryId));
    }
}
