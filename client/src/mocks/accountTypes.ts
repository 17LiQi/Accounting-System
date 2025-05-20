import { vi } from 'vitest';
import type { AxiosResponse } from 'axios';
import type { AccountTypeDTO, AccountTypeRequest } from '@/api/models/accountTypes';

export const mockAccountTypesApi = {
  getAccountTypes: vi.fn().mockImplementation((): Promise<AxiosResponse<AccountTypeDTO[]>> => {
    return Promise.resolve({
      data: [
        {
          typeId: 1,
          typeName: '现金账户',
          description: '现金账户类型'
        },
        {
          typeId: 2,
          typeName: '银行账户',
          description: '银行账户类型'
        }
      ],
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  getAccountType: vi.fn().mockImplementation((id: number): Promise<AxiosResponse<AccountTypeDTO>> => {
    return Promise.resolve({
      data: {
        typeId: id,
        typeName: '现金账户',
        description: '现金账户类型'
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  createAccountType: vi.fn().mockImplementation((request: AccountTypeRequest): Promise<AxiosResponse<AccountTypeDTO>> => {
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

  updateAccountType: vi.fn().mockImplementation((id: number, request: AccountTypeRequest): Promise<AxiosResponse<AccountTypeDTO>> => {
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

  deleteAccountType: vi.fn().mockImplementation((): Promise<AxiosResponse<null>> => {
    return Promise.resolve({
      data: null,
      status: 204,
      statusText: 'No Content',
      headers: {},
      config: {} as any
    });
  })
}; 