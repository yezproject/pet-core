package org.yproject.pet.core.domain.user;

import org.junit.jupiter.api.Test;
import org.yproject.pet.core.domain.common.error.DomainModifyException;
import org.yproject.pet.core.domain.user.enums.ApprovalStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.yproject.pet.core.util.UserRandomUtils.randomUser;

class UserTest {

    @Test
    void pending_to_approved() {
        final var pendingUser = randomUser(ApprovalStatus.PENDING);
        pendingUser.approve();
        assertEquals(ApprovalStatus.APPROVED, pendingUser.getApprovalStatus());
        assertNotNull(pendingUser.getApprovedAt());
    }

    @Test
    void pending_to_rejected() {
        final var pendingUser = randomUser(ApprovalStatus.PENDING);
        pendingUser.reject();
        assertEquals(ApprovalStatus.REJECTED, pendingUser.getApprovalStatus());
    }

    @Test
    void approved_to_rejected_user() {
        final var approvedUser = randomUser(ApprovalStatus.APPROVED);
        assertThrows(DomainModifyException.class, approvedUser::reject);
    }

    @Test
    void rejected_to_approved_user() {
        final var rejectedUser = randomUser(ApprovalStatus.REJECTED);
        assertThrows(DomainModifyException.class, rejectedUser::approve);
    }
}
