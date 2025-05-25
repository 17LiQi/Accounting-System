package com.as.server.controller;

import com.as.server.api.subaccounts.SubAccountsApi;
import com.as.server.dto.accounts.SubAccountDTO;
import com.as.server.dto.accounts.SubAccountRequest;
import com.as.server.entity.Account;
import com.as.server.entity.SubAccount;
import com.as.server.entity.User;
import com.as.server.enums.CardType;
import com.as.server.mapper.EntityMapper;
import com.as.server.service.SubAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RestController
@RequestMapping("/sub-accounts")
public class SubAccountController implements SubAccountsApi {

    private static final Logger log = LoggerFactory.getLogger(SubAccountController.class);
    private final SubAccountService subAccountService;
    private final EntityMapper entityMapper;

    public SubAccountController(SubAccountService subAccountService, EntityMapper entityMapper) {
        this.subAccountService = subAccountService;
        this.entityMapper = entityMapper;
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<SubAccountDTO> subAccountsCreate(@Valid @RequestBody SubAccountRequest request) {
        log.debug("Received create sub-account request: {}", request);
        Authentication auth = getContext().getAuthentication();
        Integer userId = Integer.valueOf(auth.getName());
        SubAccount subAccount = mapRequestToEntity(request, userId);
        SubAccount created = subAccountService.create(subAccount);
        SubAccountDTO response = entityMapper.toSubAccountDTO(created);
        log.debug("Created sub-account: {}", response);
        return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<SubAccountDTO>> subAccountsGet() {
        log.debug("Received get sub-accounts request");
        Authentication auth = getContext().getAuthentication();
        Integer userId = Integer.valueOf(auth.getName());
        List<SubAccount> subAccounts = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                ? subAccountService.findAll()
                : subAccountService.findSubAccountsByUserId(userId);
        List<SubAccountDTO> response = subAccounts.stream()
                .map(entityMapper::toSubAccountDTO)
                .collect(Collectors.toList());
        log.debug("Returning {} sub-accounts", response.size());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<SubAccountDTO> subAccountGet(@PathVariable Integer id) {
        log.debug("Received get sub-account request: id={}", id);
        Authentication auth = getContext().getAuthentication();
        Integer userId = Integer.valueOf(auth.getName());
        SubAccount subAccount = subAccountService.findById(id);
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) &&
                subAccount.getUsers().stream().noneMatch(u -> u.getUserId().equals(userId))) {
            log.warn("User {} attempted to access sub-account {} without permission", userId, id);
            throw new AccessDeniedException("Cannot access sub-account");
        }
        SubAccountDTO response = entityMapper.toSubAccountDTO(subAccount);
        log.debug("Returning sub-account: {}", response);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<SubAccountDTO> subAccountsUpdate(@PathVariable Integer id, @Valid @RequestBody SubAccountRequest request) {
        log.debug("Received update sub-account request: id={}, request={}", id, request);
        Authentication auth = getContext().getAuthentication();
        Integer userId = Integer.valueOf(auth.getName());
        SubAccount existing = subAccountService.findById(id);
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) &&
                existing.getUsers().stream().noneMatch(u -> u.getUserId().equals(userId))) {
            log.warn("User {} attempted to update sub-account {} without permission", userId, id);
            throw new AccessDeniedException("Cannot update sub-account");
        }
        SubAccount subAccount = mapRequestToEntity(request, userId);
        subAccount.setSubAccountId(id);
        SubAccount updated = subAccountService.update(id, subAccount);
        SubAccountDTO response = entityMapper.toSubAccountDTO(updated);
        log.debug("Updated sub-account: {}", response);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> subAccountsDelete(@PathVariable Integer id) {
        log.debug("Received delete sub-account request: id={}", id);
        Authentication auth = getContext().getAuthentication();
        Integer userId = Integer.valueOf(auth.getName());
        SubAccount subAccount = subAccountService.findById(id);
        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) &&
                subAccount.getUsers().stream().noneMatch(u -> u.getUserId().equals(userId))) {
            log.warn("User {} attempted to delete sub-account {} without permission", userId, id);
            throw new AccessDeniedException("Cannot delete sub-account");
        }
        if (subAccountService.hasTransactions(id)) {
            log.warn("Sub-account {} has associated transactions and cannot be deleted", id);
            throw new IllegalStateException("Sub-account has associated transactions");
        }
        subAccountService.delete(id);
        log.debug("Deleted sub-account: id={}", id);
        return ResponseEntity.noContent().build();
    }

    private SubAccount mapRequestToEntity(SubAccountRequest request, Integer userId) {
        if (request == null) {
            throw new IllegalArgumentException("Sub-account request cannot be null");
        }
        SubAccount subAccount = new SubAccount();
        subAccount.setAccountName(request.getAccountName());
        subAccount.setAccountNumber(request.getAccountNumber());
        subAccount.setCardType(request.getCardType() != null ? request.getCardType() : CardType.VISA);
        subAccount.setBalance(request.getBalance() != null ? new BigDecimal(request.getBalance()) : BigDecimal.ZERO);
        Account account = new Account();
        account.setAccountId(request.getAccountId() != null ? request.getAccountId() : 0);
        subAccount.setAccount(account);

        Authentication auth = getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin && request.getUserIds() != null && !request.getUserIds().isEmpty()) {
            request.getUserIds().forEach(id -> {
                User user = new User();
                user.setUserId(id);
                subAccount.addUser(user);
            });
        } else {
            User user = new User();
            user.setUserId(userId);
            subAccount.addUser(user);
        }

        return subAccount;
    }
}