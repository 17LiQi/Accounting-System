import { axiosInstance } from './axiosInstance';
import { useAuthStore } from '@/store/modules/auth';
import type { StatisticsResponse } from '../models/statistics';

export const statisticsService = {
  async getStatistics(rawParams: {
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
      const authStore = useAuthStore();
      const isAdmin = authStore.currentUser?.role === 'ADMIN';
      const currentUserId = authStore.currentUser?.userId;

      // 构建最终 params，只在管理员身份下传 userId
      const params: Record<string, any> = {
        period: rawParams.period,
        year: rawParams.year,
        accountId: rawParams.accountId,
        subAccountId: rawParams.subAccountId,
        month: rawParams.month,
        week: rawParams.week,
        day: rawParams.day
      };

      if (isAdmin && rawParams.userId !== undefined) {
        params.userId = rawParams.userId;
      }

      // 非管理员必须传当前用户 ID，由后端做身份校验
      if (!isAdmin && currentUserId) {
        params.userId = currentUserId;
      }

      const response = await axiosInstance.get<StatisticsResponse>('/statistics', { params });
      return response.data;
    } catch (error: any) {
      throw new Error(`获取统计数据失败: ${error.response?.data?.message || error.message}`);
    }
  }
};
