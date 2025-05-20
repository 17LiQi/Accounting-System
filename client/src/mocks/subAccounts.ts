import { vi } from 'vitest';
import type { AxiosResponse } from 'axios';
import type { SubAccountDTO, SubAccountRequest } from '@/api/models/subAccounts';

export const mockSubAccountsApi = {
  getSubAccounts: vi.fn().mockImplementation((accountId: number): Promise<AxiosResponse<SubAccountDTO[]>> => {
    return Promise.resolve({
      data: [
        {
          subAccountId: 1,
          accountId,
          name: '测试子账户1',
          balance: 1000,
          description: '测试子账户1描述'
        },
        {
          subAccountId: 2,
          accountId,
          name: '测试子账户2',
          balance: 2000,
          description: '测试子账户2描述'
        }
      ],
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  createSubAccount: vi.fn().mockImplementation((accountId: number, request: SubAccountRequest): Promise<AxiosResponse<SubAccountDTO>> => {
    return Promise.resolve({
      data: {
        subAccountId: 3,
        accountId,
        name: request.name,
        balance: request.balance,
        description: request.description
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  updateSubAccount: vi.fn().mockImplementation((accountId: number, subAccountId: number, request: SubAccountRequest): Promise<AxiosResponse<SubAccountDTO>> => {
    return Promise.resolve({
      data: {
        subAccountId,
        accountId,
        name: request.name,
        balance: request.balance,
        description: request.description
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  deleteSubAccount: vi.fn().mockImplementation((): Promise<AxiosResponse<null>> => {
    return Promise.resolve({
      data: null,
      status: 204,
      statusText: 'No Content',
      headers: {},
      config: {} as any
    });
  })
}; 