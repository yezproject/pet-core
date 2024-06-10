package org.yezproject.pet.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {

    Optional<TransactionEntity> findByIsDeleteFalseAndIdAndCreatorUserId(String id, String creatorUserId);

    List<TransactionEntity> findAllByIsDeleteFalseAndIdInAndCreatorUserId(Set<String> transactionIds, String creatorUserId);

    @Query("select t from transactions t where t.creatorUserId = :creatorId")
    List<TransactionEntity> findAll(String creatorId);

    @Query("""
            select t from transactions t\s
            where t.isDelete = false and t.creatorUserId = :creatorId\s
            order by t.transactionDate desc\s
            limit :limit\s
           \s""")
    List<TransactionEntity> findAll(@Param("creatorId") String creatorId, @Param("limit") int limit);

    @Query("""
            select t from transactions t\s
            where t.isDelete = false and t.creatorUserId = :creatorId and t.transactionDate < :after\s
            order by t.transactionDate desc\s
            limit :limit\s
           \s""")
    List<TransactionEntity> findAll(@Param("creatorId") String creatorId, @Param("limit") int limit, @Param("after") Instant after);
}

