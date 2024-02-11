package org.yproject.pet.core.domain.user;

import lombok.AccessLevel;
import lombok.With;
import org.springframework.lang.Nullable;
import org.yproject.pet.core.domain.exception.DomainModifyException;

import java.time.Instant;
import java.util.Objects;

public record User(
        String id,
        String email,
        String fullName,
        String password,
        Role role,
        @With(AccessLevel.PRIVATE) ApprovalStatus approvalStatus,
        Instant createAt,
        @With(AccessLevel.PRIVATE) @Nullable Instant approvedAt
) {

    public User {
        Objects.requireNonNull(id);
        Objects.requireNonNull(email);
        Objects.requireNonNull(fullName);
        Objects.requireNonNull(role);
        Objects.requireNonNull(approvalStatus);
        Objects.requireNonNull(createAt);
        Objects.requireNonNull(approvedAt);
        Objects.requireNonNull(password);
    }

    public User approve() {
        if (approvalStatus != ApprovalStatus.REJECTED) {
            return this.withApprovalStatus(ApprovalStatus.APPROVED)
                    .withApprovedAt(Instant.now());
        } else {
            throw new DomainModifyException("Invalid user ApprovalStatus");
        }
    }

    public User reject() {
        if (approvalStatus != ApprovalStatus.APPROVED) {
            return this.withApprovalStatus(ApprovalStatus.REJECTED);
        } else {
            throw new DomainModifyException("Invalid user ApprovalStatus");
        }
    }
}
