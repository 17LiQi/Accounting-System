package com.as.server.repositoryTest;

import com.as.server.entity.Account;
import com.as.server.entity.AccountType;
import com.as.server.entity.SubAccount;
import com.as.server.enums.CardType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.AccountRepository;
import com.as.server.repository.SubAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SubAccountRepository subAccountRepository;

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
        assertThat(saved.getAccountName()).isEqualTo("Personal Account");
    }

    @Test
    @Transactional
    @Rollback
    void shouldEnforceForeignKeyConstraint() {
        SubAccount subAccount = new SubAccount();
        subAccount.setAccountName("Savings Card");
        subAccount.setBalance(BigDecimal.ZERO);
        subAccount.setCardType(CardType.SAVINGS);
        // 未设置 account，违反外键约束
        assertThrows(Exception.class, () -> subAccountRepository.saveAndFlush(subAccount));
    }

    @Test
    @Transactional
    @Rollback
    void shouldSaveSubAccountWithAccount() {
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
        assertThat(saved.getAccount().getAccountName()).isEqualTo("Personal Account");
    }

    @Test
    @Transactional
    @Rollback
    void shouldFetchSubAccountsForAccount() {
        AccountType accountType = new AccountType();
        accountType.setTypeName("Savings");
        entityManager.persist(accountType);

        Account account = new Account();
        account.setAccountName("Personal Account");
        account.setAccountType(accountType);
        entityManager.persist(account);

        SubAccount subAccount1 = new SubAccount();
        subAccount1.setAccountName("Card 1");
        subAccount1.setAccount(account);
        subAccount1.setAccountNumber("1234-5678-9012-3456");
        subAccount1.setBalance(BigDecimal.ZERO);
        subAccount1.setCardType(CardType.SAVINGS);
        entityManager.persist(subAccount1);

        SubAccount subAccount2 = new SubAccount();
        subAccount2.setAccountName("Card 2");
        subAccount2.setAccount(account);
        subAccount2.setAccountNumber("9876-5432-1098-7654");
        subAccount2.setBalance(BigDecimal.ZERO);
        subAccount2.setCardType(CardType.DEBIT);
        entityManager.persist(subAccount2);

        entityManager.flush();
        entityManager.clear();

        Account fetched = accountRepository.findById(account.getAccountId())
                .orElseThrow(()-> new EntityNotFoundException("Account not found with id: " + account.getAccountId()));
        List<SubAccount> subAccounts = fetched.getSubAccounts();

        assertThat(subAccounts).hasSize(2);
        assertThat(subAccounts).extracting(SubAccount::getAccountName).containsExactlyInAnyOrder("Card 1", "Card 2");
    }
}