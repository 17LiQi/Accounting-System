package com.as.server.service;

import com.as.server.dto.statistics.StatisticsResponse;

public interface StatisticsService {
    StatisticsResponse getStatistics(Integer userId, Integer accountId, Integer subAccountId, String period, Integer year, Integer month);
}
