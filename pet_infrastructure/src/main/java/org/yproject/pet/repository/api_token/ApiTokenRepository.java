package org.yproject.pet.repository.api_token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

interface ApiTokenRepository extends JpaRepository<ApiTokenEntity, String> {
    Set<ApiTokenEntity> findAllByUserId(String userId);
}
