import { axiosInstance } from './axiosInstance';
import type { StatisticsResponse } from '../models/statistics';

export const statisticsService = {
  async getStatistics(params: {
    period: string;
    year: number;
    month?: number;
    week?: number;
    day?: number;
    userId?: number;
    accountId?: number;
    subAccountId?: number;
  }): Promise<StatisticsResponse> {
    try {
      const response = await axiosInstance.get<StatisticsResponse>('/statistics', { params });
      return response.data;
    } catch (error: any) {
      throw new Error(`获取统计数据失败: ${error.response?.data?.message || error.message}`);
    }
  }
};