package org.yezproject.pet.user.driven;

public interface JoinService {

    final class UserNotFoundException extends RuntimeException {
    }

    final class InvalidPasswordException extends RuntimeException {
    }

    final class UserExistedException extends RuntimeException {
    }

    String signIn(String email, String password);

    String signup(SignUpApplicationDto signUpApplicationDto);
}