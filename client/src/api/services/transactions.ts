import { apiClient } from '../client';
import type { TransactionDTO } from '../models/transactions/transaction-dto';
import type { TransactionRequest } from '../models/transactions/transaction-request';
import type { TransactionResponse } from '../models/transactions/transaction-dto';
import type { TransactionTypeDTO } from '../models/transactions/transaction-type-dto';

export interface TransactionQuery {
  page?: number;
  size?: number;
  subAccountId?: number;
  startDate?: string;
  endDate?: string;
}

class TransactionsService {
  async getTransactions(query: TransactionQuery = {}): Promise<TransactionResponse> {
    const params = new URLSearchParams();
    if (query.page !== undefined) params.append('page', query.page.toString());
    if (query.size !== undefined) params.append('size', query.size.toString());
    if (query.subAccountId !== undefined) params.append('subAccountId', query.subAccountId.toString());
    if (query.startDate) params.append('startDate', query.startDate);
    if (query.endDate) params.append('endDate', query.endDate);

    const response = await apiClient.get<TransactionResponse>(`/transactions?${params.toString()}`);
    return response.data;
  }

  async getTransactionTypes(): Promise<TransactionTypeDTO[]> {
    const response = await apiClient.get<TransactionTypeDTO[]>('/transaction-types');
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