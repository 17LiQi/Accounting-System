package com.as.server.controllerTest;

import com.as.server.dto.accounts.AccountDTO;
import com.as.server.dto.accounts.AccountRequest;
import com.as.server.entity.Account;
import com.as.server.enums.AccountType;
import com.as.server.repository.AccountTypeRepository;
import com.as.server.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    private static final Logger log = LoggerFactory.getLogger(AccountControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reset(accountService); // 重置 mock
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAccount_success() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setAccountName("Test Account");
        request.setTypeId(1);
        request.setType(AccountType.BANK);

        com.as.server.entity.AccountType accountType = new com.as.server.entity.AccountType();
        accountType.setTypeId(1);
        accountType.setTypeName("BANK");

        Account account = new Account();
        account.setAccountId(1);
        account.setAccountName("Test Account");
        account.setAccountType(accountType);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(1);
        accountDTO.setAccountName("Test Account");
        accountDTO.setTypeId(1);
        accountDTO.setType(AccountType.BANK);

        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(accountType));
        when(accountService.create(any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.accountName").value("Test Account"))
                .andExpect(jsonPath("$.typeId").value(1))
                .andExpect(jsonPath("$.type").value("BANK"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAccount_invalidTypeId_throwsBadRequest() throws Exception {
        log.debug("Setting up createAccount_invalidTypeId_throwsBadRequest");
        AccountRequest request = new AccountRequest();
        request.setAccountName("Test Account");
        request.setTypeId(999);
        request.setType(AccountType.BANK);
        when(accountTypeRepository.findById(999)).thenReturn(Optional.empty());
        String json = objectMapper.writeValueAsString(request);
        log.debug("Request JSON: {}", json);
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("DATA_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("AccountType with id 999 not found"));
        verify(accountTypeRepository, times(1)).findById(999);
        verify(accountService, never()).create(any(Account.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listAccounts_success() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setAccountName("Test Account");
        com.as.server.entity.AccountType accountType = new com.as.server.entity.AccountType();
        accountType.setTypeId(1);
        accountType.setTypeName("BANK");
        account.setAccountType(accountType);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(1);
        accountDTO.setAccountName("Test Account");
        accountDTO.setTypeId(1);
        accountDTO.setType(AccountType.BANK);

        List<Account> accounts = Collections.singletonList(account);
        when(accountService.findAll()).thenReturn(accounts);

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].accountId").value(1))
                .andExpect(jsonPath("$[0].accountName").value("Test Account"))
                .andExpect(jsonPath("$[0].typeId").value(1))
                .andExpect(jsonPath("$[0].type").value("BANK"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAccount_success() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setAccountName("Updated Account");
        request.setTypeId(1);
        request.setType(AccountType.BANK);

        com.as.server.entity.AccountType accountType = new com.as.server.entity.AccountType();
        accountType.setTypeId(1);
        accountType.setTypeName("BANK");

        Account account = new Account();
        account.setAccountId(1);
        account.setAccountName("Updated Account");
        account.setAccountType(accountType);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(1);
        accountDTO.setAccountName("Updated Account");
        accountDTO.setTypeId(1);
        accountDTO.setType(AccountType.BANK);

        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(accountType));
        when(accountService.update(eq(1), any(Account.class))).thenReturn(account);

        mockMvc.perform(put("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.accountName").value("Updated Account"))
                .andExpect(jsonPath("$.typeId").value(1))
                .andExpect(jsonPath("$.type").value("BANK"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAccount_success() throws Exception {
        when(accountService.hasSubAccounts(1)).thenReturn(false);
        doNothing().when(accountService).delete(1);

        mockMvc.perform(delete("/accounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAccount_hasSubAccounts_throwsConflict() throws Exception {
        when(accountService.hasSubAccounts(1)).thenReturn(true);

        mockMvc.perform(delete("/accounts/1"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Account has associated sub-accounts"));
    }

    @Test
    void createAccount_accessDenied_throwsForbidden() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setAccountName("Test Account");
        request.setTypeId(1);
        request.setType(AccountType.BANK);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Access is denied"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getTransactions_nullPage_failsValidation() throws Exception {
        log.debug("Setting up getTransactions_nullPage_failsValidation");
        mockMvc.perform(get("/transactions")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("page must not be null"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getTransactions_invalidSize_failsValidation() throws Exception {
        log.debug("Setting up getTransactions_invalidSize_failsValidation");
        mockMvc.perform(get("/transactions")
                        .param("page", "0")
                        .param("size", "101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("size must be less than or equal to 100"));
    }
}