package com.as.server.ServiceImplTest;

import com.as.server.entity.Account;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.repository.AccountRepository;
import com.as.server.service.impl.AccountServiceImpl;
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
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountId(1);
        account.setName("Personal Account");
    }

    /**
     * shouldSaveAccount
     */
    @Test
    void createAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.create(account);

        assertNotNull(result);
        assertEquals("Personal Account", result.getName());
        verify(accountRepository).save(account);
    }

    /**
     * shouldReturnAccount
     */
    @Test
    void findAccountById() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        Account result = accountService.findById(1);

        assertNotNull(result);
        assertEquals("Personal Account", result.getName());
        verify(accountRepository).findById(1);
    }

    @Test
    void findAccountById_ThrowEntityNotFoundException() {
        when(accountRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accountService.findById(1));

        assertEquals("Account not found with id: 1", exception.getMessage());
        verify(accountRepository).findById(1);
    }

    /**
     * shouldReturnAccountList
     */
    @Test
    void findAllAccount() {
        when(accountRepository.findAll()).thenReturn(Collections.singletonList(account));

        List<Account> result = accountService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Personal Account", result.get(0).getName());
        verify(accountRepository).findAll();
    }

    /**
     * shouldUpdateAccount
     */
    @Test
    void updateAccount() {
        Account updated = new Account();
        updated.setName("Updated Account");

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.update(1, updated);

        assertNotNull(result);
        assertEquals("Updated Account", result.getName());
        verify(accountRepository).findById(1);
        verify(accountRepository).save(account);
    }

    /**
     * shouldDeleteAccount
     */
    @Test
    void deleteAccount() {
        when(accountRepository.existsById(1)).thenReturn(true);

        accountService.delete(1);

        verify(accountRepository).existsById(1);
        verify(accountRepository).deleteById(1);
    }

    @Test
    void deleteAccount_ThrowEntityNotFoundException() {
        when(accountRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accountService.delete(1));

        assertEquals("Account not found with id: 1", exception.getMessage());
        verify(accountRepository).existsById(1);
        verify(accountRepository, never()).deleteById(1);
    }
}
