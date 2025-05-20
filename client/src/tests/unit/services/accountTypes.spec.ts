import { describe, it, expect, beforeEach, vi } from 'vitest';
import { accountTypesService } from '@/api/services/accountTypes';
import type { AccountTypeDTO, AccountTypeRequest } from '@/api/models/accountTypes';
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

describe('AccountTypesService', () => {
  const mockAccountType: AccountTypeDTO = {
    typeId: 1,
    typeName: '测试账户类型',
    description: '测试账户类型描述'
  };

  const mockAccountTypeRequest: AccountTypeRequest = {
    typeName: '测试账户类型',
    description: '测试账户类型描述'
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getAccountTypes', () => {
    it('应该返回账户类型列表', async () => {
      vi.mocked(apiClient.get).mockResolvedValueOnce({
        data: [mockAccountType]
      });

      const result = await accountTypesService.getAccountTypes();
      expect(result).toEqual([mockAccountType]);
      expect(apiClient.get).toHaveBeenCalledWith('/account-types');
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.get).mockRejectedValueOnce(new Error('API Error'));
      await expect(accountTypesService.getAccountTypes()).rejects.toThrow('API Error');
    });
  });

  describe('createAccountType', () => {
    it('应该创建新账户类型', async () => {
      vi.mocked(apiClient.post).mockResolvedValueOnce({
        data: mockAccountType
      });

      const result = await accountTypesService.createAccountType(mockAccountTypeRequest);
      expect(result).toEqual(mockAccountType);
      expect(apiClient.post).toHaveBeenCalledWith('/account-types', mockAccountTypeRequest);
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.post).mockRejectedValueOnce(new Error('API Error'));
      await expect(accountTypesService.createAccountType(mockAccountTypeRequest)).rejects.toThrow('API Error');
    });
  });

  describe('updateAccountType', () => {
    it('应该更新账户类型', async () => {
      vi.mocked(apiClient.put).mockResolvedValueOnce({
        data: mockAccountType
      });

      const result = await accountTypesService.updateAccountType(1, mockAccountTypeRequest);
      expect(result).toEqual(mockAccountType);
      expect(apiClient.put).toHaveBeenCalledWith('/account-types/1', mockAccountTypeRequest);
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.put).mockRejectedValueOnce(new Error('API Error'));
      await expect(accountTypesService.updateAccountType(1, mockAccountTypeRequest)).rejects.toThrow('API Error');
    });
  });

  describe('deleteAccountType', () => {
    it('应该删除账户类型', async () => {
      vi.mocked(apiClient.delete).mockResolvedValueOnce({});

      await accountTypesService.deleteAccountType(1);
      expect(apiClient.delete).toHaveBeenCalledWith('/account-types/1');
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.delete).mockRejectedValueOnce(new Error('API Error'));
      await expect(accountTypesService.deleteAccountType(1)).rejects.toThrow('API Error');
    });
  });
}); 