package org.yproject.pet.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    Optional<TransactionEntity> findByIsDeleteFalseAndIdAndCreatorUserId(String id, String creatorUserId);

    List<TransactionEntity> findAllByIsDeleteFalseAndIdInAndCreatorUserId(Set<String> transactionIds, String creatorUserId);

    List<TransactionEntity> findAllByIsDeleteFalseAndCreatorUserId(String creatorId);
}

