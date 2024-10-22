package org.yezproject.pet.authentication.infrastructure.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
}
