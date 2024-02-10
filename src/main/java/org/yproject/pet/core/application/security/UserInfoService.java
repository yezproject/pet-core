package org.yproject.pet.core.application.security;

import org.yproject.pet.core.infrastructure.web.config.security.UserInfo;

public interface UserInfoService {

    UserInfo loadUserByEmail(String email);
}
