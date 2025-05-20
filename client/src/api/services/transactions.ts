import { apiClient } from '../client';
import type { TransactionDTO, TransactionRequest } from '../models/transactions';

class TransactionsService {
  async getTransactions(): Promise<TransactionDTO[]> {
    const response = await apiClient.get('/transactions');
    return response.data;
  }

  async getTransaction(id: number): Promise<TransactionDTO> {
    const response = await apiClient.get(`/transactions/${id}`);
    return response.data;
  }

  async createTransaction(request: TransactionRequest): Promise<TransactionDTO> {
    const response = await apiClient.post('/transactions', request);
    return response.data;
  }

  async updateTransaction(id: number, request: TransactionRequest): Promise<TransactionDTO> {
    const response = await apiClient.put(`/transactions/${id}`, request);
    return response.data;
  }

  async deleteTransaction(id: number): Promise<void> {
    await apiClient.delete(`/transactions/${id}`);
  }
}

export const transactionsService = new TransactionsService(); 