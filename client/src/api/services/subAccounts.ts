import { axiosInstance } from '../services/axiosInstance';
import type { SubAccountDTO } from '../models/sub-accounts/sub-account-dto';
import type { SubAccountRequest } from '../models/sub-accounts/sub-account-request';

export const subAccountsService = {
  getAllSubAccounts: async (): Promise<SubAccountDTO[]> => {
    try {
      const response = await axiosInstance.get<SubAccountDTO[]>('/sub-accounts');
      return response.data;
    } catch (error: any) {
      throw new Error(`获取所有子账户失败: ${error.response?.data?.message || error.message}`);
    }
  },
  getSubAccounts: async (subAccountId: number): Promise<SubAccountDTO> => {
    try {
      const response = await axiosInstance.get<SubAccountDTO>(`/sub-accounts/${subAccountId}`);
      return response.data;
    } catch (error: any) {
      throw new Error(`获取子账户失败: ${error.response?.data?.message || error.message}`);
    }
  },
  createSubAccount: async (accountId: number, request: SubAccountRequest): Promise<SubAccountDTO> => {
    try {
      const response = await axiosInstance.post<SubAccountDTO>(`/sub-accounts`, request);
      return response.data;
    } catch (error: any) {
      throw new Error(`创建子账户失败: ${error.response?.data?.message || error.message}`);
    }
  },
  updateSubAccount: async (subAccountId: number, request: SubAccountRequest): Promise<SubAccountDTO> => {
    try {
      const response = await axiosInstance.put<SubAccountDTO>(`/sub-accounts/${subAccountId}`, request);
      return response.data;
    } catch (error: any) {
      throw new Error(`更新子账户失败: ${error.response?.data?.message || error.message}`);
    }
  },
  deleteSubAccount: async (subAccountId: number): Promise<void> => {
    try {
      await axiosInstance.delete(`/sub-accounts/${subAccountId}`);
    } catch (error: any) {
      throw new Error(`删除子账户失败: ${error.response?.data?.message || error.message}`);
    }
  }
};