package org.yproject.pet.core.infrastructure.repository.open_api_token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

interface OpenApiTokenRepository extends JpaRepository<OpenApiToken, String> {
    Set<OpenApiToken> findAllByUserId(String userId);
}
