import { axiosInstance } from '../services/axiosInstance';
import type { SubAccountDTO } from '../models/sub-accounts/sub-account-dto';
import type { SubAccountRequest } from '../models/sub-accounts/sub-account-request';

export const subAccountsService = {
  getSubAccounts: async (accountId: number): Promise<SubAccountDTO[]> => {
    const response = await axiosInstance.get<SubAccountDTO[]>(`/accounts/${accountId}/sub-accounts`);
    return response.data;
  },

  createSubAccount: async (accountId: number, request: SubAccountRequest): Promise<SubAccountDTO> => {
    const response = await axiosInstance.post<SubAccountDTO>(`/accounts/${accountId}/sub-accounts`, request);
    return response.data;
  },

  updateSubAccount: async (accountId: number, subAccountId: number, request: SubAccountRequest): Promise<SubAccountDTO> => {
    const response = await axiosInstance.put<SubAccountDTO>(`/accounts/${accountId}/sub-accounts/${subAccountId}`, request);
    return response.data;
  },

  deleteSubAccount: async (accountId: number, subAccountId: number): Promise<void> => {
    await axiosInstance.delete(`/accounts/${accountId}/sub-accounts/${subAccountId}`);
  }
}; 