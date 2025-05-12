package com.as.server.ServiceImplTest;

import com.as.server.entity.TransactionType;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.TransactionTypeRepository;
import com.as.server.service.impl.TransactionTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionTypeServiceImplTest {

    @Mock
    private TransactionTypeRepository transactionTypeRepository;

    @InjectMocks
    private TransactionTypeServiceImpl transactionTypeService;

    private TransactionType transactionType;

    @BeforeEach
    void setUp() {
        transactionType = new TransactionType();
        transactionType.setTypeId(1);
        transactionType.setTypeName("Salary");
        transactionType.setIsIncome(true);
    }

    /**
     * shouldSaveTransactionType
     */
    @Test
    void createTransactionType() {
        when(transactionTypeRepository.save(any(TransactionType.class))).thenReturn(transactionType);

        TransactionType result = transactionTypeService.create(transactionType);

        assertNotNull(result);
        assertEquals("Salary", result.getTypeName());
        verify(transactionTypeRepository).save(transactionType);
    }

    /**
     * shouldReturnTransactionType
     */
    @Test
    void findTransactionTypeById() {
        when(transactionTypeRepository.findById(1)).thenReturn(Optional.of(transactionType));

        TransactionType result = transactionTypeService.findById(1);

        assertNotNull(result);
        assertEquals("Salary", result.getTypeName());
        verify(transactionTypeRepository).findById(1);
    }

    /**
     * shouldThrowEntityNotFoundException
     */
    @Test
    void findTransactionTypeById_ThrowEntityNotFoundException() {
        when(transactionTypeRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transactionTypeService.findById(1));

        assertEquals("TransactionType not found with id: 1", exception.getMessage());
        verify(transactionTypeRepository).findById(1);
    }

    /**
     * shouldReturnTransactionTypeList
     */
    @Test
    void findAllTransactionType() {
        when(transactionTypeRepository.findAll()).thenReturn(Collections.singletonList(transactionType));

        List<TransactionType> result = transactionTypeService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Salary", result.get(0).getTypeName());
        verify(transactionTypeRepository).findAll();
    }

    /**
     * shouldUpdateTransactionType
     */
    @Test
    void updateTransactionType() {
        TransactionType updated = new TransactionType();
        updated.setTypeName("Bonus");
        updated.setIsIncome(true);

        when(transactionTypeRepository.findById(1)).thenReturn(Optional.of(transactionType));
        when(transactionTypeRepository.save(any(TransactionType.class))).thenReturn(transactionType);

        TransactionType result = transactionTypeService.update(1, updated);

        assertNotNull(result);
        assertEquals("Bonus", result.getTypeName());
        verify(transactionTypeRepository).findById(1);
        verify(transactionTypeRepository).save(transactionType);
    }

    /**
     * shouldDeleteTransactionType
     */
    @Test
    void deleteTransactionType() {
        when(transactionTypeRepository.existsById(1)).thenReturn(true);

        transactionTypeService.delete(1);

        verify(transactionTypeRepository).existsById(1);
        verify(transactionTypeRepository).deleteById(1);
    }

    @Test
    void deleteTransactionType_shouldThrowEntityNotFoundException() {
        when(transactionTypeRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transactionTypeService.delete(1));

        assertEquals("TransactionType not found with id: 1", exception.getMessage());
        verify(transactionTypeRepository).existsById(1);
        verify(transactionTypeRepository, never()).deleteById(1);
    }
}
