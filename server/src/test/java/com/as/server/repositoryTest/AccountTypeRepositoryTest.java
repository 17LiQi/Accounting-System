package com.as.server.repositoryTest;

import com.as.server.entity.Account;
import com.as.server.entity.AccountType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.AccountRepository;
import com.as.server.repository.AccountTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountTypeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Transactional
    @Rollback
    void shouldSaveAccountType() {
        AccountType accountType = new AccountType();
        accountType.setTypeName("Savings");

        AccountType saved = accountTypeRepository.save(accountType);

        assertThat(saved.getTypeId()).isNotNull();
        assertThat(saved.getTypeName()).isEqualTo("Savings");
    }

    @Test
    @Transactional
    @Rollback
    void shouldEnforceForeignKeyConstraint() {
        Account account = new Account();
        account.setAccountName("Personal Account");
        // 未设置 accountType，违反外键约束
        assertThrows(Exception.class, () -> accountRepository.saveAndFlush(account));
    }

    @Test
    @Transactional
    @Rollback
    void shouldSaveAccountWithAccountType() {
        AccountType accountType = new AccountType();
        accountType.setTypeName("Savings");
        entityManager.persist(accountType);

        Account account = new Account();
        account.setAccountName("Personal Account");
        account.setAccountType(accountType);

        Account saved = accountRepository.save(account);

        assertThat(saved.getAccountId()).isNotNull();
        assertThat(saved.getAccountType().getTypeName()).isEqualTo("Savings");
    }

    @Test
    @Transactional
    @Rollback
    void shouldFetchAccountsForAccountType() {
        AccountType accountType = new AccountType();
        accountType.setTypeName("Savings");
        entityManager.persist(accountType);

        Account account1 = new Account();
        account1.setAccountName("Account 1");
        account1.setAccountType(accountType);
        entityManager.persist(account1);

        Account account2 = new Account();
        account2.setAccountName("Account 2");
        account2.setAccountType(accountType);
        entityManager.persist(account2);

        entityManager.flush();
        entityManager.clear();

        AccountType fetched = accountTypeRepository.findById(accountType.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Account type not found"));
        List<Account> accounts = fetched.getAccounts();

        assertThat(accounts).hasSize(2);
        assertThat(accounts).extracting(Account::getAccountName).containsExactlyInAnyOrder("Account 1", "Account 2");
    }
}
