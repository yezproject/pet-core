package org.yproject.pet.open_api_token.driven;

import java.util.List;

public interface OpenApiTokenService {
    OpenApiTokenIdWithTokenDto create(
            String userId,
            String email,
            String name
    );

    List<OpenApiTokenIdWithNameDto> retrieveAllByUserId(String userId);

    void delete(String userId, String id);
}
