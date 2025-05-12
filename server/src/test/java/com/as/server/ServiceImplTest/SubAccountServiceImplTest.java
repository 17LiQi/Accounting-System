package com.as.server.ServiceImplTest;

import com.as.server.entity.SubAccount;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.SubAccountRepository;
import com.as.server.service.impl.SubAccountServiceImpl;
import com.as.server.enums.CardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubAccountServiceImplTest {

    @Mock
    private SubAccountRepository subAccountRepository;

    @InjectMocks
    private SubAccountServiceImpl subAccountService;

    private SubAccount subAccount;

    @BeforeEach
    void setUp() {
        subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        subAccount.setName("Savings Card");
        subAccount.setAccountNumber("1234-5678-9012-3456");
        subAccount.setBalance(BigDecimal.ZERO);
        subAccount.setCardType(CardType.SAVINGS);
    }

    /**
     * shouldSaveSubAccount
     */
    @Test
    void createSubAccount() {
        when(subAccountRepository.save(any(SubAccount.class))).thenReturn(subAccount);

        SubAccount result = subAccountService.create(subAccount);

        assertNotNull(result);
        assertEquals("Savings Card", result.getName());
        verify(subAccountRepository).save(subAccount);
    }

    /**
     * shouldReturnSubAccount
     */
    @Test
    void findSubAccountById() {
        when(subAccountRepository.findById(1)).thenReturn(Optional.of(subAccount));

        SubAccount result = subAccountService.findById(1);

        assertNotNull(result);
        assertEquals("Savings Card", result.getName());
        verify(subAccountRepository).findById(1);
    }

    /**
     * shouldThrowEntityNotFoundException
     */
    @Test
    void findSubAccountById_ThrowEntityNotFoundException() {
        when(subAccountRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> subAccountService.findById(1));

        assertEquals("SubAccount not found with id: 1", exception.getMessage());
        verify(subAccountRepository).findById(1);
    }

    /**
     * shouldReturnSubAccountList
     */
    @Test
    void findAllSubAccount() {
        when(subAccountRepository.findAll()).thenReturn(Collections.singletonList(subAccount));

        List<SubAccount> result = subAccountService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Savings Card", result.get(0).getName());
        verify(subAccountRepository).findAll();
    }

    /**
     * shouldUpdateSubAccount
     */
    @Test
    void updateSubAccount() {
        SubAccount updated = new SubAccount();
        updated.setName("Updated Card");
        updated.setBalance(BigDecimal.TEN);
        updated.setAccountNumber("9876-5432-1098-7654");


        when(subAccountRepository.findById(1)).thenReturn(Optional.of(subAccount));
        when(subAccountRepository.save(any(SubAccount.class))).thenReturn(subAccount);

        SubAccount result = subAccountService.update(1, updated);

        assertNotNull(result);
        assertEquals("Updated Card", result.getName());
        assertEquals(BigDecimal.TEN, result.getBalance());
        verify(subAccountRepository).findById(1);
        verify(subAccountRepository).save(subAccount);
    }

    /**
     * shouldDeleteSubAccount
     */
    @Test
    void deleteSubAccount() {
        when(subAccountRepository.existsById(1)).thenReturn(true);

        subAccountService.delete(1);

        verify(subAccountRepository).existsById(1);
        verify(subAccountRepository).deleteById(1);
    }

    /**
     * shouldThrowEntityNotFoundException
     */
    @Test
    void deleteSubAccount_ThrowEntityNotFoundException() {
        when(subAccountRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> subAccountService.delete(1));

        assertEquals("SubAccount not found with id: 1", exception.getMessage());
        verify(subAccountRepository).existsById(1);
        verify(subAccountRepository, never()).deleteById(1);
    }
}
