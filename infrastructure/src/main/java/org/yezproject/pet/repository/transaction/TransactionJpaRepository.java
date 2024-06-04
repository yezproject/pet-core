package org.yezproject.pet.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {

    Optional<TransactionEntity> findByIsDeleteFalseAndIdAndCreatorUserId(String id, String creatorUserId);

    List<TransactionEntity> findAllByIsDeleteFalseAndIdInAndCreatorUserId(Set<String> transactionIds, String creatorUserId);

    List<TransactionEntity> findAllByIsDeleteFalseAndCreatorUserId(String creatorId);

    @Query("select t from transactions t where t.isDelete = false and t.creatorUserId = :creatorId order by t.createDate desc limit :limit")
    List<TransactionEntity> findAllByIsDeleteFalseAndCreatorUserId(@Param("creatorId") String creatorId, @Param("limit") int limit);
}

