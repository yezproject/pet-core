package org.yezproject.pet.transaction.domain.category;

public class CategoryBuilder {
    final String categoryId;
    String createUserId;
    String name;

    public CategoryBuilder(String categoryId) {
        this.categoryId = categoryId;
    }

    public CategoryBuilder createUserId(String createUserId) {
        this.createUserId = createUserId;
        return this;
    }

    public CategoryBuilder name(String name) {
        this.name = name;
        return this;
    }

    public Category build() {
        return new Category(this);
    }
}
