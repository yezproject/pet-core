package org.yezproject.pet.authentication.application.user;

import lombok.RequiredArgsConstructor;
import org.yezproject.pet.authentication.application.id.IdGenerator;
import org.yezproject.pet.authentication.application.user.driven.GeneralUserDto;
import org.yezproject.pet.authentication.application.user.driven.JoinService;
import org.yezproject.pet.authentication.application.user.driven.SignInDto;
import org.yezproject.pet.authentication.application.user.driven.SignUpDto;
import org.yezproject.pet.authentication.application.user.driving.PasswordService;
import org.yezproject.pet.authentication.application.user.driving.UserRepository;
import org.yezproject.pet.authentication.domain.user.Role;
import org.yezproject.pet.authentication.domain.user.UserBuilder;
import org.yezproject.pet.domain_common.UseCase;

@UseCase
@RequiredArgsConstructor
class JoinServiceImpl implements JoinService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final IdGenerator idGenerator;

    @Override
    public GeneralUserDto signIn(final SignInDto signInDto) {
        final var existingUserOptional = userRepository.findByEmail(signInDto.email());
        if (existingUserOptional.isEmpty()) throw new UserNotFoundException();
        final var user = existingUserOptional.get();
        final var isPasswordValid = passwordService.matches(signInDto.password(), user.getPassword());
        if (!isPasswordValid) throw new InvalidPasswordException();
        return new GeneralUserDto(user.getId(), user.getEmail(), user.getFullName());
    }

    @Override
    public String signup(final SignUpDto signUpDto) {
        final var existingUserOptional = userRepository.findByEmail(signUpDto.email());
        if (existingUserOptional.isPresent()) throw new UserExistedException();
        final var id = idGenerator.get();
        final var encodedPassword = passwordService.encode(signUpDto.password());
        final var newUser = new UserBuilder(id)
                .email(signUpDto.email())
                .fullName(signUpDto.fullName())
                .password(encodedPassword)
                .role(Role.USER)
                .build();
        /* hardcode approve at time create */
        newUser.approve();
        return userRepository.store(newUser);
    }
}
