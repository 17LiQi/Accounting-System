package com.as.server.service.impl;

import com.as.server.entity.TransactionType;
import com.as.server.repository.TransactionTypeRepository;
import com.as.server.service.TransactionTypeService;
import com.as.server.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private final TransactionTypeRepository transactionTypeRepository;

    @Override
    @Transactional
    public TransactionType create(TransactionType transactionType) {
        log.info("Creating transaction type: {}", transactionType.getTypeName());
        return transactionTypeRepository.save(transactionType);
    }

    @Override
    public TransactionType findById(Integer id) {
        log.info("Finding transaction type by id: {}", id);
        return transactionTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TransactionType not found with id: " + id));
    }

    @Override
    public List<TransactionType> findAll() {
        log.info("Finding all transaction types");
        return transactionTypeRepository.findAll();
    }

    @Override
    @Transactional
    public TransactionType update(Integer id, TransactionType transactionType) {
        log.info("Updating transaction type with id: {}", id);
        TransactionType existing = findById(id);
        existing.setTypeName(transactionType.getTypeName());
        existing.setIsIncome(transactionType.getIsIncome());
        return transactionTypeRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Deleting transaction type with id: {}", id);
        if (!transactionTypeRepository.existsById(id)) {
            throw new EntityNotFoundException("TransactionType not found with id: " + id);
        }
        transactionTypeRepository.deleteById(id);
    }

    @Override
    public boolean hasTransactions(Integer typeId) {
        return false;
    }
}
