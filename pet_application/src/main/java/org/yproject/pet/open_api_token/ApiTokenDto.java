package org.yproject.pet.open_api_token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiTokenDto {
    private String id;
    private String userId;
    private String name;
}
