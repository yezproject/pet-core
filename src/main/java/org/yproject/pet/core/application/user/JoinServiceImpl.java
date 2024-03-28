package org.yproject.pet.core.application.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yproject.pet.core.domain.user.entities.UserBuilder;
import org.yproject.pet.core.domain.user.enums.Role;
import org.yproject.pet.core.domain.user.value_objects.UserId;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;
import org.yproject.pet.core.infrastructure.web.jwt.JwtService;

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

        final var isPasswordValid = passwordEncoder.matches(password, existingUserOptional.get().getPassword());
        if (!isPasswordValid) throw new InvalidPasswordException();

        return jwtService.generateToken(email);
    }

    @Override
    public String signup(final SignUpApplicationDto signUpApplicationDto) {
        final var existingUserOptional = userStorage.findByEmail(signUpApplicationDto.email());
        if (existingUserOptional.isPresent()) throw new UserExistedException();
        final var id = idGenerator.get();
        final var encodedPassword = passwordEncoder.encode(signUpApplicationDto.password());
        final var newUser = new UserBuilder(new UserId(id))
                .email(signUpApplicationDto.email())
                .fullName(signUpApplicationDto.fullName())
                .password(encodedPassword)
                .role(Role.USER)
                .build();
        /* hardcode approve at time create */
        newUser.approve();
        return userStorage.store(newUser);
    }
}
