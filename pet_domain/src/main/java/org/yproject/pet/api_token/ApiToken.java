package org.yproject.pet.api_token;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.yproject.pet.common.error.DomainException;
import org.yproject.pet.common.models.AggregateRoot;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class ApiToken extends AggregateRoot<String> {
    public static final int API_TOKEN_NAME_MAX_LENGTH = 100;
    private final String userId;
    private final Instant createDate;
    private String name;

    ApiToken(ApiTokenBuilder builder) {
        super(builder.apiTokenId);
        this.userId = Objects.requireNonNull(builder.userId);
        this.name = nameValidated(builder.name);
        this.createDate = Optional.ofNullable(builder.createDate).orElse(Instant.now());
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

    public void modifyName(String name) {
        this.name = nameValidated(name);
    }

    public String getUserId() {
        return userId;
    }
}
