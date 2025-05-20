import { describe, it, expect, beforeEach, vi } from 'vitest';
import { accountsService } from '@/api/services/accounts';
import type { AccountDTO } from '@/api/models/accounts';
import type { AccountRequest } from '@/api/models/accounts/account-request';
import { AccountRequestTypeEnum } from '@/api/models/accounts/account-request';
import { apiClient } from '@/api/client';

vi.mock('axios', () => ({
  default: {
    create: () => ({
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      delete: vi.fn(),
      interceptors: {
        request: { use: vi.fn(), eject: vi.fn() },
        response: { use: vi.fn(), eject: vi.fn() }
      }
    })
  }
}));

vi.mock('@/api/client', () => ({
  apiClient: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
    interceptors: {
      request: { use: vi.fn(), eject: vi.fn() },
      response: { use: vi.fn(), eject: vi.fn() }
    }
  },
  isUsingMock: true
}));

describe('AccountService', () => {
  const mockAccount: AccountDTO = {
    accountId: 1,
    accountName: '测试账户',
    accountType: 'BANK',
    balance: 1000.00
  };

  const mockAccountRequest: AccountRequest = {
    accountName: '测试账户',
    typeId: 1,
    type: AccountRequestTypeEnum.Bank
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should get accounts', async () => {
    vi.mocked(apiClient.get).mockResolvedValueOnce({
      data: [mockAccount]
    });

    const result = await accountsService.getAccounts();
    expect(result).toEqual([mockAccount]);
    expect(apiClient.get).toHaveBeenCalledWith('/accounts');
  });

  it('should create account', async () => {
    vi.mocked(apiClient.post).mockResolvedValueOnce({
      data: mockAccount
    });

    const result = await accountsService.createAccount(mockAccountRequest);
    expect(result).toEqual(mockAccount);
    expect(apiClient.post).toHaveBeenCalledWith('/accounts', mockAccountRequest);
  });

  it('should update account', async () => {
    const accountId = 1;
    vi.mocked(apiClient.put).mockResolvedValueOnce({
      data: mockAccount
    });

    const result = await accountsService.updateAccount(accountId, mockAccountRequest);
    expect(result).toEqual(mockAccount);
    expect(apiClient.put).toHaveBeenCalledWith(`/accounts/${accountId}`, mockAccountRequest);
  });

  it('should delete account', async () => {
    const accountId = 1;
    vi.mocked(apiClient.delete).mockResolvedValueOnce({
      data: { message: '删除成功' }
    });

    await accountsService.deleteAccount(accountId);
    expect(apiClient.delete).toHaveBeenCalledWith(`/accounts/${accountId}`);
  });
}); 