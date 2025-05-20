import { vi } from 'vitest';
import type { AxiosResponse } from 'axios';
import type { TransactionDTO, TransactionRequest } from '@/api/models/transactions';

export const mockTransactionsApi = {
  getTransactions: vi.fn().mockImplementation((): Promise<AxiosResponse<TransactionDTO[]>> => {
    return Promise.resolve({
      data: [
        {
          transactionId: 1,
          accountId: 1,
          typeId: 1,
          amount: 1000,
          description: '测试交易1',
          transactionDate: '2024-01-01'
        },
        {
          transactionId: 2,
          accountId: 1,
          typeId: 2,
          amount: 2000,
          description: '测试交易2',
          transactionDate: '2024-01-02'
        }
      ],
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  getTransaction: vi.fn().mockImplementation((id: number): Promise<AxiosResponse<TransactionDTO>> => {
    return Promise.resolve({
      data: {
        transactionId: id,
        accountId: 1,
        typeId: 1,
        amount: 1000,
        description: '测试交易',
        transactionDate: '2024-01-01'
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  createTransaction: vi.fn().mockImplementation((request: TransactionRequest): Promise<AxiosResponse<TransactionDTO>> => {
    return Promise.resolve({
      data: {
        transactionId: 3,
        accountId: request.accountId,
        typeId: request.typeId,
        amount: request.amount,
        description: request.description,
        transactionDate: request.transactionDate
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  updateTransaction: vi.fn().mockImplementation((id: number, request: TransactionRequest): Promise<AxiosResponse<TransactionDTO>> => {
    return Promise.resolve({
      data: {
        transactionId: id,
        accountId: request.accountId,
        typeId: request.typeId,
        amount: request.amount,
        description: request.description,
        transactionDate: request.transactionDate
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  deleteTransaction: vi.fn().mockImplementation((): Promise<AxiosResponse<null>> => {
    return Promise.resolve({
      data: null,
      status: 204,
      statusText: 'No Content',
      headers: {},
      config: {} as any
    });
  })
}; 