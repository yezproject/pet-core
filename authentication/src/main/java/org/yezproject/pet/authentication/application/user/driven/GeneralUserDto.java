package org.yezproject.pet.authentication.application.user.driven;

public record GeneralUserDto(
        String userId,
        String email,
        String fullName
) {
}
