package org.yezproject.pet.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yezproject.pet.common.models.Entity;
import org.yezproject.pet.id.IdGenerator;
import org.yezproject.pet.jwt.JwtService;
import org.yezproject.pet.user.driven.JoinService;
import org.yezproject.pet.user.driven.SignUpApplicationDto;
import org.yezproject.pet.user.driving.PasswordService;
import org.yezproject.pet.user.driving.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.yezproject.pet.RandomUtils.randomLongString;
import static org.yezproject.pet.RandomUtils.randomShortString;
import static org.yezproject.pet.user.UserRandomUtils.randomUser;

@ExtendWith(MockitoExtension.class)
class JoinServiceImplTest {
    private JoinService underTest;
    @Mock
    private JwtService jwtService;
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
                jwtService,
                passwordService,
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
                .returns(newUserId, Entity::getId)
                .returns(encodedPassword, User::getPassword)
                .returns(dto.fullName(), User::getFullName)
                .returns(dto.email(), User::getEmail);
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
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(randomUser()));

        assertThrows(JoinService.UserExistedException.class, () -> underTest.signup(dto));

        // then
        then(userRepository).should().findByEmail(dto.email());

    }
}
