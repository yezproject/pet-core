package org.yproject.pet.core.domain.user;

import org.junit.jupiter.api.Test;
import org.yproject.pet.core.domain.exception.DomainModifyException;

import static org.junit.jupiter.api.Assertions.*;
import static org.yproject.pet.core.util.UserRandomUtils.randomUser;

class UserTest {

    @Test
    void pending_to_approved() {
        final var pendingUser = randomUser(ApprovalStatus.PENDING);
        final var approvedUser = pendingUser.approve();
        assertEquals(ApprovalStatus.APPROVED, approvedUser.approvalStatus());
        assertNotNull(approvedUser.approvedAt());
    }

    @Test
    void pending_to_rejected() {
        final var pendingUser = randomUser(ApprovalStatus.PENDING);
        final var approvedUser = pendingUser.reject();
        assertEquals(ApprovalStatus.REJECTED, approvedUser.approvalStatus());
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
