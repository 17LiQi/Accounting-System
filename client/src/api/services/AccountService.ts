import axios from 'axios';
import type { Account, AccountRequest, Transaction } from '@/models/accounts';

export class AccountService {
  private static baseUrl = '/api/accounts';

  static async getAccounts(): Promise<{ data: Account[] }> {
    return axios.get(this.baseUrl);
  }

  static async getAccount(id: number): Promise<{ data: Account }> {
    return axios.get(`${this.baseUrl}/${id}`);
  }

  static async createAccount(request: AccountRequest): Promise<{ data: Account }> {
    return axios.post(this.baseUrl, request);
  }

  static async getAccountTransactions(id: number): Promise<{ data: Transaction[] }> {
    return axios.get(`${this.baseUrl}/${id}/transactions`);
  }
} 