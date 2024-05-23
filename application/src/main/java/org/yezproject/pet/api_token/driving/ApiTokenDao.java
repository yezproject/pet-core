package org.yezproject.pet.api_token.driving;

import org.yezproject.pet.Dao;

import java.util.Set;

@Dao
public interface ApiTokenDao {
    Set<ApiTokenDto> findByUserId(String userId);

    String store(ApiTokenDto apiToken);

    void deleteById(String userId, String id);
}
