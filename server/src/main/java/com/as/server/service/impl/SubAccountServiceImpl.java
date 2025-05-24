package com.as.server.service.impl;

import com.as.server.entity.SubAccount;
import com.as.server.entity.User;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.SubAccountRepository;
import com.as.server.repository.TransactionRepository;
import com.as.server.repository.UserRepository;
import com.as.server.service.SubAccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class SubAccountServiceImpl implements SubAccountService {

    private static final Logger log = LoggerFactory.getLogger(SubAccountServiceImpl.class);

    private final SubAccountRepository subAccountRepository;

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public SubAccount create(SubAccount subAccount) {
        log.info("Creating sub-account: {}", subAccount.getAccountName());
        if (subAccount.getAccountNumber() == null || subAccount.getAccountNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Account number is required");
        }
        if (subAccountRepository.existsByAccountNumber(subAccount.getAccountNumber())) {
            throw new IllegalArgumentException("Account number already exists");
        }
        log.info("Creating sub-account: {}", subAccount.getAccountName());

        // 保存子账号基本信息
        SubAccount savedSubAccount = subAccountRepository.save(subAccount);

        // 处理用户关联
        if (subAccount.getUsers() != null && !subAccount.getUsers().isEmpty()) {
            Set<User> users = subAccount.getUsers();
            for (User user : users) {
                User existingUser = userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + user.getUserId()));
                savedSubAccount.addUser(existingUser);
            }
        }
        
        return subAccountRepository.save(savedSubAccount);
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
        existing.setAccountName(subAccount.getAccountName());
        existing.setAccount(subAccount.getAccount());
        existing.setAccountNumber(subAccount.getAccountNumber());
        existing.setCardType(subAccount.getCardType());
        existing.setBalance(subAccount.getBalance());
        
        // 更新用户关联
        if (subAccount.getUsers() != null) {
            existing.getUsers().clear();
            for (User user : subAccount.getUsers()) {
                User existingUser = userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + user.getUserId()));
                existing.addUser(existingUser);
            }
        }
        
        return subAccountRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting sub-account with id: {}", id);
        if (!subAccountRepository.existsById(id)) {
            throw new EntityNotFoundException("SubAccount not found with id: " + id);
        }
        if (hasTransactions(id)) {
            throw new IllegalStateException("Cannot delete sub-account with associated transactions");
        }
        subAccountRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasTransactions(Integer subAccountId) {
        return transactionRepository.existsBySubAccountSubAccountId(subAccountId);
    }

}
