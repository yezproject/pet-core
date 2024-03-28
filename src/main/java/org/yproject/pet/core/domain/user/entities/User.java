package org.yproject.pet.core.domain.user.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.yproject.pet.core.domain.common.error.DomainModifyException;
import org.yproject.pet.core.domain.common.models.AggregateRoot;
import org.yproject.pet.core.domain.user.enums.ApprovalStatus;
import org.yproject.pet.core.domain.user.enums.Role;
import org.yproject.pet.core.domain.user.value_objects.UserId;

import java.time.Instant;
import java.util.Optional;

@Getter
@EqualsAndHashCode(callSuper = true)
public class User extends AggregateRoot<UserId> {
    String email;
    String fullName;
    String password;
    Role role;
    ApprovalStatus approvalStatus;
    Instant createAt;
    Instant approvedAt;

    User(UserBuilder userBuilder) {
        super(userBuilder.userId);
        this.email = userBuilder.email;
        this.fullName = userBuilder.fullName;
        this.password = userBuilder.password;
        this.role = userBuilder.role;
        this.approvalStatus = userBuilder.approvalStatus;
        this.approvedAt = userBuilder.approvedAt;
        this.createAt = Optional.ofNullable(userBuilder.createAt).orElse(Instant.now());
    }

    public void approve() {
        if (approvalStatus != ApprovalStatus.REJECTED) {
            this.approvalStatus = ApprovalStatus.APPROVED;
            this.approvedAt = Instant.now();
        } else {
            throw new DomainModifyException("Invalid user ApprovalStatus");
        }
    }

    public void reject() {
        if (approvalStatus != ApprovalStatus.APPROVED) {
            this.approvalStatus = ApprovalStatus.REJECTED;
        } else {
            throw new DomainModifyException("Invalid user ApprovalStatus");
        }
    }
}
