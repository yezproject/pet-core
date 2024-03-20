package org.yproject.pet.core.application.user_token;

import java.util.List;

public interface UserTokenService {
    UserTokenIdWithTokenDto create(
            String userId,
            String email,
            String name
    );

    List<UserTokenIdWithNameDto> retrieveAllByUserId(String userId);

    void delete(String userId, String id);
}
