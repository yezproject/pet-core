package org.yezproject.pet.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yezproject.pet.category.driven.CategoryService;
import org.yezproject.pet.category.driven.RetrieveCategoryDto;
import org.yezproject.pet.category.driving.CategoryRepository;
import org.yezproject.pet.id.IdGenerator;

import java.util.List;

@Component
@RequiredArgsConstructor
class CategoryServiceImpl implements CategoryService {
    private final IdGenerator idGenerator;
    private final CategoryRepository categoryRepository;

    @Override
    public List<RetrieveCategoryDto> retrieveAll(String userId) {
        return categoryRepository.retrieveAll(userId).stream()
                .map(RetrieveCategoryDto::fromDomain)
                .toList();
    }

    @Override
    public String create(String userId, String name) {
        final var categoryId = idGenerator.get();
        final var category = new CategoryBuilder(categoryId)
                .createUserId(userId)
                .name(name)
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public void modify(String userId, String id, String newName) {
        final var categoryOptional = categoryRepository.retrieveOne(userId, id);
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotExisted();
        }
        final var category = categoryOptional.get();
        category.modifyName(newName);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(String userId, String categoryId) {
        categoryRepository.deleteAll(userId, List.of(categoryId));
    }
}
