package org.yezproject.pet.authentication.domain.user;

import org.yezproject.pet.test_common.RandomUtils;

import java.time.Instant;

import static org.yezproject.pet.test_common.RandomUtils.randomInstant;
import static org.yezproject.pet.test_common.RandomUtils.randomShortString;

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
