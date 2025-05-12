package com.as.server.service.impl;

import com.as.server.entity.Transaction;
import com.as.server.entity.TransactionType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.TransactionRepository;
import com.as.server.repository.TransactionTypeRepository;
import com.as.server.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionTypeRepository transactionTypeRepository;

    @Override
    @Transactional
    public Transaction create(Transaction transaction) {
        log.info("Creating transaction at time: {}", transaction.getTime());
        TransactionType type = transactionTypeRepository.findById(transaction.getTransactionType().getTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction type not found"));
        transaction.setTransactionType(type);
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findById(Integer id) {
        log.info("Finding transaction by id: {}", id);
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Integer userId, Integer subAccountId, Pageable pageable) {
        if (userId != null && subAccountId != null) {
            return transactionRepository.findByUserUserIdAndSubAccountSubAccountId(userId, subAccountId, pageable);
        } else if (userId != null) {
            return transactionRepository.findByUserUserId(userId, pageable);
        } else if (subAccountId != null) {
            return transactionRepository.findBySubAccountSubAccountId(subAccountId, pageable);
        }
        return transactionRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Transaction update(Integer id, Transaction transaction) {
        log.info("Updating transaction with id: {}", id);
        Transaction existing = findById(id);
        existing.setTime(transaction.getTime());
        existing.setTransactionType(transaction.getTransactionType());
        existing.setSubAccount(transaction.getSubAccount());
        existing.setAmount(transaction.getAmount());
        existing.setUser(transaction.getUser());
        existing.setRemarks(transaction.getRemarks());
        return transactionRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting transaction with id: {}", id);
        if (!transactionRepository.existsById(id)) {
            throw new EntityNotFoundException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }
}
