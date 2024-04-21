package org.yproject.pet.open_api_token;

import java.util.Set;

public interface ApiTokenDao {
    Set<ApiTokenDto> findByUserId(String userId);

    String store(ApiTokenDto apiToken);

    void deleteById(String userId, String id);
}
