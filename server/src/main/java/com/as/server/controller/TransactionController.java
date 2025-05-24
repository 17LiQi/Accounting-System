package com.as.server.controller;

import com.as.server.api.transactions.TransactionsApi;
import com.as.server.dto.transactions.TransactionDTO;
import com.as.server.dto.transactions.TransactionListResponse;
import com.as.server.dto.transactions.TransactionRequest;
import com.as.server.entity.SubAccount;
import com.as.server.entity.Transaction;
import com.as.server.entity.TransactionType;
import com.as.server.entity.User;
import com.as.server.mapper.EntityMapper;
import com.as.server.service.TransactionService;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;


@Validated
@RestController
@RequestMapping("/transactions")
public class TransactionController implements TransactionsApi {

    private final EntityMapper entityMapper;

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    public TransactionController(EntityMapper entityMapper, TransactionService transactionService) {
        this.entityMapper = entityMapper;
        this.transactionService = transactionService;
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TransactionDTO> transactionsCreate(@RequestBody TransactionRequest request) {
        log.info("收到创建交易请求: 时间={}, 类型ID={}, 子账号ID={}, 金额={}, 备注={}", 
            request.getTime(), request.getTypeId(), request.getSubAccountId(), request.getAmount(), request.getRemarks());
        Authentication auth = getContext().getAuthentication();
        Integer userId = Integer.valueOf(auth.getName());
        log.debug("当前用户ID: {}", userId);
        
        Transaction transaction = mapRequestToEntity(request, userId);
        log.debug("映射后的交易实体: {}", transaction);
        
        Transaction created = transactionService.create(transaction);
        log.info("交易创建成功: ID={}, 时间={}, 金额={}", 
            created.getTransactionId(), created.getTime(), created.getAmount());
        
        TransactionDTO response = mapEntityToDTO(created);
        log.debug("返回的交易DTO: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TransactionListResponse> transactionsList(
            @Parameter(description = "用户ID") @Valid @RequestParam(value = "userId", required = false) Integer userId,
            @Parameter(description = "子账户ID") @Valid @RequestParam(value = "subAccountId", required = false) Integer subAccountId,
            @Parameter(description = "页码（从0开始）") @Valid @RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) Integer page,
            @Parameter(description = "每页大小") @Valid @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) @Max(100) Integer size,
            @Parameter(description = "排序方式（desc: 降序，asc: 升序）") @Valid @RequestParam(value = "sort", required = false, defaultValue = "desc") String sort) {
        Authentication auth = getContext().getAuthentication();
        Integer currentUserId = null;
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            currentUserId = Integer.valueOf(auth.getName());
            if (userId != null && !userId.equals(currentUserId)) {
                throw new AccessDeniedException("Users can only view their own transactions");
            }
            userId = currentUserId;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "time"));
        Page<Transaction> transactions = transactionService.findAll(userId, subAccountId, pageable);
        return ResponseEntity.ok(entityMapper.toTransactionListResponse(transactions));
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TransactionDTO> transactionGet(@PathVariable Integer id) {
        log.info("收到获取交易详情请求: ID={}", id);
        Authentication auth = getContext().getAuthentication();
        log.debug("当前认证用户: {}", auth.getName());
        
        Transaction transaction = transactionService.findById(id);
        log.debug("查询到的交易: {}", transaction);
        
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !transaction.getUser().getUserId().equals(Integer.valueOf(auth.getName()))) {
            log.warn("用户 {} 尝试访问用户 {} 的交易记录", auth.getName(), transaction.getUser().getUserId());
            throw new AccessDeniedException("Cannot access other user's transaction");
        }
        
        TransactionDTO response = mapEntityToDTO(transaction);
        log.info("返回交易详情: ID={}, 时间={}, 金额={}", 
            response.getTransactionId(), response.getTime(), response.getAmount());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TransactionDTO> transactionUpdate(@PathVariable Integer id, @RequestBody TransactionRequest request) {
        log.info("收到更新交易请求: ID={}, 时间={}, 类型ID={}, 子账号ID={}, 金额={}, 备注={}", 
            id, request.getTime(), request.getTypeId(), request.getSubAccountId(), request.getAmount(), request.getRemarks());
        Authentication auth = getContext().getAuthentication();
        Integer userId = Integer.valueOf(auth.getName());
        log.debug("当前用户ID: {}", userId);

        // 获取现有交易以检查所有权
        Transaction existing = transactionService.findById(id);
        log.debug("现有交易: {}", existing);
        
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !existing.getUser().getUserId().equals(userId)) {
            log.warn("用户 {} 尝试更新用户 {} 的交易记录", auth.getName(), existing.getUser().getUserId());
            throw new AccessDeniedException("Cannot update other user's transaction");
        }

        Transaction transaction = mapRequestToEntity(request, userId);
        transaction.setTransactionId(id); // 确保ID被设置用于更新
        log.debug("更新后的交易实体: {}", transaction);
        
        Transaction updated = transactionService.update(id, transaction);
        log.info("交易更新成功: ID={}, 时间={}, 金额={}", 
            updated.getTransactionId(), updated.getTime(), updated.getAmount());
        
        TransactionDTO response = mapEntityToDTO(updated);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> transactionDelete(@PathVariable Integer id) {
        log.info("收到删除交易请求: ID={}", id);
        Authentication auth = getContext().getAuthentication();
        log.debug("当前认证用户: {}", auth.getName());
        
        Transaction transaction = transactionService.findById(id);
        log.debug("要删除的交易: {}", transaction);
        
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !transaction.getUser().getUserId().equals(Integer.valueOf(auth.getName()))) {
            log.warn("用户 {} 尝试删除用户 {} 的交易记录", auth.getName(), transaction.getUser().getUserId());
            throw new AccessDeniedException("Cannot delete other user's transaction");
        }
        
        transactionService.delete(id);
        log.info("交易删除成功: ID={}", id);
        return ResponseEntity.noContent().build();
    }

    private Transaction mapRequestToEntity(TransactionRequest request, Integer userId) {
        if (request == null) {
            throw new IllegalArgumentException("Transaction request cannot be null");
        }
        Transaction transaction = new Transaction();
        transaction.setTime(request.getTime() != null ? 
            request.getTime().withOffsetSameInstant(ZoneOffset.ofHours(8)).toLocalDateTime() : 
            LocalDateTime.now(ZoneOffset.ofHours(8)));
        TransactionType type = new TransactionType();
        type.setTypeId(request.getTypeId() != null ? request.getTypeId() : 0);
        transaction.setTransactionType(type);
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(request.getSubAccountId() != null ? request.getSubAccountId() : 0);
        transaction.setSubAccount(subAccount);
        transaction.setAmount(request.getAmount() != null ? new BigDecimal(request.getAmount()) : BigDecimal.ZERO);
        User user = new User();
        user.setUserId(userId != null ? userId : 0);
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
        dto.setTime(transaction.getTime() != null ? 
            transaction.getTime().atOffset(ZoneOffset.ofHours(8)).withOffsetSameInstant(ZoneOffset.UTC) : 
            null);
        dto.setTypeId(transaction.getTransactionType() != null ? transaction.getTransactionType().getTypeId() : null);
        dto.setIsIncome(transaction.getTransactionType() != null ? transaction.getTransactionType().getIsIncome() : false);
        dto.setSubAccountId(transaction.getSubAccount() != null ? transaction.getSubAccount().getSubAccountId() : null);
        dto.setAmount(transaction.getAmount() != null ? transaction.getAmount().toString() : null);
        dto.setUserId(transaction.getUser() != null ? transaction.getUser().getUserId() : null);
        dto.setRemarks(transaction.getRemarks());
        return dto;
    }
}