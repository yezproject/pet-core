package org.yezproject.pet.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yezproject.pet.category.driving.CategoryRepository;
import org.yezproject.pet.id.IdGenerator;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.yezproject.pet.RandomUtils.randomShortList;
import static org.yezproject.pet.RandomUtils.randomShortString;
import static org.yezproject.pet.category.CategoryRandomUtils.randomCategory;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private IdGenerator idGenerator;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl underTest;

    @Test
    void retrieveAll() {
        final var userId = randomShortString();
        final var categories = randomShortList(CategoryRandomUtils::randomCategory);
        when(categoryRepository.retrieveAll(anyString())).thenReturn(categories);

        final var result = underTest.retrieveAll(userId);

        then(categoryRepository).should().retrieveAll(userId);
        assertThat(result).hasSameSizeAs(categories);
    }

    @Test
    void create() {
        final var categoryId = randomShortString();
        final var category = randomCategory(categoryId);
        when(idGenerator.get()).thenReturn(categoryId);
        when(categoryRepository.save(any(Category.class))).thenReturn(categoryId);

        final var result = underTest.create(category.getCreateUserId(), category.getName());

        then(categoryRepository).should().save(category);
        assertThat(result).isEqualTo(categoryId);
    }

    @Test
    void modify() {
        final var userId = randomShortString();
        final var categoryId = randomShortString();
        final var category = randomCategory(categoryId);
        when(categoryRepository.retrieveOne(anyString(), anyString())).thenReturn(Optional.of(category));

        underTest.modify(userId, categoryId, "New Test Category");

        then(categoryRepository).should().save(category);
        assertThat(category.getName()).isEqualTo("New Test Category");
    }

    @Test
    void modify_throw_not_existed_exception() {
        final var userId = randomShortString();
        final var categoryId = randomShortString();
        when(categoryRepository.retrieveOne(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(CategoryServiceImpl.CategoryNotExisted.class, () -> underTest.modify(userId, categoryId, "New Test Category"));
    }

    @Test
    void delete() {
        final var userId = randomShortString();
        final var categoryId = randomShortString();

        underTest.delete(userId, categoryId);

        then(categoryRepository).should().deleteAll(userId, Collections.singletonList(categoryId));
    }
}
