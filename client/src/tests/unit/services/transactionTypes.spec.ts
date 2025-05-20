// 移除顶层 import axios, { AxiosError } from 'axios'
// import axios, { AxiosError } from 'axios';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import { transactionTypesService } from '@/api/services/transactionTypes';
// 移除从 '@/tests/setup' 导入 mockAxios
// import { mockAxios } from '@/tests/setup';
import type { TransactionTypeDTO } from '@/api/models/transaction-type/transaction-type-dto';
import type { TransactionTypeRequest } from '@/api/models/transaction-type/transaction-type-request';
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

describe('TransactionTypesService', () => {
  const mockTransactionType: TransactionTypeDTO = {
    typeId: 1,
    typeName: '工资',
    isIncome: true
  };

  const mockTransactionTypeRequest: TransactionTypeRequest = {
    typeName: '工资',
    isIncome: true
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getTransactionTypes', () => {
    it('应该返回交易类型列表', async () => {
      vi.mocked(apiClient.get).mockResolvedValueOnce({
        data: [mockTransactionType]
      });

      const result = await transactionTypesService.getTransactionTypes();
      expect(result).toEqual([mockTransactionType]);
      expect(apiClient.get).toHaveBeenCalledWith('/transaction-types');
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.get).mockRejectedValueOnce(new Error('API Error'));
      await expect(transactionTypesService.getTransactionTypes()).rejects.toThrow('API Error');
    });
  });

  describe('getTransactionType', () => {
    it('应该返回单个交易类型', async () => {
      vi.mocked(apiClient.get).mockResolvedValueOnce({
        data: mockTransactionType
      });

      const result = await transactionTypesService.getTransactionType(1);
      expect(result).toEqual(mockTransactionType);
      expect(apiClient.get).toHaveBeenCalledWith('/transaction-types/1');
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.get).mockRejectedValueOnce(new Error('API Error'));
      await expect(transactionTypesService.getTransactionType(1)).rejects.toThrow('API Error');
    });
  });

  describe('createTransactionType', () => {
    it('应该创建交易类型', async () => {
      vi.mocked(apiClient.post).mockResolvedValueOnce({
        data: mockTransactionType
      });

      const result = await transactionTypesService.createTransactionType(mockTransactionTypeRequest);
      expect(result).toEqual(mockTransactionType);
      expect(apiClient.post).toHaveBeenCalledWith('/transaction-types', mockTransactionTypeRequest);
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.post).mockRejectedValueOnce(new Error('API Error'));
      await expect(transactionTypesService.createTransactionType(mockTransactionTypeRequest)).rejects.toThrow('API Error');
    });
  });

  describe('updateTransactionType', () => {
    it('应该更新交易类型', async () => {
      const typeId = 1;
      vi.mocked(apiClient.put).mockResolvedValueOnce({
        data: mockTransactionType
      });

      const result = await transactionTypesService.updateTransactionType(typeId, mockTransactionTypeRequest);
      expect(result).toEqual(mockTransactionType);
      expect(apiClient.put).toHaveBeenCalledWith(`/transaction-types/${typeId}`, mockTransactionTypeRequest);
    });

    it('应该处理错误', async () => {
      const typeId = 1;
      vi.mocked(apiClient.put).mockRejectedValueOnce(new Error('API Error'));
      await expect(transactionTypesService.updateTransactionType(typeId, mockTransactionTypeRequest)).rejects.toThrow('API Error');
    });
  });

  describe('deleteTransactionType', () => {
    it('应该删除交易类型', async () => {
      const typeId = 1;
      vi.mocked(apiClient.delete).mockResolvedValueOnce({
        data: { message: '删除成功' }
      });

      await transactionTypesService.deleteTransactionType(typeId);
      expect(apiClient.delete).toHaveBeenCalledWith(`/transaction-types/${typeId}`);
    });

    it('应该处理错误', async () => {
      const typeId = 1;
      vi.mocked(apiClient.delete).mockRejectedValueOnce(new Error('API Error'));
      await expect(transactionTypesService.deleteTransactionType(typeId)).rejects.toThrow('API Error');
    });
  });
}); 