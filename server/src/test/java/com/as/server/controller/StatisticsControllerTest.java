package com.as.server.controller;

import com.as.server.dto.statistics.StatisticsResponse;
import com.as.server.dto.statistics.StatisticsResponseExpenseByType;
import com.as.server.dto.statistics.StatisticsResponseIncomeByType;
import com.as.server.enums.Period;
import com.as.server.service.StatisticsService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsControllerTest {

    private static final Logger log = LoggerFactory.getLogger(StatisticsControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;
    @Test
    @WithMockUser(roles = "ADMIN")
    void getStatistics_admin_success() throws Exception {
        log.debug("Setting up getStatistics_admin_success");
        StatisticsResponse response = new StatisticsResponse();
        response.setPeriod(Period.MONTHLY);
        response.setYear(2025);
        response.setMonth(5);
        response.setTotalIncome("1000.00");
        response.setTotalExpense("500.00");
        response.setIncomeByType(Collections.singletonList(new StatisticsResponseIncomeByType(1, "Salary", new BigDecimal("1000.00"))));
        response.setExpenseByType(Collections.singletonList(new StatisticsResponseExpenseByType(2, "Food", new BigDecimal("500.00"))));

        when(statisticsService.getStatistics(eq(1), nullable(Integer.class), nullable(Integer.class), eq(Period.MONTHLY), eq(2025), eq(5), nullable(Integer.class), nullable(Integer.class)))
                .thenReturn(response);

        log.debug("Executing getStatistics_admin_success request");
        mockMvc.perform(get("/statistics")
                        .param("userId", "1")
                        .param("period", "monthly")
                        .param("year", "2025")
                        .param("month", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.period").value("monthly"))
                .andExpect(jsonPath("$.year").value(2025))
                .andExpect(jsonPath("$.month").value(5))
                .andExpect(jsonPath("$.totalIncome").value("1000.00"))
                .andExpect(jsonPath("$.totalExpense").value("500.00"))
                .andExpect(jsonPath("$.incomeByType[0].typeId").value(1))
                .andExpect(jsonPath("$.incomeByType[0].typeName").value("Salary"))
                .andExpect(jsonPath("$.incomeByType[0].amount").value("1000.00"))
                .andExpect(jsonPath("$.expenseByType[0].typeId").value(2))
                .andExpect(jsonPath("$.expenseByType[0].typeName").value("Food"))
                .andExpect(jsonPath("$.expenseByType[0].amount").value("500.00"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getStatistics_user_success() throws Exception {
        log.debug("Setting up getStatistics_user_success");
        StatisticsResponse response = new StatisticsResponse();
        response.setPeriod(Period.YEARLY);
        response.setYear(2025);
        response.setTotalIncome("2000.00");
        response.setTotalExpense("1500.00");
        response.setIncomeByType(Collections.singletonList(new StatisticsResponseIncomeByType(1, "Salary", new BigDecimal("2000.00"))));
        response.setExpenseByType(Collections.singletonList(new StatisticsResponseExpenseByType(2, "Food", new BigDecimal("1500.00"))));

        when(statisticsService.getStatistics(eq(1), nullable(Integer.class), nullable(Integer.class),
                eq(Period.YEARLY), eq(2025), nullable(Integer.class), nullable(Integer.class), nullable(Integer.class)))
                .thenReturn(response);

        log.debug("Executing getStatistics_user_success request");
        mockMvc.perform(get("/statistics")
                        .param("period", "yearly")
                        .param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.period").value("yearly"))
                .andExpect(jsonPath("$.year").value(2025))
                .andExpect(jsonPath("$.totalIncome").value("2000.00"))
                .andExpect(jsonPath("$.totalExpense").value("1500.00"))
                .andExpect(jsonPath("$.incomeByType[0].typeId").value(1))
                .andExpect(jsonPath("$.incomeByType[0].typeName").value("Salary"))
                .andExpect(jsonPath("$.incomeByType[0].amount").value("2000.00"))
                .andExpect(jsonPath("$.expenseByType[0].typeId").value(2))
                .andExpect(jsonPath("$.expenseByType[0].typeName").value("Food"))
                .andExpect(jsonPath("$.expenseByType[0].amount").value("1500.00"));
    }

    @Test
    @WithMockUser(roles = "USER", username = "1")
    void getStatistics_user_accessOtherUser_throwsForbidden() throws Exception {
        mockMvc.perform(get("/statistics")
                        .param("userId", "2")
                        .param("period", "monthly")
                        .param("year", "2025")
                        .param("month", "5"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PERMISSION_DENIED"))
                .andExpect(jsonPath("$.message").value("Cannot access other user's statistics"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStatistics_invalidPeriod_throwsBadRequest() throws Exception {
        mockMvc.perform(get("/statistics")
                        .param("period", "invalid")
                        .param("year", "2025"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("DATA_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Invalid period value: invalid"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStatistics_weekly_success() throws Exception {
        StatisticsResponse response = new StatisticsResponse();
        response.setPeriod(Period.WEEKLY);
        response.setYear(2025);
        response.setWeek(20);
        response.setTotalIncome("300.00");
        response.setTotalExpense("200.00");
        response.setIncomeByType(Collections.singletonList(new StatisticsResponseIncomeByType(1, "Salary", new BigDecimal("300.00"))));
        response.setExpenseByType(Collections.singletonList(new StatisticsResponseExpenseByType(2, "Food", new BigDecimal("200.00"))));

        when(statisticsService.getStatistics(eq(1), any(), any(), eq(Period.WEEKLY), eq(2025), any(), eq(20), any()))
                .thenReturn(response);

        mockMvc.perform(get("/statistics")
                        .param("userId", "1")
                        .param("period", "weekly")
                        .param("year", "2025")
                        .param("week", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.period").value("weekly"))
                .andExpect(jsonPath("$.year").value(2025))
                .andExpect(jsonPath("$.week").value(20))
                .andExpect(jsonPath("$.totalIncome").value("300.00"))
                .andExpect(jsonPath("$.totalExpense").value("200.00"))
                .andExpect(jsonPath("$.incomeByType[0].typeId").value(1))
                .andExpect(jsonPath("$.incomeByType[0].typeName").value("Salary"))
                .andExpect(jsonPath("$.incomeByType[0].amount").value("300.00"))
                .andExpect(jsonPath("$.expenseByType[0].typeId").value(2))
                .andExpect(jsonPath("$.expenseByType[0].typeName").value("Food"))
                .andExpect(jsonPath("$.expenseByType[0].amount").value("200.00"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStatistics_daily_success() throws Exception {
        StatisticsResponse response = new StatisticsResponse();
        response.setPeriod(Period.DAILY);
        response.setYear(2025);
        response.setMonth(5);
        response.setDay(15);
        response.setTotalIncome("100.00");
        response.setTotalExpense("50.00");
        response.setIncomeByType(Collections.singletonList(new StatisticsResponseIncomeByType(1, "Salary", new BigDecimal("100.00"))));
        response.setExpenseByType(Collections.singletonList(new StatisticsResponseExpenseByType(2, "Food", new BigDecimal("50.00"))));

        when(statisticsService.getStatistics(eq(1), any(), any(), eq(Period.DAILY), eq(2025), eq(5), any(), eq(15)))
                .thenReturn(response);

        mockMvc.perform(get("/statistics")
                        .param("userId", "1")
                        .param("period", "daily")
                        .param("year", "2025")
                        .param("month", "5")
                        .param("day", "15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.period").value("daily"))
                .andExpect(jsonPath("$.year").value(2025))
                .andExpect(jsonPath("$.month").value(5))
                .andExpect(jsonPath("$.day").value(15))
                .andExpect(jsonPath("$.totalIncome").value("100.00"))
                .andExpect(jsonPath("$.totalExpense").value("50.00"))
                .andExpect(jsonPath("$.incomeByType[0].typeId").value(1))
                .andExpect(jsonPath("$.incomeByType[0].typeName").value("Salary"))
                .andExpect(jsonPath("$.incomeByType[0].amount").value("100.00"))
                .andExpect(jsonPath("$.expenseByType[0].typeId").value(2))
                .andExpect(jsonPath("$.expenseByType[0].typeName").value("Food"))
                .andExpect(jsonPath("$.expenseByType[0].amount").value("50.00"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStatistics_missingWeek_throwsBadRequest() throws Exception {
        mockMvc.perform(get("/statistics")
                        .param("period", "weekly")
                        .param("year", "2025"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("DATA_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Week is required for weekly period"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStatistics_missingDay_throwsBadRequest() throws Exception {
        mockMvc.perform(get("/statistics")
                        .param("period", "daily")
                        .param("year", "2025")
                        .param("month", "5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("DATA_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Month and day are required for daily period"));
    }
}