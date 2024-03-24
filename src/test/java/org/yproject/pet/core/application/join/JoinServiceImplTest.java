package org.yproject.pet.core.application.join;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.yproject.pet.core.application.user.JoinService;
import org.yproject.pet.core.application.user.JoinServiceImpl;
import org.yproject.pet.core.application.user.SignUpApplicationDto;
import org.yproject.pet.core.application.user.UserStorage;
import org.yproject.pet.core.domain.user.User;
import org.yproject.pet.core.infrastructure.generator.identity.IdGenerator;
import org.yproject.pet.core.infrastructure.web.jwt.JwtService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.yproject.pet.core.util.RandomUtils.randomLongString;
import static org.yproject.pet.core.util.RandomUtils.randomShortString;
import static org.yproject.pet.core.util.UserRandomUtils.randomUser;

@ExtendWith(MockitoExtension.class)
class JoinServiceImplTest {
    private JoinService underTest;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserStorage userStorage;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private IdGenerator idGenerator;

    @BeforeEach
    void setup() {
        underTest = new JoinServiceImpl(
                userStorage,
                jwtService,
                passwordEncoder,
                idGenerator
        );
    }

    @Test
    void signup_success() {
        // given
        SignUpApplicationDto dto = new SignUpApplicationDto(
                randomShortString(),
                randomShortString(),
                randomLongString()
        );
        final var newUserId = randomShortString();
        final var encodedPassword = randomLongString();

        // when
        when(userStorage.findByEmail(any())).thenReturn(Optional.empty());
        when(idGenerator.get()).thenReturn(newUserId);
        when(passwordEncoder.encode(any())).thenReturn(encodedPassword);

        underTest.signup(dto);

        // then
        final var userCaptor = ArgumentCaptor.forClass(User.class);
        then(userStorage).should().findByEmail(dto.email());
        then(idGenerator).should().get();
        then(passwordEncoder).should().encode(dto.password());
        verify(userStorage).store(userCaptor.capture());

        assertThat(userCaptor.getValue())
                .returns(newUserId, User::id)
                .returns(encodedPassword, User::password)
                .returns(dto.fullName(), User::fullName)
                .returns(dto.email(), User::email);
    }

    @Test
    void error_by_existed_user_email() {
        // given
        SignUpApplicationDto dto = new SignUpApplicationDto(
                randomShortString(),
                randomShortString(),
                randomLongString()
        );

        // when
        when(userStorage.findByEmail(any())).thenReturn(Optional.of(randomUser()));

        assertThrows(JoinService.UserExistedException.class, () -> underTest.signup(dto));

        // then
        then(userStorage).should().findByEmail(dto.email());

    }
}
