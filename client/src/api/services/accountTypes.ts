import { apiClient } from '../client';
import type { AccountTypeDTO } from '../models/accountTypes';

export class AccountTypesService {
  async getAccountTypes(): Promise<AccountTypeDTO[]> {
    const response = await apiClient.get<AccountTypeDTO[]>('/transaction-types');
    return response.data;
  }

  async createAccountType(request: any): Promise<AccountTypeDTO> {
    const response = await apiClient.post<AccountTypeDTO>('/transaction-types', request);
    return response.data;
  }

  async updateAccountType(typeId: number, request: any): Promise<AccountTypeDTO> {
    const response = await apiClient.put<AccountTypeDTO>(`/transaction-types/${typeId}`, request);
    return response.data;
  }

  async deleteAccountType(typeId: number): Promise<void> {
    await apiClient.delete(`/transaction-types/${typeId}`);
  }
}

export const accountTypesService = new AccountTypesService(); 