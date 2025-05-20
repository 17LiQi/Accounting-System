import { apiClient } from '../client';
import type { StatisticsResponse } from '../models/statistics/statistics-response';

class StatisticsService {
  async getStatistics(startDate: string, endDate: string): Promise<StatisticsResponse> {
    const response = await apiClient.get('/statistics', {
      params: {
        startDate,
        endDate
      }
    });
    return response.data;
  }
}

export const statisticsService = new StatisticsService(); 