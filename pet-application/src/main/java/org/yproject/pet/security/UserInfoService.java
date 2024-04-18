package org.yproject.pet.security;

public interface UserInfoService {
    UserInfo loadUserByEmail(String email);
}
