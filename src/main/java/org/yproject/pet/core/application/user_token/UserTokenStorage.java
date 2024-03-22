package org.yproject.pet.core.application.user_token;

import org.yproject.pet.core.domain.user_token.UserToken;

import java.util.Set;

public interface UserTokenStorage {
    Set<UserToken> findByUserId(String userId);

    String store(UserToken userToken);

    void deleteById(String userId, String id);
}
