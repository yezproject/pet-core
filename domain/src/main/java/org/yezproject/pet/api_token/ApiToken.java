package org.yezproject.pet.api_token;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.yezproject.pet.common.error.DomainException;
import org.yezproject.pet.common.models.AggregateRoot;

import java.util.Objects;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class ApiToken extends AggregateRoot<String> {
    public static final int API_TOKEN_NAME_MAX_LENGTH = 100;
    private final String userId;
    private String name;
    private final String token;

    ApiToken(ApiTokenBuilder builder) {
        super(builder.apiTokenId);
        this.userId = Objects.requireNonNull(builder.userId);
        this.name = nameValidated(builder.name);
        this.token = Objects.requireNonNull(builder.token);
    }

    private String nameValidated(String name) {
        if (name == null) {
            throw new DomainException("name is null");
        } else if (name.isBlank()) {
            throw new DomainException("name is blank");
        } else if (name.length() > API_TOKEN_NAME_MAX_LENGTH) {
            throw new DomainException("name is greater than 100 characters");
        } else {
            return name;
        }
    }

    public String getUserId() {
        return userId;
    }
}
