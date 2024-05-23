package org.yezproject.pet.user;

import org.springframework.stereotype.Component;
import org.yezproject.pet.api_token.driving.ApiTokenDao;
import org.yezproject.pet.api_token.driving.ApiTokenDto;
import org.yezproject.pet.user.driven.AuthInfo;
import org.yezproject.pet.user.driven.AuthService;

import java.util.stream.Collectors;

@Component
record AuthServiceImpl(
        UserStorage userStorage,
        ApiTokenDao apiTokenDao
) implements AuthService {

    @Override
    public AuthInfo loadUserByEmail(String email) throws UserNotFoundException {
        final var optionalUser = userStorage.findByEmail(email);
        if (optionalUser.isEmpty()) throw new UserNotFoundException("User email: %s not found".formatted(email));
        else {
            final var user = optionalUser.get();
            final var userTokenSet = apiTokenDao.findByUserId(user.getId())
                    .stream().map(ApiTokenDto::getId)
                    .collect(Collectors.toSet());
            return new AuthInfo(user.getId(), user.getEmail(), userTokenSet);
        }
    }
}