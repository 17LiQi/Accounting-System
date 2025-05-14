package com.as.server.controller;

import com.as.server.dto.transactions.TransactionRequest;
import com.as.server.entity.SubAccount;
import com.as.server.entity.Transaction;
import com.as.server.entity.TransactionType;
import com.as.server.entity.User;
import com.as.server.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    private static final Logger log = LoggerFactory.getLogger(TransactionControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void createTransaction_user_success() throws Exception {
        log.debug("Setting up createTransaction_user_success");
        TransactionRequest request = new TransactionRequest()
                .time(OffsetDateTime.of(2025, 5, 13, 10, 0, 0, 0, ZoneOffset.UTC))
                .typeId(1)
                .subAccountId(1)
                .amount("100.00")
                .remarks("Grocery purchase");

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTime(LocalDateTime.of(2025, 5, 13, 10, 0));
        TransactionType type = new TransactionType();
        type.setTypeId(1);
        type.setIsIncome(false);
        transaction.setTransactionType(type);
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        transaction.setSubAccount(subAccount);
        transaction.setAmount(new BigDecimal("100.00"));
        User user = new User();
        user.setUserId(1);
        transaction.setUser(user);
        transaction.setRemarks("Grocery purchase");

        when(transactionService.create(any(Transaction.class))).thenReturn(transaction);

        log.debug("Executing createTransaction_user_success request");
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.time").value("2025-05-13T10:00:00Z"))
                .andExpect(jsonPath("$.typeId").value(1))
                .andExpect(jsonPath("$.isIncome").value(false))
                .andExpect(jsonPath("$.subAccountId").value(1))
                .andExpect(jsonPath("$.amount").value("100.00"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.remarks").value("Grocery purchase"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getTransactions_user_success() throws Exception {
        log.debug("Setting up getTransactions_user_success");
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTime(LocalDateTime.of(2025, 5, 13, 10, 0));
        TransactionType type = new TransactionType();
        type.setTypeId(1);
        type.setIsIncome(false);
        transaction.setTransactionType(type);
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        transaction.setSubAccount(subAccount);
        transaction.setAmount(new BigDecimal("100.00"));
        User user = new User();
        user.setUserId(1);
        transaction.setUser(user);
        transaction.setRemarks("Grocery purchase");

        List<Transaction> transactions = Collections.singletonList(transaction);
        Page<Transaction> page = new PageImpl<>(transactions, PageRequest.of(0, 10), 1);

        when(transactionService.findAll(eq(1), nullable(Integer.class), any(Pageable.class))).thenReturn(page);

        log.debug("Executing getTransactions_user_success request");
        mockMvc.perform(get("/transactions")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactions[0].transactionId").value(1))
                .andExpect(jsonPath("$.transactions[0].time").value("2025-05-13T10:00:00Z"))
                .andExpect(jsonPath("$.transactions[0].typeId").value(1))
                .andExpect(jsonPath("$.transactions[0].isIncome").value(false))
                .andExpect(jsonPath("$.transactions[0].subAccountId").value(1))
                .andExpect(jsonPath("$.transactions[0].amount").value("100.00"))
                .andExpect(jsonPath("$.transactions[0].userId").value(1))
                .andExpect(jsonPath("$.transactions[0].remarks").value("Grocery purchase"))
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getTransactions_user_otherUser_forbidden() throws Exception {
        log.debug("Setting up getTransactions_user_otherUser_forbidden");
        mockMvc.perform(get("/transactions")
                        .param("userId", "2")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Cannot access other user's transactions"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getTransaction_user_success() throws Exception {
        log.debug("Setting up getTransaction_user_success");
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTime(LocalDateTime.of(2025, 5, 13, 10, 0));
        TransactionType type = new TransactionType();
        type.setTypeId(1);
        type.setIsIncome(false);
        transaction.setTransactionType(type);
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        transaction.setSubAccount(subAccount);
        transaction.setAmount(new BigDecimal("100.00"));
        User user = new User();
        user.setUserId(1);
        transaction.setUser(user);
        transaction.setRemarks("Grocery purchase");

        when(transactionService.findById(eq(1))).thenReturn(transaction);

        log.debug("Executing getTransaction_user_success request");
        mockMvc.perform(get("/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.time").value("2025-05-13T10:00:00Z"))
                .andExpect(jsonPath("$.typeId").value(1))
                .andExpect(jsonPath("$.isIncome").value(false))
                .andExpect(jsonPath("$.subAccountId").value(1))
                .andExpect(jsonPath("$.amount").value("100.00"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.remarks").value("Grocery purchase"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getTransaction_user_otherUser_forbidden() throws Exception {
        log.debug("Setting up getTransaction_user_otherUser_forbidden");
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTime(LocalDateTime.of(2025, 5, 13, 10, 0));
        TransactionType type = new TransactionType();
        type.setTypeId(1);
        type.setIsIncome(false);
        transaction.setTransactionType(type);
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        transaction.setSubAccount(subAccount);
        transaction.setAmount(new BigDecimal("100.00"));
        User user = new User();
        user.setUserId(2); // Other user
        transaction.setUser(user);
        transaction.setRemarks("Grocery purchase");

        when(transactionService.findById(eq(1))).thenReturn(transaction);

        log.debug("Executing getTransaction_user_otherUser_forbidden request");
        mockMvc.perform(get("/transactions/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Cannot access other user's transaction"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void updateTransaction_user_success() throws Exception {
        log.debug("Setting up updateTransaction_user_success");
        TransactionRequest request = new TransactionRequest()
                .time(OffsetDateTime.of(2025, 5, 13, 10, 0, 0, 0, ZoneOffset.UTC))
                .typeId(1)
                .subAccountId(1)
                .amount("150.00")
                .remarks("Updated grocery purchase");

        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTime(LocalDateTime.of(2025, 5, 13, 10, 0));
        TransactionType type = new TransactionType();
        type.setTypeId(1);
        type.setIsIncome(false);
        transaction.setTransactionType(type);
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        transaction.setSubAccount(subAccount);
        transaction.setAmount(new BigDecimal("150.00"));
        User user = new User();
        user.setUserId(1);
        transaction.setUser(user);
        transaction.setRemarks("Updated grocery purchase");

        when(transactionService.findById(1)).thenReturn(transaction);
        when(transactionService.update(eq(1), any(Transaction.class))).thenReturn(transaction);

        log.debug("Executing updateTransaction_user_success request");
        mockMvc.perform(put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.time").value("2025-05-13T10:00:00Z"))
                .andExpect(jsonPath("$.typeId").value(1))
                .andExpect(jsonPath("$.isIncome").value(false))
                .andExpect(jsonPath("$.subAccountId").value(1))
                .andExpect(jsonPath("$.amount").value("150.00"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.remarks").value("Updated grocery purchase"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void updateTransaction_user_otherUser_forbidden() throws Exception {
        log.debug("Setting up updateTransaction_user_otherUser_forbidden");
        TransactionRequest request = new TransactionRequest()
                .time(OffsetDateTime.of(2025, 5, 13, 10, 0, 0, 0, ZoneOffset.UTC))
                .typeId(1)
                .subAccountId(1)
                .amount("150.00")
                .remarks("Updated grocery purchase");

        Transaction existing = new Transaction();
        existing.setTransactionId(1);
        existing.setTime(LocalDateTime.of(2025, 5, 13, 10, 0));
        TransactionType type = new TransactionType();
        type.setTypeId(1);
        type.setIsIncome(false);
        existing.setTransactionType(type);
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        existing.setSubAccount(subAccount);
        existing.setAmount(new BigDecimal("100.00"));
        User user = new User();
        user.setUserId(2); // Other user
        existing.setUser(user);
        existing.setRemarks("Grocery purchase");

        when(transactionService.findById(eq(1))).thenReturn(existing);

        log.debug("Executing updateTransaction_user_otherUser_forbidden request");
        mockMvc.perform(put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Cannot update other user's transaction"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void deleteTransaction_user_success() throws Exception {
        log.debug("Setting up deleteTransaction_user_success");
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTime(LocalDateTime.of(2025, 5, 13, 10, 0));
        TransactionType type = new TransactionType();
        type.setTypeId(1);
        type.setIsIncome(false);
        transaction.setTransactionType(type);
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        transaction.setSubAccount(subAccount);
        transaction.setAmount(new BigDecimal("100.00"));
        User user = new User();
        user.setUserId(1);
        transaction.setUser(user);
        transaction.setRemarks("Grocery purchase");

        when(transactionService.findById(eq(1))).thenReturn(transaction);
        doNothing().when(transactionService).delete(eq(1));

        log.debug("Executing deleteTransaction_user_success request");
        mockMvc.perform(delete("/transactions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void deleteTransaction_user_otherUser_forbidden() throws Exception {
        log.debug("Setting up deleteTransaction_user_otherUser_forbidden");
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setTime(LocalDateTime.of(2025, 5, 13, 10, 0));
        TransactionType type = new TransactionType();
        type.setTypeId(1);
        type.setIsIncome(false);
        transaction.setTransactionType(type);
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        transaction.setSubAccount(subAccount);
        transaction.setAmount(new BigDecimal("100.00"));
        User user = new User();
        user.setUserId(2); // Other user
        transaction.setUser(user);
        transaction.setRemarks("Grocery purchase");

        when(transactionService.findById(eq(1))).thenReturn(transaction);

        log.debug("Executing deleteTransaction_user_otherUser_forbidden request");
        mockMvc.perform(delete("/transactions/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Cannot delete other user's transaction"));
    }
}