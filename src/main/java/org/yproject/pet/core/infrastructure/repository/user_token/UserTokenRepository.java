package org.yproject.pet.core.infrastructure.repository.user_token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

interface UserTokenRepository extends JpaRepository<UserTokenEntity, String> {
    Set<UserTokenEntity> findAllByUserId(String userId);
}
