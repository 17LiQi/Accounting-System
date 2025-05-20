import { vi } from 'vitest';
import type { AxiosResponse } from 'axios';
import type { TransactionTypeRequest } from '@/api/models/transactionTypes';

export const mockTransactionTypesApi = {
  getTransactionTypes: vi.fn().mockImplementation((): Promise<AxiosResponse<any[]>> => {
    return Promise.resolve({
      data: [
        {
          typeId: 1,
          typeName: '收入',
          description: '收入类型'
        },
        {
          typeId: 2,
          typeName: '支出',
          description: '支出类型'
        }
      ],
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  getTransactionType: vi.fn().mockImplementation((id: number): Promise<AxiosResponse<any>> => {
    return Promise.resolve({
      data: {
        typeId: id,
        typeName: '收入',
        description: '收入类型'
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  createTransactionType: vi.fn().mockImplementation((request: TransactionTypeRequest): Promise<AxiosResponse<any>> => {
    return Promise.resolve({
      data: {
        typeId: 3,
        typeName: request.typeName,
        description: request.description
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  updateTransactionType: vi.fn().mockImplementation((id: number, request: TransactionTypeRequest): Promise<AxiosResponse<any>> => {
    return Promise.resolve({
      data: {
        typeId: id,
        typeName: request.typeName,
        description: request.description
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  deleteTransactionType: vi.fn().mockImplementation((): Promise<AxiosResponse<null>> => {
    return Promise.resolve({
      data: null,
      status: 204,
      statusText: 'No Content',
      headers: {},
      config: {} as any
    });
  })
}; 