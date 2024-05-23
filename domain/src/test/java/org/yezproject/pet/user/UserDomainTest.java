package org.yezproject.pet.user;

import org.junit.jupiter.api.Test;
import org.yezproject.pet.common.error.DomainException;

import static org.junit.jupiter.api.Assertions.*;
import static org.yezproject.pet.user.UserRandomUtils.randomUser;

class UserDomainTest {

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
        assertThrows(DomainException.class, approvedUser::reject);
    }

    @Test
    void rejected_to_approved_user() {
        final var rejectedUser = randomUser(ApprovalStatus.REJECTED);
        assertThrows(DomainException.class, rejectedUser::approve);
    }
}
