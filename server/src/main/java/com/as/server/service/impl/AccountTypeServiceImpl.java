package com.as.server.service.impl;

import com.as.server.entity.AccountType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.AccountTypeRepository;
import com.as.server.repository.TransactionRepository;
import com.as.server.service.AccountTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {
    private final AccountTypeRepository accountTypeRepository;

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public AccountType create(AccountType accountType) {
        log.info("Creating account type: {}", accountType.getTypeName());
        return accountTypeRepository.save(accountType);
    }

    @Override
    public AccountType findById(Integer id) {
        log.info("Finding account type by id: {}", id);
        return accountTypeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account type not found with id: " + id));
    }

    @Override
    public List<AccountType> findAll() {
        log.info("Finding all account types");
        return accountTypeRepository.findAll();
    }

    @Override
    @Transactional
    public AccountType update(Integer id, AccountType accountType) {
        log.info("Updating account type with id: {}", id);
        AccountType existing = findById(id);
        existing.setTypeName(accountType.getTypeName());
        return accountTypeRepository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting account type with id: {}", id);
        if (!accountTypeRepository.existsById(id)) {
            throw new EntityNotFoundException("Account type not found with id : " + id);
        }
        accountTypeRepository.deleteById(id);

    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasTransactions(Integer typeId) {
        return transactionRepository.existsBySubAccountSubAccountId(typeId);
    }

}
