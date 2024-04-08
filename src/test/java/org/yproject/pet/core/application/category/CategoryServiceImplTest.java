package org.yproject.pet.core.application.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yproject.pet.core.domain.category.CategoryRandomUtils;
import org.yproject.pet.core.domain.category.entities.Category;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.yproject.pet.core.domain.category.CategoryRandomUtils.randomCategory;
import static org.yproject.pet.core.util.RandomUtils.randomShortList;
import static org.yproject.pet.core.util.RandomUtils.randomShortString;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private IdGenerator idGenerator;

    @Mock
    private CategoryStorage categoryStorage;

    @InjectMocks
    private CategoryServiceImpl underTest;

    @Test
    void retrieveAll() {
        final var userId = randomShortString();
        final var categories = randomShortList(CategoryRandomUtils::randomCategory);
        when(categoryStorage.retrieveAll(anyString())).thenReturn(categories);

        final var result = underTest.retrieveAll(userId);

        then(categoryStorage).should().retrieveAll(userId);
        assertThat(result).hasSameSizeAs(categories);
    }

    @Test
    void create() {
        final var categoryId = randomShortString();
        final var category = randomCategory(categoryId);
        when(idGenerator.get()).thenReturn(categoryId);
        when(categoryStorage.save(any(Category.class))).thenReturn(categoryId);

        final var result = underTest.create(category.getCreateUserId().value(), category.getName());

        then(categoryStorage).should().save(category);
        assertThat(result).isEqualTo(categoryId);
    }

    @Test
    void modify() {
        final var userId = randomShortString();
        final var categoryId = randomShortString();
        final var category = randomCategory(categoryId);
        when(categoryStorage.retrieveOne(anyString(), anyString())).thenReturn(Optional.of(category));

        underTest.modify(userId, categoryId, "New Test Category");

        then(categoryStorage).should().save(category);
        assertThat(category.getName()).isEqualTo("New Test Category");
    }

    @Test
    void modify_throw_not_existed_exception() {
        final var userId = randomShortString();
        final var categoryId = randomShortString();
        when(categoryStorage.retrieveOne(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(CategoryServiceImpl.CategoryNotExisted.class, () -> underTest.modify(userId, categoryId, "New Test Category"));
    }

    @Test
    void delete() {
        final var userId = randomShortString();
        final var categoryId = randomShortString();

        underTest.delete(userId, categoryId);

        then(categoryStorage).should().deleteAll(userId, Collections.singletonList(categoryId));
    }
}
