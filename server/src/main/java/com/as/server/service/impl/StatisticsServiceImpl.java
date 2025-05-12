package com.as.server.service.impl;

import com.as.server.dto.statistics.StatisticsResponse;
import com.as.server.dto.statistics.StatisticsResponseExpenseByType;
import com.as.server.dto.statistics.StatisticsResponseIncomeByType;
import com.as.server.repository.TransactionRepository;
import com.as.server.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public StatisticsResponse getStatistics(Integer userId, Integer accountId, Integer subAccountId, String period, Integer year, Integer month) {
        LocalDateTime start, end;
        if ("monthly".equals(period)) {
            if (month == null) {
                throw new IllegalArgumentException("Month is required for monthly statistics");
            }
            YearMonth yearMonth = YearMonth.of(year, month);
            start = yearMonth.atDay(1).atStartOfDay();
            end = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        } else if ("annual".equals(period)) {
            start = LocalDateTime.of(year, 1, 1, 0, 0);
            end = LocalDateTime.of(year, 12, 31, 23, 59, 59);
        } else {
            throw new IllegalArgumentException("Invalid period: " + period);
        }

        List<Object[]> incomeByType = transactionRepository.findIncomeByType(userId, accountId, subAccountId, start, end);
        List<Object[]> expenseByType = transactionRepository.findExpenseByType(userId, accountId, subAccountId, start, end);

        StatisticsResponse response = new StatisticsResponse();
        response.setPeriod(StatisticsResponse.PeriodEnum.valueOf(period));
        response.setYear(year);
        response.setMonth(month);
        response.setIncomeByType(incomeByType.stream().map(row -> {
            StatisticsResponseIncomeByType item = new StatisticsResponseIncomeByType();
            item.setTypeId((Integer) row[0]);
            item.setTypeName((String) row[1]);
            item.setAmount(((BigDecimal) row[2]).toString());
            return item;
        }).collect(Collectors.toList()));
        response.setExpenseByType(expenseByType.stream().map(row -> {
            StatisticsResponseExpenseByType item = new StatisticsResponseExpenseByType();
            item.setTypeId((Integer) row[0]);
            item.setTypeName((String) row[1]);
            item.setAmount(((BigDecimal) row[2]).toString());
            return item;
        }).collect(Collectors.toList()));
        response.setTotalIncome(response.getIncomeByType().stream()
                .map(item -> new BigDecimal(item.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .toString());
        response.setTotalExpense(response.getExpenseByType().stream()
                .map(item -> new BigDecimal(item.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .toString());
        return response;
    }
}
