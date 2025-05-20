import { vi } from 'vitest';
import type { AxiosResponse } from 'axios';
import type { AccountDTO, AccountRequest } from '@/api/models/accounts';
import type { AccountTypeDTO } from '@/api/models/accountTypes';
import { AccountRequestTypeEnum } from '@/api/models/accounts/account-request';

export const mockAccountsApi = {
  getAccounts: vi.fn().mockImplementation((): Promise<AxiosResponse<AccountDTO[]>> => {
    return Promise.resolve({
      data: [
        {
          accountId: 1,
          accountName: '工商银行',
          accountType: 'BANK',
          balance: 10000,
          description: '工商银行账户'
        },
        {
          accountId: 2,
          accountName: '建设银行',
          accountType: 'BANK',
          balance: 20000,
          description: '建设银行账户'
        }
      ],
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  getAccount: vi.fn().mockImplementation((id: number): Promise<AxiosResponse<AccountDTO>> => {
    return Promise.resolve({
      data: {
        accountId: id,
        accountName: '工商银行',
        accountType: 'BANK',
        balance: 10000,
        description: '工商银行账户'
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  createAccount: vi.fn().mockImplementation((request: AccountRequest): Promise<AxiosResponse<AccountDTO>> => {
    return Promise.resolve({
      data: {
        accountId: 3,
        accountName: request.accountName,
        accountType: request.type,
        balance: 0,
        description: ''
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  updateAccount: vi.fn().mockImplementation((id: number, request: AccountRequest): Promise<AxiosResponse<AccountDTO>> => {
    return Promise.resolve({
      data: {
        accountId: id,
        accountName: request.accountName,
        accountType: request.type,
        balance: 0,
        description: ''
      },
      status: 200,
      statusText: 'OK',
      headers: {},
      config: {} as any
    });
  }),

  deleteAccount: vi.fn().mockImplementation((): Promise<AxiosResponse<null>> => {
    return Promise.resolve({
      data: null,
      status: 204,
      statusText: 'No Content',
      headers: {},
      config: {} as any
    });
  })
};

export const mockAccounts: AccountDTO[] = [
  {
    accountId: 1,
    accountName: '工商银行',
    accountType: AccountRequestTypeEnum.Bank,
    description: '工资卡',
    balance: 10000
  },
  {
    accountId: 2,
    accountName: '微信钱包',
    accountType: AccountRequestTypeEnum.Wechat,
    description: '日常消费',
    balance: 5000
  },
  {
    accountId: 3,
    accountName: '支付宝',
    accountType: AccountRequestTypeEnum.Alipay,
    description: '网购专用',
    balance: 3000
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