package org.yproject.pet.core.infrastructure.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    Optional<TransactionEntity> findByIdAndCreatorUserId(String id, String creatorId);

    void deleteAllByIdInAndCreatorUserId(Set<String> ids, String creatorId);

    List<TransactionEntity> findAllByCreatorUserId(String creatorId);
}

