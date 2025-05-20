import { vi } from 'vitest';
import type { AxiosResponse } from 'axios';
import type { StatisticsDTO } from '@/api/models/statistics';

export const mockStatisticsApi = {
  getStatistics: vi.fn().mockImplementation((): Promise<AxiosResponse<StatisticsDTO>> => {
    return Promise.resolve({
      data: {
        totalIncome: 10000,
        totalExpense: 5000,
        netIncome: 5000,
        accountBalances: [
          {
            accountId: 1,
            accountName: '工商银行',
            balance: 5000
          }
        ],
        monthlyStatistics: [
          {
            month: '2024-01',
            income: 5000,
            expense: 2000,
            netIncome: 3000
          }
        ]
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  })
}; 