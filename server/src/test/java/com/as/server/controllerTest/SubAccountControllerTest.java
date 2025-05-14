package com.as.server.controllerTest;

import com.as.server.dto.accounts.SubAccountRequest;
import com.as.server.entity.Account;
import com.as.server.entity.SubAccount;
import com.as.server.entity.User;
import com.as.server.enums.CardType;
import com.as.server.service.SubAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SubAccountControllerTest {

    private static final Logger log = LoggerFactory.getLogger(SubAccountControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubAccountService subAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void createSubAccount_user_success() throws Exception {
        log.debug("Setting up createSubAccount_user_success");
        SubAccountRequest request = new SubAccountRequest()
                .accountId(1)
                .accountName("Savings Sub-Account")
                .accountNumber("1234567890")
                .cardType(CardType.VISA)
                .balance("500.05");

        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        subAccount.setAccountName("Savings Sub-Account");
        subAccount.setAccountNumber("1234567890");
        subAccount.setCardType(CardType.VISA);
        subAccount.setBalance(new BigDecimal("500.00"));
        Account account = new Account();
        account.setAccountId(1);
        subAccount.setAccount(account);
        User user = new User();
        user.setUserId(1);
        subAccount.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(subAccountService.create(any(SubAccount.class))).thenReturn(subAccount);

        mockMvc.perform(post("/sub-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subAccountId").value(1))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.accountName").value("Savings Sub-Account"))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.cardType").value("VISA")) // 修正为大写
                .andExpect(jsonPath("$.balance").value("500.00"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void createSubAccount_invalidBalance_throwsBadRequest() throws Exception {
        log.debug("Setting up createSubAccount_invalidBalance_throwsBadRequest");
        SubAccountRequest request = new SubAccountRequest()
                .accountId(1)
                .accountName("Savings Sub-Account")
                .accountNumber("1234567890")
                .cardType(CardType.VISA)
                .balance("500.0"); // 非法的 balance
        String json = objectMapper.writeValueAsString(request);
        log.debug("Request JSON: {}", json);
        mockMvc.perform(post("/sub-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.message").value("balance: must match \"^\\d+(\\.\\d{2})?$\""));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getSubAccounts_user_success() throws Exception {
        log.debug("Setting up getSubAccounts_user_success");
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        subAccount.setAccountName("Savings Sub-Account");
        subAccount.setAccountNumber("1234567890");
        subAccount.setCardType(CardType.VISA);
        subAccount.setBalance(new BigDecimal("500.00"));
        Account account = new Account();
        account.setAccountId(1);
        subAccount.setAccount(account);
        User user = new User();
        user.setUserId(1);
        subAccount.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(subAccountService.findAll()).thenReturn(Collections.singletonList(subAccount));

        mockMvc.perform(get("/sub-accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].subAccountId").value(1))
                .andExpect(jsonPath("$[0].accountId").value(1))
                .andExpect(jsonPath("$[0].accountName").value("Savings Sub-Account"))
                .andExpect(jsonPath("$[0].accountNumber").value("1234567890"))
                .andExpect(jsonPath("$[0].cardType").value("VISA")) // 修正为大写
                .andExpect(jsonPath("$[0].balance").value("500.00"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getSubAccount_user_success() throws Exception {
        log.debug("Setting up getSubAccount_user_success");
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        subAccount.setAccountName("Savings Sub-Account");
        subAccount.setAccountNumber("1234567890");
        subAccount.setCardType(CardType.VISA);
        subAccount.setBalance(new BigDecimal("500.00"));
        Account account = new Account();
        account.setAccountId(1);
        subAccount.setAccount(account);
        User user = new User();
        user.setUserId(1);
        subAccount.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(subAccountService.findById(eq(1))).thenReturn(subAccount);

        mockMvc.perform(get("/sub-accounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subAccountId").value(1))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.accountName").value("Savings Sub-Account"))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.cardType").value("VISA")) // 修正为大写
                .andExpect(jsonPath("$.balance").value("500.00"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getSubAccount_user_otherUser_forbidden() throws Exception {
        log.debug("Setting up getSubAccount_user_otherUser_forbidden");
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        subAccount.setAccountName("Savings Sub-Account");
        subAccount.setAccountNumber("1234567890");
        subAccount.setCardType(CardType.VISA);
        subAccount.setBalance(new BigDecimal("500.00"));
        Account account = new Account();
        account.setAccountId(1);
        subAccount.setAccount(account);
        User user = new User();
        user.setUserId(2); // Other user
        subAccount.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(subAccountService.findById(eq(1))).thenReturn(subAccount);

        mockMvc.perform(get("/sub-accounts/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Cannot access sub-account"));
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "1")
    void getSubAccount_admin_success() throws Exception {
        log.debug("Setting up getSubAccount_admin_success");
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        subAccount.setAccountName("Savings Sub-Account");
        subAccount.setAccountNumber("1234567890");
        subAccount.setCardType(CardType.VISA);
        subAccount.setBalance(new BigDecimal("500.00"));
        Account account = new Account();
        account.setAccountId(1);
        subAccount.setAccount(account);
        User user = new User();
        user.setUserId(2); // Other user
        subAccount.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(subAccountService.findById(eq(1))).thenReturn(subAccount);

        mockMvc.perform(get("/sub-accounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subAccountId").value(1))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.accountName").value("Savings Sub-Account"))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andExpect(jsonPath("$.cardType").value("VISA")) // 修正为大写
                .andExpect(jsonPath("$.balance").value("500.00"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void updateSubAccount_user_success() throws Exception {
        log.debug("Setting up updateSubAccount_user_success");
        SubAccountRequest request = new SubAccountRequest()
                .accountId(1)
                .accountName("Updated Sub-Account")
                .accountNumber("0987654321")
                .cardType(CardType.MASTERCARD)
                .balance("1000.00");

        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        subAccount.setAccountName("Updated Sub-Account");
        subAccount.setAccountNumber("0987654321");
        subAccount.setCardType(CardType.MASTERCARD);
        subAccount.setBalance(new BigDecimal("1000.00"));
        Account account = new Account();
        account.setAccountId(1);
        subAccount.setAccount(account);
        User user = new User();
        user.setUserId(1);
        subAccount.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(subAccountService.findById(eq(1))).thenReturn(subAccount);
        when(subAccountService.update(eq(1), any(SubAccount.class))).thenReturn(subAccount);

        mockMvc.perform(put("/sub-accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subAccountId").value(1))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.accountName").value("Updated Sub-Account"))
                .andExpect(jsonPath("$.accountNumber").value("0987654321"))
                .andExpect(jsonPath("$.cardType").value("MASTERCARD")) // 修正为大写
                .andExpect(jsonPath("$.balance").value("1000.00"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void updateSubAccount_user_otherUser_forbidden() throws Exception {
        log.debug("Setting up updateSubAccount_user_otherUser_forbidden");
        SubAccountRequest request = new SubAccountRequest()
                .accountId(1)
                .accountName("Updated Sub-Account")
                .accountNumber("0987654321")
                .cardType(CardType.MASTERCARD)
                .balance("1000.00");

        SubAccount existing = new SubAccount();
        existing.setSubAccountId(1);
        existing.setAccountName("Savings Sub-Account");
        existing.setAccountNumber("1234567890");
        existing.setCardType(CardType.VISA);
        existing.setBalance(new BigDecimal("500.00"));
        Account account = new Account();
        account.setAccountId(1);
        existing.setAccount(account);
        User user = new User();
        user.setUserId(2); // Other user
        existing.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(subAccountService.findById(eq(1))).thenReturn(existing);

        mockMvc.perform(put("/sub-accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Cannot update sub-account")); // 修正为实际消息
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void deleteSubAccount_user_success() throws Exception {
        log.debug("Setting up deleteSubAccount_user_success");
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        subAccount.setAccountName("Savings Sub-Account");
        subAccount.setAccountNumber("1234567890");
        subAccount.setCardType(CardType.VISA);
        subAccount.setBalance(new BigDecimal("500.00"));
        Account account = new Account();
        account.setAccountId(1);
        subAccount.setAccount(account);
        User user = new User();
        user.setUserId(1);
        subAccount.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(subAccountService.findById(eq(1))).thenReturn(subAccount);
        when(subAccountService.hasTransactions(eq(1))).thenReturn(false);
        doNothing().when(subAccountService).delete(eq(1));

        mockMvc.perform(delete("/sub-accounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void deleteSubAccount_user_otherUser_forbidden() throws Exception {
        log.debug("Setting up deleteSubAccount_user_otherUser_forbidden");
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        subAccount.setAccountName("Savings Sub-Account");
        subAccount.setAccountNumber("1234567890");
        subAccount.setCardType(CardType.VISA);
        subAccount.setBalance(new BigDecimal("500.00"));
        Account account = new Account();
        account.setAccountId(1);
        subAccount.setAccount(account);
        User user = new User();
        user.setUserId(2); // Other user
        subAccount.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(subAccountService.findById(eq(1))).thenReturn(subAccount);

        mockMvc.perform(delete("/sub-accounts/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Cannot delete sub-account"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void deleteSubAccount_hasTransactions_throwsConflict() throws Exception {
        log.debug("Setting up deleteSubAccount_hasTransactions_throwsConflict");
        SubAccount subAccount = new SubAccount();
        subAccount.setSubAccountId(1);
        subAccount.setAccountName("Savings Sub-Account");
        subAccount.setAccountNumber("1234567890");
        subAccount.setCardType(CardType.VISA);
        subAccount.setBalance(new BigDecimal("500.00"));
        Account account = new Account();
        account.setAccountId(1);
        subAccount.setAccount(account);
        User user = new User();
        user.setUserId(1);
        subAccount.setUsers(new HashSet<>(Collections.singletonList(user)));

        when(subAccountService.findById(eq(1))).thenReturn(subAccount);
        when(subAccountService.hasTransactions(eq(1))).thenReturn(true);

        mockMvc.perform(delete("/sub-accounts/1"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Sub-account has associated transactions"));
    }
}