package org.yezproject.pet.api_token.driving;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiTokenDto {
    private String id;
    private String userId;
    private String name;
}
