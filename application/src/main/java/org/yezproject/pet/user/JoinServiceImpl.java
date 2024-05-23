package org.yezproject.pet.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yezproject.pet.id.IdGenerator;
import org.yezproject.pet.jwt.JwtService;
import org.yezproject.pet.user.driven.JoinService;
import org.yezproject.pet.user.driven.SignUpApplicationDto;
import org.yezproject.pet.user.driving.PasswordService;

@Component
@RequiredArgsConstructor
class JoinServiceImpl implements JoinService {
    private final UserStorage userStorage;
    private final JwtService jwtService;
    private final PasswordService passwordService;
    private final IdGenerator idGenerator;

    @Override
    public String signIn(final String email, final String password) {
        final var existingUserOptional = userStorage.findByEmail(email);
        if (existingUserOptional.isEmpty()) throw new UserNotFoundException();

        final var isPasswordValid = passwordService.matches(password, existingUserOptional.get().getPassword());
        if (!isPasswordValid) throw new InvalidPasswordException();

        return jwtService.generateToken(email);
    }

    @Override
    public String signup(final SignUpApplicationDto signUpApplicationDto) {
        final var existingUserOptional = userStorage.findByEmail(signUpApplicationDto.email());
        if (existingUserOptional.isPresent()) throw new UserExistedException();
        final var id = idGenerator.get();
        final var encodedPassword = passwordService.encode(signUpApplicationDto.password());
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
