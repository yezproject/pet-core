package org.yproject.pet.user;

import java.util.Set;

public record AuthInfo(
        String userId,
        String email,
        Set<String> tokens
) {
}
