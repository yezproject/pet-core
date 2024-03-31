package org.yproject.pet.core.util;

import org.yproject.pet.core.domain.user.entities.UserBuilder;
import org.yproject.pet.core.domain.user.enums.ApprovalStatus;
import org.yproject.pet.core.domain.user.enums.Role;
import org.yproject.pet.core.domain.user.entities.User;
import org.yproject.pet.core.domain.user.value_objects.UserId;

import java.time.Instant;

import static org.yproject.pet.core.util.RandomUtils.*;
import static org.yproject.pet.core.util.RandomUtils.randomShortString;

public class UserRandomUtils {

    public static User randomUser() {
        final var approvalStatus = randomApprovalStatus();
        Instant approvalAt = null;
        if (approvalStatus == ApprovalStatus.APPROVED) {
            approvalAt = randomInstant();
        }
        return new UserBuilder(randomShortString())
                .email(randomShortString())
                .fullName(randomShortString())
                .password(randomShortString())
                .role(randomUserRole())
                .approvalStatus(approvalStatus)
                .createAt(randomInstant())
                .approvedAt(approvalAt)
                .build();
    }

    public static Role randomUserRole() {
        return RandomUtils.randomFrom(Role.values());
    }

    public static ApprovalStatus randomApprovalStatus() {
        return RandomUtils.randomFrom(ApprovalStatus.values());
    }

    public static User randomUser(ApprovalStatus approvalStatus) {
        Instant approvalAt = null;
        if (approvalStatus == ApprovalStatus.APPROVED) {
            approvalAt = randomInstant();
        }
        return new UserBuilder(randomShortString())
                .email(randomShortString())
                .fullName(randomShortString())
                .password(randomShortString())
                .role(randomUserRole())
                .approvalStatus(approvalStatus)
                .createAt(randomInstant())
                .approvedAt(approvalAt)
                .build();
    }
}
