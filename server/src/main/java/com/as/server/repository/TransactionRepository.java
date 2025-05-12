package com.as.server.repository;

import com.as.server.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    boolean existsByUserUserId(Integer userId);
    boolean existsBySubAccountSubAccountId(Integer subAccountId);
    boolean existsByTransactionTypeTypeId(Integer typeId);

    Page<Transaction> findByUserUserId(Integer userId, Pageable pageable);
    Page<Transaction> findBySubAccountSubAccountId(Integer subAccountId, Pageable pageable);
    Page<Transaction> findByUserUserIdAndSubAccountSubAccountId(Integer userId, Integer subAccountId, Pageable pageable);

    @Query("SELECT t.transactionType.typeId, t.transactionType.typeName, SUM(t.amount) " +
            "FROM Transaction t WHERE t.transactionType.isIncome = true AND t.time BETWEEN :start AND :end " +
            "AND (:userId IS NULL OR t.user.userId = :userId) " +
            "AND (:accountId IS NULL OR t.subAccount.account.accountId = :accountId) " +
            "AND (:subAccountId IS NULL OR t.subAccount.subAccountId = :subAccountId) " +
            "GROUP BY t.transactionType.typeId, t.transactionType.typeName")
    List<Object[]> findIncomeByType(Integer userId, Integer accountId, Integer subAccountId,
                                    LocalDateTime start, LocalDateTime end);

    @Query("SELECT t.transactionType.typeId, t.transactionType.typeName, SUM(t.amount) " +
            "FROM Transaction t WHERE t.transactionType.isIncome = false AND t.time BETWEEN :start AND :end " +
            "AND (:userId IS NULL OR t.user.userId = :userId) " +
            "AND (:accountId IS NULL OR t.subAccount.account.accountId = :accountId) " +
            "AND (:subAccountId IS NULL OR t.subAccount.subAccountId = :subAccountId) " +
            "GROUP BY t.transactionType.typeId, t.transactionType.typeName")
    List<Object[]> findExpenseByType(Integer userId, Integer accountId, Integer subAccountId,
                                     LocalDateTime start, LocalDateTime end);
}