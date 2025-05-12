package com.as.server.service.impl;

import com.as.server.dto.statistics.StatisticsResponse;
import com.as.server.dto.statistics.StatisticsResponseExpenseByType;
import com.as.server.dto.statistics.StatisticsResponseIncomeByType;
import com.as.server.enums.Period;
import com.as.server.repository.TransactionRepository;
import com.as.server.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final TransactionRepository transactionRepository;

    public StatisticsServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public StatisticsResponse getStatistics(Integer userId, Integer accountId, Integer subAccountId, Period period, Integer year, Integer month, Integer week, Integer day) {
        // 参数验证
        if (period == null || year == null) {
            throw new IllegalArgumentException("Period and year are required");
        }
        if (period == Period.MONTHLY && month == null) {
            throw new IllegalArgumentException("Month is required for monthly period");
        }
        if (period == Period.WEEKLY && week == null) {
            throw new IllegalArgumentException("Week is required for weekly period");
        }
        if (period == Period.DAILY && (month == null || day == null)) {
            throw new IllegalArgumentException("Month and day are required for daily period");
        }

        StatisticsResponse response = new StatisticsResponse();
        response.setPeriod(period);
        response.setYear(year);
        response.setMonth(month);
        response.setWeek(week);
        response.setDay(day);

        // 总收入和支出
        BigDecimal totalIncome = transactionRepository.sumByTypeAndPeriod(true, userId, accountId, subAccountId, String.valueOf(period), year, month, week, day);
        BigDecimal totalExpense = transactionRepository.sumByTypeAndPeriod(false, userId, accountId, subAccountId, String.valueOf(period), year, month, week, day);

        response.setTotalIncome(totalIncome != null ? totalIncome.setScale(2, RoundingMode.HALF_UP).toString() : "0.00");
        response.setTotalExpense(totalExpense != null ? totalExpense.setScale(2, RoundingMode.HALF_UP).toString() : "0.00");

        // 按类型统计（示例，需根据实际类型实现）
        List<StatisticsResponseIncomeByType> incomeByType = transactionRepository.sumIncomeByType(userId, accountId, subAccountId, String.valueOf(period), year, month, week, day);
        List<StatisticsResponseExpenseByType> expenseByType = transactionRepository.sumExpenseByType(userId, accountId, subAccountId, String.valueOf(period), year, month, week, day);

        response.setIncomeByType(incomeByType);
        response.setExpenseByType(expenseByType);

        return response;
    }
}
