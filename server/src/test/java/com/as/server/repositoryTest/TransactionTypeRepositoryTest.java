package com.as.server.repositoryTest;

import com.as.server.entity.*;
import com.as.server.enums.CardType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.TransactionRepository;
import com.as.server.repository.TransactionTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionTypeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        entityManager.getEntityManager().createNativeQuery("DELETE FROM transactions").executeUpdate();
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback
    public void shouldSaveTransactionType() {
        TransactionType transactionType = new TransactionType();
        transactionType.setTypeName("Salary");
        transactionType.setIsIncome(true);

        TransactionType saved = transactionTypeRepository.save(transactionType);

        assertThat(saved.getTypeId()).isNotNull();
        assertThat(saved.getTypeName()).isEqualTo("Salary");
        assertThat(saved.getIsIncome()).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    public void shouldFindTransactionTypeById() {
        TransactionType transactionType = new TransactionType();
        transactionType.setTypeName("Salary");
        transactionType.setIsIncome(true);
        entityManager.persist(transactionType);

        TransactionType found = transactionTypeRepository.findById(transactionType.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("TransactionType not found with id: " + transactionType.getTypeId()));

        assertThat(found.getTypeName()).isEqualTo("Salary");
        assertThat(found.getIsIncome()).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    public void shouldEnforceForeignKeyConstraintForTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTime(LocalDateTime.now());
        transaction.setAmount(BigDecimal.valueOf(100.00));
        assertThrows(Exception.class, () -> transactionRepository.saveAndFlush(transaction));
    }

    @Test
    @Transactional
    @Rollback
    public void shouldDeleteTransactionTypeAndCleanTransactions() {
        // 创建相关实体
        AccountType accountType = new AccountType();
        accountType.setTypeName("Savings");
        entityManager.persist(accountType);

        Account account = new Account();
        account.setAccountName("Personal Account");
        account.setAccountType(accountType);
        entityManager.persist(account);

        SubAccount subAccount = new SubAccount();
        subAccount.setAccountName("Savings Card");
        subAccount.setAccount(account);
        subAccount.setAccountNumber("1234-5678-9012-3456");
        subAccount.setBalance(BigDecimal.ZERO);
        subAccount.setCardType(CardType.SAVINGS);
        entityManager.persist(subAccount);

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setIsAdmin(false);
        entityManager.persist(user);

        TransactionType transactionType = new TransactionType();
        transactionType.setTypeName("Salary");
        transactionType.setIsIncome(true);
        entityManager.persist(transactionType);

        // 创建 Transaction
        Transaction transaction = new Transaction();
        transaction.setTime(LocalDateTime.now());
        transaction.setTransactionType(transactionType);
        transaction.setSubAccount(subAccount);
        transaction.setAmount(BigDecimal.valueOf(100.00));
        transaction.setUser(user);
        entityManager.persist(transaction);

        entityManager.flush();
        entityManager.clear();

        // 删除 TransactionType
        TransactionType typeToDelete = transactionTypeRepository.findById(transactionType.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("TransactionType not found with id: " + transactionType.getTypeId()));
        // 清理 transactions
        transactionRepository.deleteAll(transactionRepository.findAll().stream()
                .filter(t -> t.getTransactionType().getTypeId().equals(typeToDelete.getTypeId()))
                .collect(Collectors.toList()));
        transactionTypeRepository.delete(typeToDelete);

        entityManager.flush();

        // 验证
        assertThat(transactionTypeRepository.findById(transactionType.getTypeId())).isEmpty();
        assertThat(transactionRepository.findById(transaction.getTransactionId())).isEmpty();
    }
}
