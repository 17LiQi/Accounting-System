package com.as.server.service;

import com.as.server.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionService {
    Transaction create(Transaction transaction);
    Transaction findById(Integer id);


    @Transactional(readOnly = true)
    Page<Transaction> findAll(Integer userId, Integer subAccountId, Pageable pageable);

    Transaction update(Integer id, Transaction transaction);
    void delete(Integer id);
}
