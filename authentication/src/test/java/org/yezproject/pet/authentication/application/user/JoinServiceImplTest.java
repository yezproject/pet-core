package org.yezproject.pet.authentication.application.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yezproject.pet.authentication.application.id.IdGenerator;
import org.yezproject.pet.authentication.application.user.driven.JoinService;
import org.yezproject.pet.authentication.application.user.driven.SignUpDto;
import org.yezproject.pet.authentication.application.user.driving.PasswordService;
import org.yezproject.pet.authentication.application.user.driving.UserRepository;
import org.yezproject.pet.authentication.domain.user.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.yezproject.pet.test_common.RandomUtils.randomLongString;
import static org.yezproject.pet.test_common.RandomUtils.randomShortString;
import static org.yezproject.pet.authentication.application.user.UserRandomUtils.randomUser;

@ExtendWith(MockitoExtension.class)
class JoinServiceImplTest {
    private JoinService underTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordService passwordService;
    @Mock
    private IdGenerator idGenerator;

    @BeforeEach
    void setup() {
        underTest = new JoinServiceImpl(
                userRepository,
                passwordService,
                idGenerator
        );
    }

    @Test
    void signup_success() {
        // given
        SignUpDto dto = new SignUpDto(
                randomShortString(),
                randomShortString(),
                randomLongString()
        );
        final var newUserId = randomShortString();
        final var encodedPassword = randomLongString();

        // when
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(idGenerator.get()).thenReturn(newUserId);
        when(passwordService.encode(any())).thenReturn(encodedPassword);

        underTest.signup(dto);

        // then
        final var userCaptor = ArgumentCaptor.forClass(User.class);
        then(userRepository).should().findByEmail(dto.email());
        then(idGenerator).should().get();
        then(passwordService).should().encode(dto.password());
        verify(userRepository).store(userCaptor.capture());

        assertThat(userCaptor.getValue())
                .returns(newUserId, User::getId)
                .returns(encodedPassword, User::getPassword)
                .returns(dto.fullName(), User::getFullName)
                .returns(dto.email(), User::getEmail);
    }

    @Test
    void error_by_existed_user_email() {
        // given
        SignUpDto dto = new SignUpDto(
                randomShortString(),
                randomShortString(),
                randomLongString()
        );

        // when
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(randomUser()));

        assertThrows(JoinService.UserExistedException.class, () -> underTest.signup(dto));

        // then
        then(userRepository).should().findByEmail(dto.email());

    }
}
