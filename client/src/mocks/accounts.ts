import { vi } from 'vitest';
import type { AxiosResponse } from 'axios';
import type { AccountDTO, AccountRequest } from '@/api/models/accounts';
import type { AccountTypeDTO } from '@/api/models/accountTypes';
import { AccountRequestTypeEnum } from '@/api/models/accounts/account-request';

// 模拟数据存储
export const mockAccounts: AccountDTO[] = [
  {
    accountId: 1,
    accountName: '工商银行',
    accountType: AccountRequestTypeEnum.Bank,
    balance: 10000
  }
];

export const mockAccountTypes: AccountTypeDTO[] = [
  {
    typeId: 1,
    typeName: AccountRequestTypeEnum.Bank,
    description: '银行账户'
  },
  {
    typeId: 2,
    typeName: AccountRequestTypeEnum.Wechat,
    description: '微信钱包'
  },
  {
    typeId: 3,
    typeName: AccountRequestTypeEnum.Alipay,
    description: '支付宝'
  },
  {
    typeId: 4,
    typeName: AccountRequestTypeEnum.Other,
    description: '其他'
  }
];

export const mockAccountsApi = {
  getAccounts: vi.fn().mockImplementation((): Promise<AxiosResponse<AccountDTO[]>> => {
    return Promise.resolve({
      data: mockAccounts,
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  getAccount: vi.fn().mockImplementation((id: number): Promise<AxiosResponse<AccountDTO>> => {
    const account = mockAccounts.find(a => a.accountId === id);
    if (!account) {
      return Promise.reject(new Error('Account not found'));
    }
    return Promise.resolve({
      data: account,
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  createAccount: vi.fn().mockImplementation((request: AccountRequest): Promise<AxiosResponse<AccountDTO>> => {
    const newAccount: AccountDTO = {
      accountId: mockAccounts.length + 1,
      accountName: request.accountName,
      accountType: request.type,
      balance: 0
    };
    mockAccounts.push(newAccount);
    return Promise.resolve({
      data: newAccount,
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  updateAccount: vi.fn().mockImplementation((id: number, request: AccountRequest): Promise<AxiosResponse<AccountDTO>> => {
    const index = mockAccounts.findIndex(a => a.accountId === id);
    if (index === -1) {
      return Promise.reject(new Error('Account not found'));
    }
    const updatedAccount: AccountDTO = {
      ...mockAccounts[index],
      accountName: request.accountName,
      accountType: request.type
    };
    mockAccounts[index] = updatedAccount;
    return Promise.resolve({
      data: updatedAccount,
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  deleteAccount: vi.fn().mockImplementation((id: number): Promise<AxiosResponse<null>> => {
    const index = mockAccounts.findIndex(a => a.accountId === id);
    if (index === -1) {
      return Promise.reject(new Error('Account not found'));
    }
    mockAccounts.splice(index, 1);
    return Promise.resolve({
      data: null,
      status: 204,
      statusText: 'No Content',
      headers: {},
      config: {} as any
    });
  })
}; 