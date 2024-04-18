package org.yproject.pet.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yproject.pet.id.IdGenerator;
import org.yproject.pet.jwt.JwtService;
import org.yproject.pet.user.entities.UserBuilder;
import org.yproject.pet.user.enums.Role;

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
        final var newUser = new UserBuilder(id)
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
