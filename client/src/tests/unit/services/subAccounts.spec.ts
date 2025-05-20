import { describe, it, expect, beforeEach, vi } from 'vitest';
import { subAccountsService } from '@/api/services/subAccounts';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import type { SubAccountRequest } from '@/api/models/sub-accounts/sub-account-request';
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

describe('SubAccountsService', () => {
  const mockSubAccount: SubAccountDTO = {
    subAccountId: 1,
    accountId: 1,
    accountName: '测试子账户',
    accountNumber: '1234567890',
    cardType: 'DEBIT',
    balance: '1000.00'
  };

  const mockSubAccountRequest: SubAccountRequest = {
    accountId: 1,
    accountName: '测试子账户',
    accountNumber: '1234567890',
    cardType: 'DEBIT',
    balance: '1000.00'
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getSubAccounts', () => {
    it('应该返回子账户列表', async () => {
      const accountId = 1;
      vi.mocked(apiClient.get).mockResolvedValueOnce({
        data: [mockSubAccount]
      });

      const result = await subAccountsService.getSubAccounts(accountId);
      expect(result).toEqual([mockSubAccount]);
      expect(apiClient.get).toHaveBeenCalledWith(`/accounts/${accountId}/sub-accounts`);
    });

    it('应该处理错误', async () => {
      const accountId = 1;
      vi.mocked(apiClient.get).mockRejectedValueOnce(new Error('API Error'));
      await expect(subAccountsService.getSubAccounts(accountId)).rejects.toThrow('API Error');
    });
  });

  describe('createSubAccount', () => {
    it('应该创建子账户', async () => {
      const accountId = 1;
      vi.mocked(apiClient.post).mockResolvedValueOnce({
        data: mockSubAccount
      });

      const result = await subAccountsService.createSubAccount(accountId, mockSubAccountRequest);
      expect(result).toEqual(mockSubAccount);
      expect(apiClient.post).toHaveBeenCalledWith(`/accounts/${accountId}/sub-accounts`, mockSubAccountRequest);
    });

    it('应该处理错误', async () => {
      const accountId = 1;
      vi.mocked(apiClient.post).mockRejectedValueOnce(new Error('API Error'));
      await expect(subAccountsService.createSubAccount(accountId, mockSubAccountRequest)).rejects.toThrow('API Error');
    });
  });

  describe('updateSubAccount', () => {
    it('应该更新子账户', async () => {
      const accountId = 1;
      const subAccountId = 1;
      vi.mocked(apiClient.put).mockResolvedValueOnce({
        data: mockSubAccount
      });

      const result = await subAccountsService.updateSubAccount(accountId, subAccountId, mockSubAccountRequest);
      expect(result).toEqual(mockSubAccount);
      expect(apiClient.put).toHaveBeenCalledWith(`/accounts/${accountId}/sub-accounts/${subAccountId}`, mockSubAccountRequest);
    });

    it('应该处理错误', async () => {
      const accountId = 1;
      const subAccountId = 1;
      vi.mocked(apiClient.put).mockRejectedValueOnce(new Error('API Error'));
      await expect(subAccountsService.updateSubAccount(accountId, subAccountId, mockSubAccountRequest)).rejects.toThrow('API Error');
    });
  });

  describe('deleteSubAccount', () => {
    it('应该删除子账户', async () => {
      const accountId = 1;
      const subAccountId = 1;
      vi.mocked(apiClient.delete).mockResolvedValueOnce({
        data: { message: '删除成功' }
      });

      await subAccountsService.deleteSubAccount(accountId, subAccountId);
      expect(apiClient.delete).toHaveBeenCalledWith(`/accounts/${accountId}/sub-accounts/${subAccountId}`);
    });

    it('应该处理错误', async () => {
      const accountId = 1;
      const subAccountId = 1;
      vi.mocked(apiClient.delete).mockRejectedValueOnce(new Error('API Error'));
      await expect(subAccountsService.deleteSubAccount(accountId, subAccountId)).rejects.toThrow('API Error');
    });
  });
}); 