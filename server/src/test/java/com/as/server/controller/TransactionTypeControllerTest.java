package com.as.server.controller;

import com.as.server.dto.transactions.TransactionTypeDTO;
import com.as.server.dto.transactions.TransactionTypeRequest;
import com.as.server.entity.TransactionType;
import com.as.server.mapper.EntityMapper;
import com.as.server.service.TransactionTypeService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionTypeService transactionTypeService;

    @MockBean
    private EntityMapper entityMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTransactionType_success() throws Exception {
        TransactionTypeRequest request = new TransactionTypeRequest();
        request.setTypeName("Salary");
        request.setIsIncome(true);

        TransactionType transactionType = new TransactionType();
        transactionType.setTypeId(1);
        transactionType.setTypeName("Salary");
        transactionType.setIsIncome(true);

        TransactionTypeDTO typeDTO = new TransactionTypeDTO();
        typeDTO.setTypeId(1);
        typeDTO.setTypeName("Salary");
        typeDTO.setIsIncome(true);

        when(entityMapper.toTransactionType(any(TransactionTypeRequest.class))).thenReturn(transactionType);
        when(transactionTypeService.create(any(TransactionType.class))).thenReturn(transactionType);
        when(entityMapper.toTransactionTypeDTO(any(TransactionType.class))).thenReturn(typeDTO);

        mockMvc.perform(post("/transaction-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.typeId").value(1))
                .andExpect(jsonPath("$.typeName").value("Salary"))
                .andExpect(jsonPath("$.isIncome").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTransactionType_duplicateName_throwsBadRequest() throws Exception {
        TransactionTypeRequest request = new TransactionTypeRequest();
        request.setTypeName("Salary");
        request.setIsIncome(true);

        when(entityMapper.toTransactionType(any(TransactionTypeRequest.class))).thenReturn(new TransactionType());
        when(transactionTypeService.create(any(TransactionType.class))).thenThrow(new IllegalArgumentException("Transaction type name already exists"));

        mockMvc.perform(post("/transaction-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Transaction type name already exists"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listTransactionTypes_success() throws Exception {
        TransactionType transactionType = new TransactionType();
        transactionType.setTypeId(1);
        transactionType.setTypeName("Salary");
        transactionType.setIsIncome(true);

        TransactionTypeDTO typeDTO = new TransactionTypeDTO();
        typeDTO.setTypeId(1);
        typeDTO.setTypeName("Salary");
        typeDTO.setIsIncome(true);

        List<TransactionType> types = Collections.singletonList(transactionType);
        when(transactionTypeService.findAll()).thenReturn(types);
        when(entityMapper.toTransactionTypeDTO(any(TransactionType.class))).thenReturn(typeDTO);

        mockMvc.perform(get("/transaction-types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].typeId").value(1))
                .andExpect(jsonPath("$[0].typeName").value("Salary"))
                .andExpect(jsonPath("$[0].isIncome").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateTransactionType_success() throws Exception {
        TransactionTypeRequest request = new TransactionTypeRequest();
        request.setTypeName("Updated Salary");
        request.setIsIncome(true);

        TransactionType transactionType = new TransactionType();
        transactionType.setTypeId(1);
        transactionType.setTypeName("Updated Salary");
        transactionType.setIsIncome(true);

        TransactionTypeDTO typeDTO = new TransactionTypeDTO();
        typeDTO.setTypeId(1);
        typeDTO.setTypeName("Updated Salary");
        typeDTO.setIsIncome(true);

        when(entityMapper.toTransactionType(any(TransactionTypeRequest.class))).thenReturn(transactionType);
        when(transactionTypeService.update(eq(1), any(TransactionType.class))).thenReturn(transactionType);
        when(entityMapper.toTransactionTypeDTO(any(TransactionType.class))).thenReturn(typeDTO);

        mockMvc.perform(put("/transaction-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.typeId").value(1))
                .andExpect(jsonPath("$.typeName").value("Updated Salary"))
                .andExpect(jsonPath("$.isIncome").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTransactionType_success() throws Exception {
        when(transactionTypeService.hasTransactions(1)).thenReturn(false);
        doNothing().when(transactionTypeService).delete(1);

        mockMvc.perform(delete("/transaction-types/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTransactionType_hasTransactions_throwsConflict() throws Exception {
        when(transactionTypeService.hasTransactions(1)).thenReturn(true);

        mockMvc.perform(delete("/transaction-types/1"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Transaction type has associated transactions"));
    }
}