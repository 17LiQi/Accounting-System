package com.as.server.service;

import com.as.server.dto.statistics.StatisticsResponse;
import com.as.server.enums.Period;

public interface StatisticsService {
    StatisticsResponse getStatistics(Integer userId, Integer accountId, Integer subAccountId, Period period, Integer year, Integer month, Integer week , Integer day);
}
