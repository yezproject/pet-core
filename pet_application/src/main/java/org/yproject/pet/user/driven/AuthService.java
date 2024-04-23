package org.yproject.pet.user.driven;

import lombok.experimental.StandardException;

public interface AuthService {
    @StandardException
    final class UserNotFoundException extends Exception {
    }

    AuthInfo loadUserByEmail(String email) throws UserNotFoundException;
}
