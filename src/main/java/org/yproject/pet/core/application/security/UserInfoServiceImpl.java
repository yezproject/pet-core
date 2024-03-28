package org.yproject.pet.core.application.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.yproject.pet.core.application.user.UserStorage;
import org.yproject.pet.core.application.open_api_token.OpenApiTokenStorage;
import org.yproject.pet.core.infrastructure.web.security.UserInfo;

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
