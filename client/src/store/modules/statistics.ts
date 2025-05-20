import { defineStore } from 'pinia';
import { statisticsService } from '@/api/services/statistics';
import type { StatisticsDTO } from '@/api/models/statistics';

interface StatisticsState {
  statistics: StatisticsDTO | null;
  loading: boolean;
  error: string | null;
}

export const useStatisticsStore = defineStore('statistics', {
  state: (): StatisticsState => ({
    statistics: null,
    loading: false,
    error: null
  }),

  actions: {
    async fetchStatistics(startDate: string, endDate: string) {
      this.loading = true;
      this.error = null;
      try {
        const response = await statisticsService.getStatistics(startDate, endDate);
        const statistics: StatisticsDTO = {
          totalIncome: parseFloat(response.totalIncome),
          totalExpense: parseFloat(response.totalExpense),
          accountBalances: [],
          monthlyStatistics: []
        };
        this.statistics = statistics;
        return statistics;
      } catch (error) {
        this.error = error instanceof Error ? error.message : '获取统计数据失败';
        throw error;
      } finally {
        this.loading = false;
      }
    }
  }
}); 