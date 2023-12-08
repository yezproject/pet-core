package y.project.core.application.security;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import y.project.core.application.user.UserStorage;
import y.project.core.domain.UserInfo;

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
