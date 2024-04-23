package org.yproject.pet.api_token.driven;

import java.util.List;

public interface ApiTokenService {
    ApiTokenIdWithTokenDto create(
            String userId,
            String email,
            String name
    );

    List<ApiTokenIdWithNameDto> retrieveAllByUserId(String userId);

    void delete(String userId, String id);
}
