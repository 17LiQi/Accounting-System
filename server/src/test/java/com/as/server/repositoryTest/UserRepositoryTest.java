package com.as.server.repositoryTest;

import com.as.server.entity.*;
import com.as.server.enums.CardType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.SubAccountRepository;
import com.as.server.repository.TransactionRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SubAccountRepository subAccountRepository;

    @BeforeEach
    public void setup() {
        entityManager.getEntityManager().createNativeQuery("DELETE FROM user_sub_accounts").executeUpdate();
        entityManager.getEntityManager().createNativeQuery("DELETE FROM transactions").executeUpdate();
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback
    public void shouldSaveUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setIsAdmin(false);

        User saved = userRepository.save(user);

        assertThat(saved.getUserId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("testuser");
        assertThat(saved.getIsAdmin()).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    public void shouldFindUserById() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setIsAdmin(false);
        entityManager.persist(user);

        User found = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + user.getUserId()));

        assertThat(found.getUsername()).isEqualTo("testuser");
        assertThat(found.getPassword()).isEqualTo("password");
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
    public void shouldDeleteUserAndCleanRelationships() {
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

        // 建立 user_sub_accounts 关系
        subAccount.addUser(user);
        subAccountRepository.save(subAccount);

        // 创建 Transaction
        TransactionType transactionType = new TransactionType();
        transactionType.setTypeName("Salary");
        transactionType.setIsIncome(true);
        entityManager.persist(transactionType);

        Transaction transaction = new Transaction();
        transaction.setTime(LocalDateTime.now());
        transaction.setTransactionType(transactionType);
        transaction.setSubAccount(subAccount);
        transaction.setAmount(BigDecimal.valueOf(100.00));
        transaction.setUser(user);
        entityManager.persist(transaction);

        entityManager.flush();
        entityManager.clear();

        // 删除 User
        User userToDelete = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + user.getUserId()));
        // 清理 user_sub_accounts
        List<SubAccount> subAccounts = subAccountRepository.findSubAccountsByUserId(user.getUserId());
        for (SubAccount sa : subAccounts) {
            sa.getUsers().removeIf(u -> u.getUserId().equals(user.getUserId()));
            subAccountRepository.save(sa);
        }
        userRepository.delete(userToDelete);

        entityManager.flush();

        // 验证
        assertThat(userRepository.findById(user.getUserId())).isEmpty();
        assertThat(subAccountRepository.findById(subAccount.getSubAccountId()).get().getUsers()).isEmpty();
        assertThat(transactionRepository.findById(transaction.getTransactionId())).isEmpty();
    }
}
