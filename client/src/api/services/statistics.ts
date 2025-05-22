import { apiClient } from '../client';
import type { StatisticsResponse } from '../models/statistics/statistics-response';

export class StatisticsService {
  async getStatistics(startDate: string, endDate: string): Promise<StatisticsResponse> {
    const response = await apiClient.get('/statistics', {
      params: {
        year: new Date().getFullYear(),
        period: 'MONTHLY',
        startDate,
        endDate
      }
    });
    return response.data;
  }
}

export const statisticsService = new StatisticsService(); 