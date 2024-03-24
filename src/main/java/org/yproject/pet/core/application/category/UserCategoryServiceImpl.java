package org.yproject.pet.core.application.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yproject.pet.core.domain.category.UserCategory;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;

import java.util.List;

@Component
@RequiredArgsConstructor
class UserCategoryServiceImpl implements UserCategoryService {
    private final IdGenerator idGenerator;
    private final UserCategoryStorage userCategoryStorage;

    @Override
    public List<UserCategoryDTO> retrieveAll(String userId) {
        return userCategoryStorage.retrieveAll(userId).stream()
                .map(domain -> new UserCategoryDTO(domain.name()))
                .toList();
    }

    @Override
    public String create(String userId, String name) {
        final var categoryId = idGenerator.get();
        return userCategoryStorage.save(new UserCategory(
                categoryId,
                userId,
                name
        ));
    }

    @Override
    public void modify(String userId, String id, String modifiedName) {
        final var categoryOptional = userCategoryStorage.retrieveOne(userId, id);
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotExisted();
        }
        final var oldCategory = categoryOptional.get();
        final var modifiedCategory = new UserCategory(
                oldCategory.id(),
                oldCategory.userId(),
                modifiedName
        );
        userCategoryStorage.save(modifiedCategory);
    }

    @Override
    public void delete(String userId, String categoryId) {
        userCategoryStorage.deleteAll(userId, List.of(categoryId));
    }
}
