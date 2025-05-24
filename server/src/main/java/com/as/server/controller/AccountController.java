package com.as.server.controller;

import com.as.server.api.accounts.AccountsApi;
import com.as.server.dto.accounts.AccountDTO;
import com.as.server.dto.accounts.AccountRequest;
import com.as.server.entity.Account;
import com.as.server.mapper.EntityMapper;
import com.as.server.repository.AccountTypeRepository;
import com.as.server.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
public class AccountController implements AccountsApi {

    private final AccountService accountService;
    private final EntityMapper entityMapper;
    private final AccountTypeRepository accountTypeRepository;

    public AccountController(AccountService accountService, EntityMapper entityMapper, AccountTypeRepository accountTypeRepository) {
        this.accountService = accountService;
        this.entityMapper = entityMapper;
        this.accountTypeRepository = accountTypeRepository;
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDTO> accountsCreate(@Valid @RequestBody AccountRequest request) {
        Account account = entityMapper.toAccount(request, accountTypeRepository);
        Account createdAccount = accountService.create(account);
        AccountDTO accountDTO = entityMapper.toAccountDTO(createdAccount);
        return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<AccountDTO>> accountsList() {
        List<Account> accounts = accountService.findAll();
        List<AccountDTO> accountDTOs = accounts.stream()
                .map(entityMapper::toAccountDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accountDTOs);
    }

    @Override
    @GetMapping("/{accountId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<AccountDTO> accountsGet(@PathVariable("accountId") Integer accountId) {
        Account account = accountService.findById(accountId);
        AccountDTO accountDTO = entityMapper.toAccountDTO(account);
        return ResponseEntity.ok(accountDTO);
    }

    @Override
    @PutMapping("/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDTO> accountsUpdate(@PathVariable("accountId") Integer accountId, @Valid @RequestBody AccountRequest request) {
        Account account = entityMapper.toAccount(request, accountTypeRepository);
        Account updatedAccount = accountService.update(accountId, account);
        AccountDTO accountDTO = entityMapper.toAccountDTO(updatedAccount);
        return ResponseEntity.ok(accountDTO);
    }

    @Override
    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> accountsDelete(@PathVariable("accountId") Integer accountId) {
        if (accountService.hasSubAccounts(accountId)) {
            throw new IllegalStateException("Account has associated sub-accounts");
        }
        accountService.delete(accountId);
        return ResponseEntity.noContent().build();
    }
}