package org.yproject.pet.user;

import org.springframework.stereotype.Component;
import org.yproject.pet.open_api_token.ApiTokenDao;

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
            final var userTokenSet = apiTokenDao.findByUserId(user.getId().value())
                    .stream().map(it -> it.getId().toString())
                    .collect(Collectors.toSet());
            return new AuthInfo(user.getId().toString(), user.getEmail(), userTokenSet);
        }
    }
}
