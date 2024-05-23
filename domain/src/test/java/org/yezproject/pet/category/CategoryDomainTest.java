package org.yezproject.pet.category;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.yezproject.pet.RandomUtils.randomShortString;
import static org.yezproject.pet.category.CategoryRandomUtils.randomCategoryBuilder;

class CategoryDomainTest {

    private static final String OVER_100_CHARACTERS_STRING = RandomStringUtils.randomAlphanumeric(101);
    private static final String BLANK_STRING = " ";

    private static Stream<String> invalidNames() {
        return Stream.of(BLANK_STRING, OVER_100_CHARACTERS_STRING);
    }

    @Test
    void testCategoryBuilder() {
        final var categoryId = randomShortString();
        final var userId = randomShortString();
        final var name = randomShortString();
        final var category = randomCategoryBuilder(categoryId)
                .createUserId(userId)
                .name(name)
                .build();

        assertEquals(categoryId, category.getId());
        assertEquals(userId, category.getCreateUserId());
        assertEquals(name, category.getName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidNames")
    void testCategoryBuilderWithInvalidName(String invalidName) {
        final var categoryBuilder = randomCategoryBuilder()
                .name(invalidName);

        assertThrows(Exception.class, categoryBuilder::build);
    }

    @Test
    void testModifyName() {
        final var name = randomShortString();
        final var category = randomCategoryBuilder().build();
        category.modifyName(name);

        assertEquals(name, category.getName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidNames")
    void testModifyNameWithInvalidName(String invalidName) {
        final var category = randomCategoryBuilder()
                .build();

        assertThrows(Exception.class, () -> category.modifyName(invalidName));
    }
}
