package org.yproject.pet.core.infrastructure.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRepository extends JpaRepository<UserEntity, String> {

}
