package org.yproject.pet.web.apis.utils;

import org.yproject.pet.RandomUtils;
import org.yproject.pet.user.entities.User;
import org.yproject.pet.user.entities.UserBuilder;
import org.yproject.pet.user.enums.ApprovalStatus;
import org.yproject.pet.user.enums.Role;

import java.time.Instant;

import static org.yproject.pet.RandomUtils.randomInstant;
import static org.yproject.pet.RandomUtils.randomShortString;

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
