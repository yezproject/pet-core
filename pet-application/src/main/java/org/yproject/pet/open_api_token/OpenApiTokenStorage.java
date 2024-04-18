package org.yproject.pet.open_api_token;

import org.yproject.pet.api_token.entities.ApiToken;

import java.util.Set;

public interface OpenApiTokenStorage {
    Set<ApiToken> findByUserId(String userId);

    String store(ApiToken apiToken);

    void deleteById(String userId, String id);
}
