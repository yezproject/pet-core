package org.yproject.pet.core.application.join;

public interface JoinService {

    String signIn(String email, String password) throws UserNotFoundException, InvalidPasswordException;

    String signup(SignUpApplicationDto signUpApplicationDto) throws UserExistedException;

    final class UserNotFoundException extends Exception {
    }

    final class InvalidPasswordException extends Exception {
    }

    final class UserExistedException extends Exception {
    }
}
