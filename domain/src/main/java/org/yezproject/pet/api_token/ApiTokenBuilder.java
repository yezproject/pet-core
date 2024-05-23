package org.yezproject.pet.api_token;

import java.time.Instant;

public class ApiTokenBuilder {
    final String apiTokenId;
    String userId;
    String name;
    Instant createDate;

    public ApiTokenBuilder(String apiTokenId) {
        this.apiTokenId = apiTokenId;
    }

    public ApiTokenBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public ApiTokenBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ApiTokenBuilder createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public ApiToken build() {
        return new ApiToken(this);
    }
}
