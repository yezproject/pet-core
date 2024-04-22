package org.yproject.pet.open_api_token.driving;

import org.yproject.pet.Dao;

import java.util.Set;

@Dao
public interface ApiTokenDao {
    Set<ApiTokenDto> findByUserId(String userId);

    String store(ApiTokenDto apiToken);

    void deleteById(String userId, String id);
}
