package com.as.server.controller;

import com.as.server.dto.accounts.SubAccountDTO;
import com.as.server.dto.accounts.SubAccountRequest;
import com.as.server.entity.SubAccount;
import com.as.server.exception.ConflictException;
import com.as.server.mapper.EntityMapper;
import com.as.server.service.SubAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sub-accounts")
@PreAuthorize("hasRole('ADMIN')")
public class SubAccountController {

    @Autowired
    private SubAccountService subAccountService;

    @Autowired
    private EntityMapper entityMapper;

    @PostMapping
    public ResponseEntity<SubAccountDTO> create(@RequestBody SubAccountRequest request) {
        SubAccount subAccount = entityMapper.toSubAccount(request);
        SubAccount created = subAccountService.create(subAccount);
        return ResponseEntity.status(201).body(entityMapper.toSubAccountDTO(created));
    }

    @PutMapping("/{subAccountId}")
    public ResponseEntity<SubAccountDTO> update(@PathVariable Integer subAccountId, @RequestBody SubAccountRequest request) {
        SubAccount subAccount = entityMapper.toSubAccount(request);
        SubAccount updated = subAccountService.update(subAccountId, subAccount);
        return ResponseEntity.ok(entityMapper.toSubAccountDTO(updated));
    }

    @DeleteMapping("/{subAccountId}")
    public ResponseEntity<Void> delete(@PathVariable Integer subAccountId) {
        if (subAccountService.hasTransactions(subAccountId)) {
            throw new ConflictException("Sub-account has associated transactions");
        }
        subAccountService.delete(subAccountId);
        return ResponseEntity.noContent().build();
    }
}
