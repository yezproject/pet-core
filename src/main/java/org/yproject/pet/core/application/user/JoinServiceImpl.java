package org.yproject.pet.core.application.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yproject.pet.core.domain.user.ApprovalStatus;
import org.yproject.pet.core.domain.user.Role;
import org.yproject.pet.core.domain.user.User;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;
import org.yproject.pet.core.infrastructure.web.jwt.JwtService;

import java.time.Instant;

@Component
@RequiredArgsConstructor
class JoinServiceImpl implements JoinService {
    private final UserStorage userStorage;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;

    @Override
    public String signIn(final String email, final String password) {
        final var existingUserOptional = userStorage.findByEmail(email);
        if (existingUserOptional.isEmpty()) throw new UserNotFoundException();

        final var isPasswordValid = passwordEncoder.matches(password, existingUserOptional.get().password());
        if (!isPasswordValid) throw new InvalidPasswordException();

        return jwtService.generateToken(email);
    }

    @Override
    public String signup(final SignUpApplicationDto signUpApplicationDto) {
        final var existingUserOptional = userStorage.findByEmail(signUpApplicationDto.email());
        if (existingUserOptional.isPresent()) throw new UserExistedException();
        final var id = idGenerator.get();
        final var encodedPassword = passwordEncoder.encode(signUpApplicationDto.password());
        final var newUser = new User(
                id,
                signUpApplicationDto.email(),
                signUpApplicationDto.fullName(),
                encodedPassword,
                Role.USER,
                ApprovalStatus.APPROVED,
                Instant.now(),
                Instant.now()
        );
        return userStorage.store(newUser);
    }
}
