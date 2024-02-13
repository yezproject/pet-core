package org.yproject.pet.core.application.join;

public interface JoinService {

    String signIn(String email, String password);

    String signup(SignUpApplicationDto signUpApplicationDto);

    final class UserNotFoundException extends RuntimeException {
    }

    final class InvalidPasswordException extends RuntimeException {
    }

    final class UserExistedException extends RuntimeException {
    }
}
