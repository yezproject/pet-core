package org.yezproject.pet.authentication.infrastructure.repository.api_token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

interface ApiTokenJpaRepository extends JpaRepository<ApiTokenEntity, String> {
    Set<ApiTokenEntity> findAllByUserId(String userId);

    Optional<ApiTokenEntity> findByToken(String token);
}
