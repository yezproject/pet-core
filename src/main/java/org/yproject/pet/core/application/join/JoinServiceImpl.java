package org.yproject.pet.core.application.join;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yproject.pet.core.application.user.UserStorage;
import org.yproject.pet.core.configuration.GenerateId;
import org.yproject.pet.core.configuration.jwt.JwtService;
import org.yproject.pet.core.domain.User;
import org.yproject.pet.core.infrastructure.repository.user.Role;
import org.yproject.pet.core.infrastructure.repository.user.UserStatus;

import java.time.Instant;

@Component
public record JoinServiceImpl(
        UserStorage userStorage,
        JwtService jwtService,
        PasswordEncoder passwordEncoder,
        GenerateId generateId
) implements JoinService {

    @Override
    public String signIn(String email, String password) throws UserNotFoundException, InvalidPasswordException {
        final var isUser = userStorage.findByEmail(email);
        if (isUser.isEmpty()) throw new UserNotFoundException();

        final var isPasswordValid = passwordEncoder.matches(password, isUser.get().password());
        if (!isPasswordValid) throw new InvalidPasswordException();

        return jwtService.generateToken(email);
    }

    @Override
    public String signup(SignUpApplicationDto signUpApplicationDto) throws UserExistedException {
        final var isUser = userStorage.findByEmail(signUpApplicationDto.fullName());
        if (isUser.isPresent()) throw new UserExistedException();
        final var id = generateId.nextId();
        final var password = passwordEncoder.encode(signUpApplicationDto.password());
       final var newUser = new User(
               id,
               signUpApplicationDto.email(),
               signUpApplicationDto.fullName(),
               password,
               Role.USER,
               UserStatus.APPROVED,
               Instant.now(),
               Instant.now()
       );
       return userStorage.store(newUser);
    }
}
