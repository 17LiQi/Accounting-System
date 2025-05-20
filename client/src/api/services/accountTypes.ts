import { apiClient } from '../client';
import type { AccountTypeDTO, AccountTypeRequest } from '../models/accountTypes';

class AccountTypesService {
  async getAccountTypes(): Promise<AccountTypeDTO[]> {
    const response = await apiClient.get('/account-types');
    return response.data;
  }

  async createAccountType(request: AccountTypeRequest): Promise<AccountTypeDTO> {
    const response = await apiClient.post('/account-types', request);
    return response.data;
  }

  async updateAccountType(typeId: number, request: AccountTypeRequest): Promise<AccountTypeDTO> {
    const response = await apiClient.put(`/account-types/${typeId}`, request);
    return response.data;
  }

  async deleteAccountType(typeId: number): Promise<void> {
    await apiClient.delete(`/account-types/${typeId}`);
  }
}

export const accountTypesService = new AccountTypesService(); 