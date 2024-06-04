package org.yezproject.pet.user;

import org.springframework.stereotype.Component;
import org.yezproject.pet.api_token.ApiToken;
import org.yezproject.pet.api_token.driving.ApiTokenRepository;
import org.yezproject.pet.user.driven.AuthInfo;
import org.yezproject.pet.user.driven.AuthService;
import org.yezproject.pet.user.driving.UserRepository;

import java.util.stream.Collectors;

@Component
record AuthServiceImpl(
        UserRepository userRepository,
        ApiTokenRepository apiTokenRepository
) implements AuthService {

    @Override
    public AuthInfo loadUserByEmail(String email) throws UserNotFoundException {
        final var optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new UserNotFoundException("User email: %s not found".formatted(email));
        else {
            final var user = optionalUser.get();
            final var userTokenSet = apiTokenRepository.findByUserId(user.getId())
                    .stream().map(ApiToken::getId)
                    .collect(Collectors.toSet());
            return new AuthInfo(user.getId(), user.getEmail(), userTokenSet);
        }
    }
}
