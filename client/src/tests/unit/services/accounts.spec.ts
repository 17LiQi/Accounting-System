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
  }
}));

describe('AccountsService', () => {
  const mockAccount: AccountDTO = {
    accountId: 1,
    accountName: '测试账户',
    accountType: 'BANK',
    balance: 1000,
    description: '测试账户描述'
  };

  const mockAccountRequest: AccountRequest = {
    accountName: '测试账户',
    typeId: 1,
    type: AccountRequestTypeEnum.Bank
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getAccounts', () => {
    it('应该返回账户列表', async () => {
      vi.mocked(apiClient.get).mockResolvedValueOnce({
        data: [mockAccount]
      });

      const result = await accountsService.getAccounts();
      expect(result).toEqual([mockAccount]);
      expect(apiClient.get).toHaveBeenCalledWith('/accounts');
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.get).mockRejectedValueOnce(new Error('API Error'));
      await expect(accountsService.getAccounts()).rejects.toThrow('API Error');
    });
  });

  describe('createAccount', () => {
    it('应该创建新账户', async () => {
      vi.mocked(apiClient.post).mockResolvedValueOnce({
        data: mockAccount
      });

      const result = await accountsService.createAccount(mockAccountRequest);
      expect(result).toEqual(mockAccount);
      expect(apiClient.post).toHaveBeenCalledWith('/accounts', mockAccountRequest);
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.post).mockRejectedValueOnce(new Error('API Error'));
      await expect(accountsService.createAccount(mockAccountRequest)).rejects.toThrow('API Error');
    });
  });

  describe('updateAccount', () => {
    it('应该更新账户', async () => {
      vi.mocked(apiClient.put).mockResolvedValueOnce({
        data: mockAccount
      });

      const result = await accountsService.updateAccount(1, mockAccountRequest);
      expect(result).toEqual(mockAccount);
      expect(apiClient.put).toHaveBeenCalledWith('/accounts/1', mockAccountRequest);
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.put).mockRejectedValueOnce(new Error('API Error'));
      await expect(accountsService.updateAccount(1, mockAccountRequest)).rejects.toThrow('API Error');
    });
  });

  describe('deleteAccount', () => {
    it('应该删除账户', async () => {
      vi.mocked(apiClient.delete).mockResolvedValueOnce({});

      await accountsService.deleteAccount(1);
      expect(apiClient.delete).toHaveBeenCalledWith('/accounts/1');
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.delete).mockRejectedValueOnce(new Error('API Error'));
      await expect(accountsService.deleteAccount(1)).rejects.toThrow('API Error');
    });
  });
}); 