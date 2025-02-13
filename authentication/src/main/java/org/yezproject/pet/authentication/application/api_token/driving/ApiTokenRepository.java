package org.yezproject.pet.authentication.application.api_token.driving;

import org.yezproject.pet.authentication.domain.api_token.ApiToken;

import java.util.Optional;
import java.util.Set;

public interface ApiTokenRepository {
    Set<ApiToken> findByUserId(String userId);

    String store(ApiToken apiToken);

    void deleteById(String userId, String id);

    Optional<ApiToken> findByToken(String token);
}
