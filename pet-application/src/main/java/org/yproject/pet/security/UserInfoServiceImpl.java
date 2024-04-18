package org.yproject.pet.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.yproject.pet.open_api_token.OpenApiTokenStorage;
import org.yproject.pet.user.UserStorage;

@Component
record UserInfoServiceImpl(
        UserStorage userStorage,
        OpenApiTokenStorage openApiTokenStorage
) implements UserInfoService {

    @Override
    public UserInfo loadUserByEmail(String email) {
        final var optionalUser = userStorage.findByEmail(email);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("User not found");
        else {
            final var user = optionalUser.get();
            final var userTokenSet = openApiTokenStorage.findByUserId(user.getId().value());
            return new UserInfo(user, userTokenSet);
        }
    }
}
