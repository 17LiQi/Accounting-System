package com.as.server.repositoryTest;

import com.as.server.entity.Account;
import com.as.server.entity.AccountType;
import com.as.server.entity.SubAccount;
import com.as.server.entity.User;
import com.as.server.enums.CardType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.SubAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubAccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SubAccountRepository subAccountRepository;

    @BeforeEach
    public void setup() {
        entityManager.getEntityManager().createNativeQuery("DELETE FROM user_sub_accounts").executeUpdate();
        entityManager.flush();
    }

    @Test
    @Transactional
    @Rollback
    public void shouldSaveSubAccountWithAccount() {
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

        SubAccount saved = subAccountRepository.save(subAccount);

        assertThat(saved.getSubAccountId()).isNotNull();
        assertThat(saved.getAccountName()).isEqualTo("Savings Card");
    }

    @Test
    @Transactional
    @Rollback
    public void shouldEnforceForeignKeyConstraint() {
        SubAccount subAccount = new SubAccount();
        subAccount.setAccountName("Savings Card");
        subAccount.setBalance(BigDecimal.ZERO);
        subAccount.setCardType(CardType.SAVINGS);
        assertThrows(Exception.class, () -> subAccountRepository.saveAndFlush(subAccount));
    }

    @Test
    @Transactional
    @Rollback
    public void shouldHandleManyToManyWithUser() {
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

        subAccount.addUser(user);
        subAccountRepository.save(subAccount);

        entityManager.flush();
        entityManager.clear();

        SubAccount fetchedSubAccount = subAccountRepository.findById(subAccount.getSubAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Sub_account not found with id: " + subAccount.getSubAccountId()));
        assertThat(fetchedSubAccount.getUsers()).hasSize(1);
        assertThat(new ArrayList<User>(fetchedSubAccount.getUsers()).get(0).getUsername()).isEqualTo("testuser");
    }

    @Test
    @Transactional
    @Rollback
    public void shouldPreventDuplicateUserInManyToMany() {
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

        subAccount.addUser(user);
        subAccount.addUser(user); // 重复添加
        subAccountRepository.save(subAccount);

        entityManager.flush();
        entityManager.clear();

        SubAccount fetchedSubAccount = subAccountRepository.findById(subAccount.getSubAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Sub-account not found with id: " + subAccount.getSubAccountId()));
        assertThat(fetchedSubAccount.getUsers()).hasSize(1);
        assertThat(fetchedSubAccount.getUsers().stream().findFirst().get().getUsername()).isEqualTo("testuser");
    }
}