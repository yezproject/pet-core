package org.yezproject.pet.api_token.driven;

import java.util.List;

public interface ApiTokenService {
    class InvalidTokenException extends Exception {
    }

    ApiTokenIdWithTokenDto create(
            String userId,
            String email,
            String name
    );

    List<ApiTokenIdWithNameDto> retrieveAllByUserId(String userId);

    void delete(String userId, String id);

    UserIdDto verify(String token) throws InvalidTokenException;
}
