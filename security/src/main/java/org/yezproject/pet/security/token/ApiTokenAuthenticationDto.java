package org.yezproject.pet.security.token;

public record ApiTokenAuthenticationDto(
        String token,
        Boolean isAuthenticated,
        String userId,
        String fullName,
        String email
) {
}
