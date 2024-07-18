package org.yezproject.pet.transaction.application.user.driven;

import lombok.experimental.StandardException;

public interface AuthService {
    @StandardException
    final class UserNotFoundException extends Exception {
    }

    AuthInfo loadUserByEmail(String email) throws UserNotFoundException;

    AuthInfo loadUserById(String userId) throws UserNotFoundException;
}
