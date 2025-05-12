package com.as.server.service.impl;

import com.as.server.entity.SubAccount;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.SubAccountRepository;
import com.as.server.repository.TransactionRepository;
import com.as.server.service.SubAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubAccountServiceImpl implements SubAccountService {

    private final SubAccountRepository subAccountRepository;

    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public SubAccount create(SubAccount subAccount) {
        log.info("Creating sub-account: {}", subAccount.getName());
        if (subAccount.getAccountNumber() == null || subAccount.getAccountNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (subAccountRepository.existsByAccountNumber(subAccount.getAccountNumber())) {
            throw new IllegalArgumentException("Account number already exists");
        }
        log.info("Creating sub-account: {}", subAccount.getName());
        return subAccountRepository.save(subAccount);
    }

    @Override
    public SubAccount findById(Integer id) {
        log.info("Finding sub-account by id: {}", id);
        return subAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubAccount not found with id: " + id));
    }

    @Override
    public List<SubAccount> findAll() {
        log.info("Finding all sub-accounts");
        return subAccountRepository.findAll();
    }

    @Override
    @Transactional
    public SubAccount update(Integer id, SubAccount subAccount) {
        log.info("Updating sub-account with id: {}", id);
        SubAccount existing = findById(id);
        if (subAccount.getAccountNumber() == null || subAccount.getAccountNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (!subAccount.getAccountNumber().equals(existing.getAccountNumber()) &&
                subAccountRepository.existsByAccountNumber(subAccount.getAccountNumber())) {
            throw new IllegalArgumentException("Account number already exists");
        }
        existing.setName(subAccount.getName());
        existing.setAccount(subAccount.getAccount());
        existing.setAccountNumber(subAccount.getAccountNumber());
        existing.setCardType(subAccount.getCardType());
        existing.setBalance(subAccount.getBalance());
        return subAccountRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting sub-account with id: {}", id);
        if (!subAccountRepository.existsById(id)) {
            throw new EntityNotFoundException("SubAccount not found with id: " + id);
        }
        subAccountRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasTransactions(Integer subAccountId) {
        return transactionRepository.existsBySubAccountSubAccountId(subAccountId);
    }

}
