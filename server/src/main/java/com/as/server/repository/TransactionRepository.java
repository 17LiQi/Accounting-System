package com.as.server.repository;

import com.as.server.dto.statistics.StatisticsResponseExpenseByType;
import com.as.server.dto.statistics.StatisticsResponseIncomeByType;
import com.as.server.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE t.transactionType.isIncome = :isIncome " +
            "AND (:userId IS NULL OR t.user.userId = :userId) " +
            "AND (:accountId IS NULL OR t.subAccount.account.accountId = :accountId) " +
            "AND (:subAccountId IS NULL OR t.subAccount.subAccountId = :subAccountId) " +
            "AND YEAR(t.time) = :year " +
            "AND (:period != 'MONTHLY' OR MONTH(t.time) = :month) " +
            "AND (:period != 'WEEKLY' OR WEEK(t.time, 1) = :week) " +
            "AND (:period != 'DAILY' OR MONTH(t.time) = :month AND DAY(t.time) = :day)")
    BigDecimal sumByTypeAndPeriod(@Param("isIncome") boolean isIncome,
                                  @Param("userId") Integer userId,
                                  @Param("accountId") Integer accountId,
                                  @Param("subAccountId") Integer subAccountId,
                                  @Param("period") String period,
                                  @Param("year") Integer year,
                                  @Param("month") Integer month,
                                  @Param("week") Integer week,
                                  @Param("day") Integer day);

    @Query("SELECT new com.as.server.dto.statistics.StatisticsResponseExpenseByType(t.transactionType.typeId, t.transactionType.typeName, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.transactionType.isIncome = false " +
            "AND (:userId IS NULL OR t.user.userId = :userId) " +
            "AND (:accountId IS NULL OR t.subAccount.account.accountId = :accountId) " +
            "AND (:subAccountId IS NULL OR t.subAccount.subAccountId = :subAccountId) " +
            "AND YEAR(t.time) = :year " +
            "AND (:period != 'MONTHLY' OR MONTH(t.time) = :month) " +
            "AND (:period != 'WEEKLY' OR WEEK(t.time, 1) = :week) " +
            "AND (:period != 'DAILY' OR MONTH(t.time) = :month AND DAY(t.time) = :day) " +
            "GROUP BY t.transactionType.typeId, t.transactionType.typeName")
    List<StatisticsResponseExpenseByType> sumExpenseByType(@Param("userId") Integer userId,
                                                           @Param("accountId") Integer accountId,
                                                           @Param("subAccountId") Integer subAccountId,
                                                           @Param("period") String period,
                                                           @Param("year") Integer year,
                                                           @Param("month") Integer month,
                                                           @Param("week") Integer week,
                                                           @Param("day") Integer day);

    @Query("SELECT new com.as.server.dto.statistics.StatisticsResponseIncomeByType(t.transactionType.typeId, t.transactionType.typeName, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.transactionType.isIncome = true " +
            "AND (:userId IS NULL OR t.user.userId = :userId) " +
            "AND (:accountId IS NULL OR t.subAccount.account.accountId = :accountId) " +
            "AND (:subAccountId IS NULL OR t.subAccount.subAccountId = :subAccountId) " +
            "AND YEAR(t.time) = :year " +
            "AND (:period != 'MONTHLY' OR MONTH(t.time) = :month) " +
            "AND (:period != 'WEEKLY' OR WEEK(t.time, 1) = :week) " +
            "AND (:period != 'DAILY' OR MONTH(t.time) = :month AND DAY(t.time) = :day) " +
            "GROUP BY t.transactionType.typeId, t.transactionType.typeName")
    List<StatisticsResponseIncomeByType> sumIncomeByType(@Param("userId") Integer userId,
                                                         @Param("accountId") Integer accountId,
                                                         @Param("subAccountId") Integer subAccountId,
                                                         @Param("period") String period,
                                                         @Param("year") Integer year,
                                                         @Param("month") Integer month,
                                                         @Param("week") Integer week,
                                                         @Param("day") Integer day);

}