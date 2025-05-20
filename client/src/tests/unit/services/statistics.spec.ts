import { describe, it, expect, beforeEach, vi } from 'vitest';
import { statisticsService } from '@/api/services/statistics';
import type { StatisticsResponse } from '@/api/models/statistics/statistics-response';
import { apiClient } from '@/api/client';

vi.mock('axios', () => ({
  default: {
    create: () => ({
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      delete: vi.fn(),
      interceptors: {
        request: { use: vi.fn(), eject: vi.fn() },
        response: { use: vi.fn(), eject: vi.fn() }
      }
    })
  }
}));

vi.mock('@/api/client', () => ({
  apiClient: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
    interceptors: {
      request: { use: vi.fn(), eject: vi.fn() },
      response: { use: vi.fn(), eject: vi.fn() }
    }
  },
  isUsingMock: true
}));

describe('StatisticsService', () => {
  const mockStatistics: StatisticsResponse = {
    period: 'monthly',
    year: 2024,
    month: 3,
    totalIncome: '5000.00',
    totalExpense: '3000.00',
    incomeByType: [
      {
        typeId: 1,
        typeName: '工资',
        amount: '5000.00'
      }
    ],
    expenseByType: [
      {
        typeId: 2,
        typeName: '日常消费',
        amount: '3000.00'
      }
    ]
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getStatistics', () => {
    it('应该返回统计数据', async () => {
      const startDate = '2024-03-01';
      const endDate = '2024-03-31';
      vi.mocked(apiClient.get).mockResolvedValueOnce({
        data: mockStatistics
      });

      const result = await statisticsService.getStatistics(startDate, endDate);
      expect(result).toEqual(mockStatistics);
      expect(apiClient.get).toHaveBeenCalledWith('/statistics', {
        params: {
          startDate,
          endDate
        }
      });
    });

    it('应该处理错误', async () => {
      const startDate = '2024-03-01';
      const endDate = '2024-03-31';
      vi.mocked(apiClient.get).mockRejectedValueOnce(new Error('API Error'));
      await expect(statisticsService.getStatistics(startDate, endDate)).rejects.toThrow('API Error');
    });
  });
}); 