package org.yezproject.pet.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yezproject.pet.id.IdGenerator;
import org.yezproject.pet.jwt.JwtService;
import org.yezproject.pet.jwt.JwtUserRequest;
import org.yezproject.pet.user.driven.JoinService;
import org.yezproject.pet.user.driven.SignUpApplicationDto;
import org.yezproject.pet.user.driving.PasswordService;
import org.yezproject.pet.user.driving.UserRepository;

@Component
@RequiredArgsConstructor
class JoinServiceImpl implements JoinService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordService passwordService;
    private final IdGenerator idGenerator;

    @Override
    public String signIn(final String email, final String password) {
        final var existingUserOptional = userRepository.findByEmail(email);
        if (existingUserOptional.isEmpty()) throw new UserNotFoundException();
        final var user = existingUserOptional.get();
        final var isPasswordValid = passwordService.matches(password, user.getPassword());
        if (!isPasswordValid) throw new InvalidPasswordException();

        return jwtService.generateToken(new JwtUserRequest(user.getEmail(), user.getFullName()));
    }

    @Override
    public String signup(final SignUpApplicationDto signUpApplicationDto) {
        final var existingUserOptional = userRepository.findByEmail(signUpApplicationDto.email());
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
        return userRepository.store(newUser);
    }
}
