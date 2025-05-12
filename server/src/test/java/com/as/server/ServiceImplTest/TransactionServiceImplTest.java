package com.as.server.ServiceImplTest;

import com.as.server.entity.Transaction;
import com.as.server.entity.TransactionType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.TransactionRepository;
import com.as.server.repository.TransactionTypeRepository;
import com.as.server.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionTypeRepository transactionTypeRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTime(LocalDateTime.now());
        transaction.setAmount(BigDecimal.valueOf(100.00));
        transaction.setRemarks("Test transaction");


    }

    /**
     * shouldSaveTransaction
     */
    @Test
    void createTransaction() {
        TransactionType type = new TransactionType();
        type.setTypeId(1);  // 这里的 ID 要和 mocked repository 返回值匹配
        transaction.setTransactionType(type);

        when(transactionTypeRepository.findById(1)).thenReturn(Optional.of(type));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.create(transaction);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100.00), result.getAmount());
        verify(transactionRepository).save(transaction);
    }

    /**
     * shouldReturnTransaction
     */
    @Test
    void findTransactionById() {
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.findById(1);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100.00), result.getAmount());
        verify(transactionRepository).findById(1);
    }

    @Test
    void findTransactionById_ThrowEntityNotFoundException() {
        when(transactionRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transactionService.findById(1));

        assertEquals("Transaction not found with id: 1", exception.getMessage());
        verify(transactionRepository).findById(1);
    }

    /**
     * shouldReturnTransactionList
     */
    @Test
    void findAllTransaction() {
        Integer userId = 1;
        Integer subAccountId = 1;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> mockPage = new PageImpl<>(Collections.singletonList(transaction));

        when(transactionRepository.findByUserUserIdAndSubAccountSubAccountId(eq(userId), eq(subAccountId), eq(pageable)))
                .thenReturn(mockPage);

        Page<Transaction> result = transactionService.findAll(userId, subAccountId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(BigDecimal.valueOf(100.00), result.getContent().get(0).getAmount());
        verify(transactionRepository).findByUserUserIdAndSubAccountSubAccountId(eq(userId), eq(subAccountId), eq(pageable));
    }


    /**
     * shouldUpdateTransaction
     */
    @Test
    void updateTransaction() {
        Transaction updated = new Transaction();
        updated.setAmount(BigDecimal.valueOf(200.00));
        updated.setRemarks("Updated transaction");

        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.update(1, updated);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(200.00), result.getAmount());
        assertEquals("Updated transaction", result.getRemarks());
        verify(transactionRepository).findById(1);
        verify(transactionRepository).save(transaction);
    }

    /**
     * shouldDeleteTransaction
     */
    @Test
    void deleteTransaction() {
        when(transactionRepository.existsById(1)).thenReturn(true);

        transactionService.delete(1);

        verify(transactionRepository).existsById(1);
        verify(transactionRepository).deleteById(1);
    }

    @Test
    void deleteTransaction_ThrowEntityNotFoundException() {
        when(transactionRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transactionService.delete(1));

        assertEquals("Transaction not found with id: 1", exception.getMessage());
        verify(transactionRepository).existsById(1);
        verify(transactionRepository, never()).deleteById(1);
    }
}
