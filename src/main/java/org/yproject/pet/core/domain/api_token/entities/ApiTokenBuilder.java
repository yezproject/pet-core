package org.yproject.pet.core.domain.api_token.entities;

import org.yproject.pet.core.domain.api_token.value_objects.ApiTokenId;
import org.yproject.pet.core.domain.user.value_objects.UserId;

public class ApiTokenBuilder {
    final ApiTokenId apiTokenId;
    UserId userId;
    String name;

    public ApiTokenBuilder(String apiTokenId) {
        this.apiTokenId = new ApiTokenId(apiTokenId);
    }

    public ApiTokenBuilder userId(String userId) {
        this.userId = new UserId(userId);
        return this;
    }

    public ApiTokenBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ApiToken build() {
        return new ApiToken(this);
    }
}
