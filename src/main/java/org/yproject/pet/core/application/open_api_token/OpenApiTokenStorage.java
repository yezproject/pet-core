package org.yproject.pet.core.application.open_api_token;

import org.yproject.pet.core.domain.open_api_token.OpenApiToken;

import java.util.Set;

public interface OpenApiTokenStorage {
    Set<OpenApiToken> findByUserId(String userId);

    String store(OpenApiToken openApiToken);

    void deleteById(String userId, String id);
}
