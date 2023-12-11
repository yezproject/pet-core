package org.yproject.pet.core.application.security;

import org.yproject.pet.core.domain.UserInfo;

public interface UserInfoService {

    UserInfo loadUserByUsername(String username);
}
