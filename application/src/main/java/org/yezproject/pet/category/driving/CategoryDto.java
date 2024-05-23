package org.yezproject.pet.category.driving;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDto {
    String id;
    String createUserId;
    String name;
}
