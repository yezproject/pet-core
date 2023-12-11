package org.yproject.pet.core.domain;

import org.springframework.lang.Nullable;
import org.yproject.pet.core.infrastructure.repository.user.UserStatus;
import org.yproject.pet.core.infrastructure.repository.user.Role;

import java.time.Instant;
import java.util.Objects;

public record User(
        String id,
        String email,
        String password,
        String userName,
        String displayName,
        Role role,
        UserStatus status,
        Instant createAt,
        @Nullable Instant approvedAt
) {

    public User {
        Objects.requireNonNull(id);
        Objects.requireNonNull(email);
        Objects.requireNonNull(userName);
        Objects.requireNonNull(displayName);
        Objects.requireNonNull(role);
        Objects.requireNonNull(status);
        Objects.requireNonNull(createAt);
        Objects.requireNonNull(approvedAt);
        Objects.requireNonNull(password);
    }
}
