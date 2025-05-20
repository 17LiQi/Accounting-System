import { apiClient } from '../client';
import type { TransactionTypeDTO } from '../models/transaction-type/transaction-type-dto';
import type { TransactionTypeRequest } from '../models/transaction-type/transaction-type-request';

class TransactionTypesService {
  async getTransactionTypes(): Promise<TransactionTypeDTO[]> {
    const response = await apiClient.get('/transaction-types');
    return response.data;
  }

  async getTransactionType(id: number): Promise<TransactionTypeDTO> {
    const response = await apiClient.get(`/transaction-types/${id}`);
    return response.data;
  }

  async createTransactionType(request: TransactionTypeRequest): Promise<TransactionTypeDTO> {
    const response = await apiClient.post('/transaction-types', request);
    return response.data;
  }

  async updateTransactionType(id: number, request: TransactionTypeRequest): Promise<TransactionTypeDTO> {
    const response = await apiClient.put(`/transaction-types/${id}`, request);
    return response.data;
  }

  async deleteTransactionType(id: number): Promise<void> {
    await apiClient.delete(`/transaction-types/${id}`);
  }
}

export const transactionTypesService = new TransactionTypesService(); 