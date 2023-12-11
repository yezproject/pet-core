package org.yproject.pet.core.application.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.yproject.pet.core.application.user.UserStorage;
import org.yproject.pet.core.domain.UserInfo;

@Component
public record UserInfoServiceImpl(
        UserStorage userStorage
) implements UserInfoService {
    @Override
    public UserInfo loadUserByUsername(String username) {
        return userStorage.findByUserName(username)
                .map(UserInfo::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
