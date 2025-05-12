package com.as.server.controller;

import com.as.server.dto.accounts.AccountDTO;
import com.as.server.dto.accounts.AccountRequest;
import com.as.server.entity.Account;
import com.as.server.entity.AccountType;
import com.as.server.mapper.EntityMapper;
import com.as.server.repository.AccountTypeRepository;
import com.as.server.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private EntityMapper entityMapper;

    @MockBean
    private AccountTypeRepository accountTypeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAccount_success() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setName("Test Account");
        request.setTypeId(1);
        request.setType(AccountRequest.TypeEnum.BANK);

        AccountType accountType = new AccountType();
        accountType.setTypeId(1);
        accountType.setTypeName("BANK");

        Account account = new Account();
        account.setAccountId(1);
        account.setName("Test Account");
        account.setAccountType(accountType);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(1);
        accountDTO.setName("Test Account");
        accountDTO.setTypeId(1);
        accountDTO.setType(AccountDTO.TypeEnum.BANK);

        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(accountType));
        when(entityMapper.toAccount(any(AccountRequest.class), eq(accountTypeRepository))).thenReturn(account);
        when(accountService.create(any(Account.class))).thenReturn(account);
        when(entityMapper.toAccountDTO(any(Account.class))).thenReturn(accountDTO);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.name").value("Test Account"))
                .andExpect(jsonPath("$.typeId").value(1))
                .andExpect(jsonPath("$.type").value("BANK"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAccount_invalidTypeId_throwsBadRequest() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setName("Test Account");
        request.setTypeId(999);
        // 故意不设置 type，触发 @NotNull 验证错误

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("DATA_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Validation failed for request: type must not be null"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listAccounts_success() throws Exception {
        Account account = new Account();
        account.setAccountId(1);
        account.setName("Test Account");
        AccountType accountType = new AccountType();
        accountType.setTypeId(1);
        accountType.setTypeName("BANK");
        account.setAccountType(accountType);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(1);
        accountDTO.setName("Test Account");
        accountDTO.setTypeId(1);
        accountDTO.setType(AccountDTO.TypeEnum.BANK);

        List<Account> accounts = Collections.singletonList(account);
        when(accountService.findAll()).thenReturn(accounts);
        when(entityMapper.toAccountDTO(any(Account.class))).thenReturn(accountDTO);

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].accountId").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Account"))
                .andExpect(jsonPath("$[0].typeId").value(1))
                .andExpect(jsonPath("$[0].type").value("BANK"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAccount_success() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setName("Updated Account");
        request.setTypeId(1);
        request.setType(AccountRequest.TypeEnum.BANK);

        AccountType accountType = new AccountType();
        accountType.setTypeId(1);
        accountType.setTypeName("BANK");

        Account account = new Account();
        account.setAccountId(1);
        account.setName("Updated Account");
        account.setAccountType(accountType);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountId(1);
        accountDTO.setName("Updated Account");
        accountDTO.setTypeId(1);
        accountDTO.setType(AccountDTO.TypeEnum.BANK);

        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(accountType));
        when(entityMapper.toAccount(any(AccountRequest.class), eq(accountTypeRepository))).thenReturn(account);
        when(accountService.update(eq(1), any(Account.class))).thenReturn(account);
        when(entityMapper.toAccountDTO(any(Account.class))).thenReturn(accountDTO);

        mockMvc.perform(put("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.name").value("Updated Account"))
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
        request.setName("Test Account");
        request.setTypeId(1);
        request.setType(AccountRequest.TypeEnum.BANK);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Access is denied"));
    }
}