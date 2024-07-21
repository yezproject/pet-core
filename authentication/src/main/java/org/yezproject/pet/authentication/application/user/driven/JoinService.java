package org.yezproject.pet.authentication.application.user.driven;

public interface JoinService {

    final class UserNotFoundException extends RuntimeException {
    }

    final class InvalidPasswordException extends RuntimeException {
    }

    final class UserExistedException extends RuntimeException {
    }

    GeneralUserDto signIn(SignInDto signInDto);

    String signup(SignUpDto signUpDto);
}
