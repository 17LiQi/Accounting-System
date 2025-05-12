package com.as.server.controller;

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
public class AccountController {

    private final AccountService accountService;
    private final EntityMapper entityMapper;
    private final AccountTypeRepository accountTypeRepository;

    public AccountController(AccountService accountService, EntityMapper entityMapper, AccountTypeRepository accountTypeRepository) {
        this.accountService = accountService;
        this.entityMapper = entityMapper;
        this.accountTypeRepository = accountTypeRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDTO> create(@Valid @RequestBody AccountRequest request) {
        Account account = entityMapper.toAccount(request, accountTypeRepository);
        Account createdAccount = accountService.create(account);
        AccountDTO accountDTO = entityMapper.toAccountDTO(createdAccount);
        return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AccountDTO>> list() {
        List<Account> accounts = accountService.findAll();
        List<AccountDTO> accountDTOs = accounts.stream()
                .map(entityMapper::toAccountDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accountDTOs);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountDTO> update(@PathVariable Integer id, @Valid @RequestBody AccountRequest request) {
        Account account = entityMapper.toAccount(request, accountTypeRepository);
        Account updatedAccount = accountService.update(id, account);
        AccountDTO accountDTO = entityMapper.toAccountDTO(updatedAccount);
        return ResponseEntity.ok(accountDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (accountService.hasSubAccounts(id)) {
            throw new IllegalStateException("Account has associated sub-accounts");
        }
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}