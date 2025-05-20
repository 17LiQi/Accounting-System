import { apiClient } from '../client';
import type { AccountDTO } from '../models/accounts';
import type { AccountRequest } from '../models/accounts/account-request';

class AccountsService {
  async getAccounts(): Promise<AccountDTO[]> {
    const response = await apiClient.get('/accounts');
    return response.data;
  }

  async createAccount(request: AccountRequest): Promise<AccountDTO> {
    const response = await apiClient.post('/accounts', request);
    return response.data;
  }

  async updateAccount(accountId: number, request: AccountRequest): Promise<AccountDTO> {
    const response = await apiClient.put(`/accounts/${accountId}`, request);
    return response.data;
  }

  async deleteAccount(accountId: number): Promise<void> {
    await apiClient.delete(`/accounts/${accountId}`);
  }
}

export const accountsService = new AccountsService(); 