package com.as.server.controller;

import com.as.server.api.accounts.AccountTypesApi;
import com.as.server.dto.accounts.AccountTypeDTO;
import com.as.server.dto.accounts.AccountTypeRequest;
import com.as.server.entity.AccountType;
import com.as.server.exception.ConflictException;
import com.as.server.mapper.EntityMapper;
import com.as.server.service.AccountTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account-types")
public class AccountTypeController implements AccountTypesApi {

    private final AccountTypeService accountTypeService;
    private final EntityMapper entityMapper;

    public AccountTypeController(AccountTypeService accountTypeService, EntityMapper entityMapper) {
        this.accountTypeService = accountTypeService;
        this.entityMapper = entityMapper;
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<AccountTypeDTO>> accountTypesList() {
        List<AccountType> types = accountTypeService.findAll();
        List<AccountTypeDTO> typeDTOs = types.stream()
                .map(entityMapper::toAccountTypeDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(typeDTOs);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountTypeDTO> accountTypesCreate(@Valid @RequestBody AccountTypeRequest request) {
        AccountType accountType = entityMapper.toAccountType(request);
        AccountType created = accountTypeService.create(accountType);
        return ResponseEntity.status(201).body(entityMapper.toAccountTypeDTO(created));
    }

    @Override
    @GetMapping("/{typeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<AccountTypeDTO> accountTypesGet(@PathVariable Integer typeId) {
        AccountType accountType = accountTypeService.findById(typeId);
        return ResponseEntity.ok(entityMapper.toAccountTypeDTO(accountType));
    }

    @Override
    @PutMapping("/{typeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountTypeDTO> accountTypesUpdate(@PathVariable Integer typeId, @Valid @RequestBody AccountTypeRequest request) {
        AccountType accountType = entityMapper.toAccountType(request);
        AccountType updated = accountTypeService.update(typeId, accountType);
        return ResponseEntity.ok(entityMapper.toAccountTypeDTO(updated));
    }

    @Override
    @DeleteMapping("/{typeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> accountTypesDelete(@PathVariable Integer typeId) {
        if (accountTypeService.hasTransactions(typeId)) {
            throw new ConflictException("Account type has associated accounts");
        }
        accountTypeService.delete(typeId);
        return ResponseEntity.noContent().build();
    }
} 