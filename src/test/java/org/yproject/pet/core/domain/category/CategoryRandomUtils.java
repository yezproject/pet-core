package org.yproject.pet.core.domain.category;

import org.yproject.pet.core.domain.category.entities.Category;
import org.yproject.pet.core.domain.category.entities.CategoryBuilder;

import static org.yproject.pet.core.util.RandomUtils.randomShortString;

public class CategoryRandomUtils {
    public static CategoryBuilder randomCategoryBuilder() {
        return new CategoryBuilder(randomShortString())
                .name(randomShortString())
                .createUserId(randomShortString());
    }

    public static CategoryBuilder randomCategoryBuilder(String categoryId) {
        return new CategoryBuilder(categoryId)
                .name(randomShortString())
                .createUserId(randomShortString());
    }

    public static Category randomCategory() {
        return randomCategoryBuilder().build();
    }

    public static Category randomCategory(String id) {
        return randomCategoryBuilder(id).build();
    }
}
