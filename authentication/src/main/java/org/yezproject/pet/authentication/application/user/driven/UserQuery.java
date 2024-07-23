package org.yezproject.pet.authentication.application.user.driven;

import lombok.experimental.StandardException;

public interface UserQuery {
    @StandardException
    final class UserNotFoundException extends RuntimeException {
    }

    GeneralUserDto getByEmail(String email) throws UserNotFoundException;

    GeneralUserDto getById(String userId) throws UserNotFoundException;
}
