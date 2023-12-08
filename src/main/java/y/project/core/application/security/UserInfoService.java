package y.project.core.application.security;

import y.project.core.domain.UserInfo;

public interface UserInfoService {

    UserInfo loadUserByUsername(String username);
}
