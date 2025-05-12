package com.as.server.repositoryTest;

import com.as.server.entity.*;
import com.as.server.enums.CardType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.SubAccountRepository;
import com.as.server.repository.TransactionRepository;
import com.as.server.repository.TransactionTypeRepository;
import com.as.server.repository.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SubAccountRepository subAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @BeforeEach
    public void setup() {
        entityManager.getEntityManager().createNativeQuery("DELETE FROM transactions").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM user_sub_accounts").executeUpdate();
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback
    public void shouldSaveTransactionWithRelationships() {
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

        Transaction saved = transactionRepository.save(transaction);

        assertThat(saved.getTransactionId()).isNotNull();
        assertThat(saved.getAmount()).isEqualTo(BigDecimal.valueOf(100.00));
        assertThat(saved.getSubAccount().getAccountName()).isEqualTo("Savings Card");
        assertThat(saved.getUser().getUsername()).isEqualTo("testuser");
        assertThat(saved.getTransactionType().getTypeName()).isEqualTo("Salary");
    }

    @Test
    @Transactional
    @Rollback
    public void shouldFindTransactionById() {
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

        Transaction found = transactionRepository.findById(transaction.getTransactionId())
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + transaction.getTransactionId()));

        assertThat(found.getAmount()).isEqualTo(BigDecimal.valueOf(100.00));
        assertThat(found.getSubAccount().getAccountName()).isEqualTo("Savings Card");
    }

    @Test
    @Transactional
    @Rollback
    public void shouldEnforceForeignKeyConstraints() {
        Transaction transaction = new Transaction();
        transaction.setTime(LocalDateTime.now());
        transaction.setAmount(BigDecimal.valueOf(100.00));
        assertThrows(Exception.class, () -> transactionRepository.saveAndFlush(transaction));
    }

    @Test
    @Transactional
    @Rollback
    public void shouldDeleteTransaction() {
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

        // 删除 Transaction
        transactionRepository.deleteById(transaction.getTransactionId());

        assertThat(transactionRepository.findById(transaction.getTransactionId())).isEmpty();
    }
}
