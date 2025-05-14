package com.as.server.controller;

import com.as.server.dto.transactions.TransactionDTO;
import com.as.server.dto.transactions.TransactionListResponse;
import com.as.server.dto.transactions.TransactionRequest;
import com.as.server.entity.SubAccount;
import com.as.server.entity.Transaction;
import com.as.server.entity.TransactionType;
import com.as.server.entity.User;
import com.as.server.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionRequest request) {
        log.debug("Received create transaction request: {}", request);
        Authentication auth = getContext().getAuthentication();
        Integer userId = Integer.valueOf(auth.getName());
        Transaction transaction = mapRequestToEntity(request, userId);
        Transaction created = transactionService.create(transaction);
        TransactionDTO response = mapEntityToDTO(created);
        log.debug("Created transaction: {}", response);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TransactionListResponse> getTransactions(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer subAccountId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Received get transactions request: userId={}, subAccountId={}, page={}, size={}",
                userId, subAccountId, page, size);
        Authentication auth = getContext().getAuthentication();
        Integer effectiveUserId = userId != null ? userId : Integer.valueOf(auth.getName());
        if (userId != null && !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !userId.equals(Integer.valueOf(auth.getName()))) {
            log.warn("User {} attempted to access transactions of user {}", auth.getName(), userId);
            throw new AccessDeniedException("Cannot access other user's transactions");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionPage = transactionService.findAll(effectiveUserId, subAccountId, pageable);
        TransactionListResponse response = new TransactionListResponse()
                .transactions(transactionPage.getContent().stream().map(this::mapEntityToDTO).collect(Collectors.toList()))
                .total((int) transactionPage.getTotalElements());
        log.debug("Returning {} transactions", response.getTransactions().size());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Integer id) {
        log.debug("Received get transaction request: id={}", id);
        Authentication auth = getContext().getAuthentication();
        Transaction transaction = transactionService.findById(id);
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !transaction.getUser().getUserId().equals(Integer.valueOf(auth.getName()))) {
            log.warn("User {} attempted to access transaction {} of another user", auth.getName(), id);
            throw new AccessDeniedException("Cannot access other user's transaction");
        }
        TransactionDTO response = mapEntityToDTO(transaction);
        log.debug("Returning transaction: {}", response);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Integer id, @RequestBody TransactionRequest request) {
        log.debug("Received update transaction request: id={}, request={}", id, request);
        Authentication auth = getContext().getAuthentication();
        Integer userId = Integer.valueOf(auth.getName());

        // Fetch existing transaction to check ownership
        Transaction existing = transactionService.findById(id);
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !existing.getUser().getUserId().equals(userId)) {
            log.warn("User {} attempted to update transaction {} of another user", auth.getName(), id);
            throw new AccessDeniedException("Cannot update other user's transaction");
        }

        Transaction transaction = mapRequestToEntity(request, userId);
        transaction.setTransactionId(id); // Ensure ID is set for update
        Transaction updated = transactionService.update(id, transaction);
        TransactionDTO response = mapEntityToDTO(updated);
        log.debug("Updated transaction: {}", response);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer id) {
        log.debug("Received delete transaction request: id={}", id);
        Authentication auth = getContext().getAuthentication();
        Transaction transaction = transactionService.findById(id);
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !transaction.getUser().getUserId().equals(Integer.valueOf(auth.getName()))) {
            log.warn("User {} attempted to delete transaction {} of another user", auth.getName(), id);
            throw new AccessDeniedException("Cannot delete other user's transaction");
        }
        transactionService.delete(id);
        log.debug("Deleted transaction: id={}", id);
        return ResponseEntity.noContent().build();
    }

    private Transaction mapRequestToEntity(TransactionRequest request, Integer userId) {
        if (request == null) {
            throw new IllegalArgumentException("Transaction request cannot be null");
        }
        Transaction transaction = new Transaction();
        transaction.setTime(request.getTime() != null ? request.getTime().toLocalDateTime() : LocalDateTime.now());
        TransactionType type = new TransactionType();
        type.setTypeId(request.getTypeId() != null ? request.getTypeId() : 0); // Default to 0 if null
        transaction.setTransactionType(type);
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(request.getSubAccountId() != null ? request.getSubAccountId() : 0); // Default to 0
        transaction.setSubAccount(subAccount);
        transaction.setAmount(request.getAmount() != null ? new BigDecimal(request.getAmount()) : BigDecimal.ZERO);
        User user = new User();
        user.setUserId(userId != null ? userId : 0); // Default to 0 if null
        transaction.setUser(user);
        transaction.setRemarks(request.getRemarks());
        return transaction;
    }

    private TransactionDTO mapEntityToDTO(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setTime(transaction.getTime() != null ? transaction.getTime().atOffset(ZoneOffset.UTC) : null);
        dto.setTypeId(transaction.getTransactionType() != null ? transaction.getTransactionType().getTypeId() : null);
        dto.setIsIncome(transaction.getTransactionType() != null ? transaction.getTransactionType().getIsIncome() : false);
        dto.setSubAccountId(transaction.getSubAccount() != null ? transaction.getSubAccount().getSubAccountId() : null);
        dto.setAmount(transaction.getAmount() != null ? transaction.getAmount().toString() : null);
        dto.setUserId(transaction.getUser() != null ? transaction.getUser().getUserId() : null);
        dto.setRemarks(transaction.getRemarks());
        return dto;
    }
}