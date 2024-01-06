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
        final var existingUserOptional = userStorage.findByEmail(email);
        if (existingUserOptional.isEmpty()) throw new UserNotFoundException();

        final var isPasswordValid = passwordEncoder.matches(password, existingUserOptional.get().password());
        if (!isPasswordValid) throw new InvalidPasswordException();

        return jwtService.generateToken(email);
    }

    @Override
    public String signup(SignUpApplicationDto signUpApplicationDto) throws UserExistedException {
        final var existingUserOptional = userStorage.findByEmail(signUpApplicationDto.fullName());
        if (existingUserOptional.isPresent()) throw new UserExistedException();
        final var id = generateId.nextId();
        final var encodedPassword = passwordEncoder.encode(signUpApplicationDto.password());
       final var newUser = new User(
               id,
               signUpApplicationDto.email(),
               signUpApplicationDto.fullName(),
               encodedPassword,
               Role.USER,
               UserStatus.APPROVED,
               Instant.now(),
               Instant.now()
       );
       return userStorage.store(newUser);
    }
}
