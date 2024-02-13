package org.yproject.pet.core.util;

import org.yproject.pet.core.domain.user.ApprovalStatus;
import org.yproject.pet.core.domain.user.Role;
import org.yproject.pet.core.domain.user.User;

import java.time.Instant;

import static org.yproject.pet.core.util.RandomUtils.*;

public class UserRandomUtils {

    public static User randomUser() {
        final var approvalStatus = randomApprovalStatus();
        Instant approvalAt = null;
        if (approvalStatus == ApprovalStatus.APPROVED) {
            approvalAt = randomInstant();
        }
        return new User(
                randomShortString(),
                randomShortString(),
                randomShortString(),
                randomLongString(),
                randomUserRole(),
                approvalStatus,
                randomInstant(),
                approvalAt
        );
    }

    public static Role randomUserRole() {
        return RandomUtils.random(Role.values());
    }

    public static ApprovalStatus randomApprovalStatus() {
        return RandomUtils.random(ApprovalStatus.values());
    }

    public static User randomUser(ApprovalStatus approvalStatus) {
        Instant approvalAt = null;
        if (approvalStatus == ApprovalStatus.APPROVED) {
            approvalAt = randomInstant();
        }
        return new User(
                randomShortString(),
                randomShortString(),
                randomShortString(),
                randomLongString(),
                randomUserRole(),
                approvalStatus,
                randomInstant(),
                approvalAt
        );
    }
}
