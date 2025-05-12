package com.as.server.service;

import com.as.server.entity.TransactionType;

import java.util.List;

public interface TransactionTypeService {
    TransactionType create(TransactionType transactionType);
    TransactionType findById(Integer id);
    List<TransactionType> findAll();
    TransactionType update(Integer id, TransactionType transactionType);
    void delete(Integer id);

    boolean hasTransactions(Integer typeId);
}
