package com.as.server.service.impl;

import com.as.server.entity.Account;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.AccountRepository;
import com.as.server.repository.AccountTypeRepository;
import com.as.server.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AccountTypeRepository accountTypeRepository;

    @Override
    @Transactional
    public Account create(Account account) {
        log.info("Creating account: {}", account.getAccountName());
        if (account.getAccountType() == null || account.getAccountType().getTypeId() == null) {
            throw new IllegalArgumentException("Account type is required");
        }
        // 检查typeId是否存在
        if (!accountTypeRepository.existsById(account.getAccountType().getTypeId())) {
            throw new IllegalArgumentException("Account type not found");
        }
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Integer id) {
        log.info("Finding account by id: {}", id);
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + id));
    }

    @Override
    public List<Account> findAll() {
        log.info("Finding all accounts");
        return accountRepository.findAll();
    }

    @Override
    @Transactional
    public Account update(Integer id, Account account) {
        log.info("Updating account with id: {}", id);
        Account existing = findById(id);
        existing.setAccountName(account.getAccountName());
        existing.setAccountType(account.getAccountType());
        return accountRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting account with id: {}", id);
        if (!accountRepository.existsById(id)) {
            throw new EntityNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasSubAccounts(Integer accountId) {
        return accountRepository.existsByIdWithSubAccounts(accountId);
    }
}
