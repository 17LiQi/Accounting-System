package com.as.server.controller;

import com.as.server.dto.transactions.TransactionDTO;
import com.as.server.dto.transactions.TransactionListResponse;
import com.as.server.dto.transactions.TransactionRequest;
import com.as.server.entity.SubAccount;
import com.as.server.entity.Transaction;
import com.as.server.entity.User;
import com.as.server.exception.EntityNotFoundException;
import com.as.server.mapper.EntityMapper;
import com.as.server.repository.UserRepository;
import com.as.server.service.SubAccountService;
import com.as.server.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SubAccountService subAccountService;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TransactionDTO> create(@RequestBody TransactionRequest request) {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID format: " + userIdStr);
        }

        SubAccount subAccount = subAccountService.findById(request.getSubAccountId());
        if (subAccount.getUsers().stream().noneMatch(u -> u.getUserId().equals(userId)) &&
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("User not associated with sub-account");
        }

        Transaction transaction = entityMapper.toTransaction(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        transaction.setUser(user);
        Transaction created = transactionService.create(transaction);
        return ResponseEntity.status(201).body(entityMapper.toTransactionDTO(created));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TransactionListResponse> list(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer subAccountId,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && (userId != null && !userId.equals(Integer.parseInt(currentUserId)))) {
            throw new AccessDeniedException("Cannot access other user's transactions");
        }
        Page<Transaction> transactions = transactionService.findAll(
                isAdmin ? userId : Integer.parseInt(currentUserId),
                subAccountId,
                PageRequest.of(page, size));
        TransactionListResponse response = new TransactionListResponse();
        response.setTransactions(transactions.getContent().stream()
                .map(entityMapper::toTransactionDTO)
                .collect(Collectors.toList()));
        response.setTotal((int) transactions.getTotalElements());
        return ResponseEntity.ok(response);
    }
}
