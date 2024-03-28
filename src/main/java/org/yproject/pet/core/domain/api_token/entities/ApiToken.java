package org.yproject.pet.core.domain.api_token.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.yproject.pet.core.domain.api_token.value_objects.ApiTokenId;
import org.yproject.pet.core.domain.common.error.DomainCreateException;
import org.yproject.pet.core.domain.common.models.AggregateRoot;
import org.yproject.pet.core.domain.user.value_objects.UserId;

import java.time.Instant;
import java.util.Optional;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ApiToken extends AggregateRoot<ApiTokenId> {
    private final UserId userId;
    private final Instant createTime;
    private String name;

    ApiToken(ApiTokenBuilder builder) {
        super(builder.apiTokenId);
        this.userId = Optional.ofNullable(builder.userId)
                .orElseThrow(() -> new DomainCreateException("user id is null"));
        this.name = nameValidated(builder.name);
        this.createTime = Instant.now();
    }

    private String nameValidated(String name) {
        if (name == null) {
            throw new DomainCreateException("name is null");
        } else if (name.isBlank()) {
            throw new DomainCreateException("name is blank");
        } else if (name.length() > 100) {
            throw new DomainCreateException("name is greater than 100 characters");
        } else {
            return name;
        }
    }

    public void modifyName(String name) {
        this.name = nameValidated(name);
    }
}
