import { httpClient } from '../http-client';
import type { TransactionDTO } from '../models/transactions/transaction-dto';
import type { TransactionRequest } from '../models/transactions/transaction-request';
import type { TransactionResponse } from '../models/transactions/transaction-dto';
import type { TransactionTypeDTO } from '../models/transactions/transaction-type-dto';
import { useAuthStore } from '@/store/modules/auth';

export interface TransactionQuery {
  page: number;
  size: number;
  subAccountId?: number;
  startDate?: string;
  endDate?: string;
  userId?: number;
  sort?: 'asc' | 'desc';
}

class TransactionsService {
  async getTransactions(query: TransactionQuery): Promise<TransactionResponse> {
    const authStore = useAuthStore();
    const isAdmin = authStore.currentUser?.role === 'ADMIN';
    
    const params = new URLSearchParams();
    params.append('page', query.page.toString());
    params.append('size', query.size.toString());
    if (query.subAccountId !== undefined) params.append('subAccountId', query.subAccountId.toString());
    if (query.startDate) params.append('startDate', query.startDate);
    if (query.endDate) params.append('endDate', query.endDate);
    if (query.sort) params.append('sort', query.sort);
    
    // 只有管理员可以指定 userId，普通用户只能查看自己的记录
    if (isAdmin && query.userId !== undefined) {
      params.append('userId', query.userId.toString());
    } else if (!isAdmin) {
      // 普通用户强制使用自己的 userId
      const currentUserId = authStore.currentUser?.userId;
      if (currentUserId) {
        params.append('userId', currentUserId.toString());
      }
    }

    const response = await httpClient.get<TransactionResponse>(`/transactions?${params.toString()}`);
    return response.data;
  }

  async getTransactionTypes(): Promise<TransactionTypeDTO[]> {
    const response = await httpClient.get<TransactionTypeDTO[]>('/transaction-types');
    return response.data;
  }

  async getTransaction(id: number): Promise<TransactionDTO> {
    const response = await httpClient.get<TransactionDTO>(`/transactions/${id}`);
    return response.data;
  }

  async createTransaction(request: TransactionRequest): Promise<TransactionDTO> {
    const response = await httpClient.post<TransactionDTO>('/transactions', request);
    return response.data;
  }

  async updateTransaction(id: number, request: TransactionRequest): Promise<TransactionDTO> {
    const response = await httpClient.put<TransactionDTO>(`/transactions/${id}`, request);
    return response.data;
  }

  async deleteTransaction(id: number): Promise<void> {
    await httpClient.delete(`/transactions/${id}`);
  }
}

export const transactionsService = new TransactionsService(); 