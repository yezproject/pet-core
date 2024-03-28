package org.yproject.pet.core.domain.api_token.entities;

import lombok.RequiredArgsConstructor;
import org.yproject.pet.core.domain.api_token.value_objects.ApiTokenId;
import org.yproject.pet.core.domain.user.value_objects.UserId;

@RequiredArgsConstructor
public class ApiTokenBuilder {
    final ApiTokenId apiTokenId;
    UserId userId;
    String name;

    public ApiTokenBuilder userId(UserId userId) {
        this.userId = userId;
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
