package org.yezproject.pet.user.driven;

import java.util.Set;

public record AuthInfo(
        String userId,
        String email,
        Set<String> tokens
) {
}
