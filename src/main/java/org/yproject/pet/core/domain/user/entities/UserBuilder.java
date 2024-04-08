package org.yproject.pet.core.domain.user.entities;

import org.yproject.pet.core.domain.user.enums.ApprovalStatus;
import org.yproject.pet.core.domain.user.enums.Role;
import org.yproject.pet.core.domain.user.value_objects.UserId;

import java.time.Instant;

public class UserBuilder {
    final UserId userId;
    String email;
    String fullName;
    String password;
    Role role;
    ApprovalStatus approvalStatus;
    Instant createAt;
    Instant approvedAt;

    public UserBuilder(String userId) {
        this.userId = new UserId(userId);
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder role(Role role) {
        this.role = role;
        return this;
    }

    public UserBuilder approvalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
        return this;
    }

    public UserBuilder createAt(Instant createAt) {
        this.createAt = createAt;
        return this;
    }

    public UserBuilder approvedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
        return this;
    }

    public User build() {
        return new User(this);
    }
}
