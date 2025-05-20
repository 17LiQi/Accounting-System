import { describe, it, expect, vi, beforeEach } from 'vitest';
import { transactionsService } from '@/api/services/transactions';
import type { TransactionDTO, TransactionRequest } from '@/api/models/transactions';
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

describe('TransactionsService', () => {
  const mockTransaction: TransactionDTO = {
    transactionId: 1,
    amount: 100,
    description: '测试交易',
    transactionDate: '2024-01-01',
    accountId: 1,
    typeId: 1
  };

  const mockTransactionRequest: TransactionRequest = {
    amount: 100,
    description: '测试交易',
    transactionDate: '2024-01-01',
    accountId: 1,
    typeId: 1
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getTransactions', () => {
    it('应该返回交易列表', async () => {
      vi.mocked(apiClient.get).mockResolvedValueOnce({
        data: [mockTransaction]
      });

      const result = await transactionsService.getTransactions();
      expect(result).toEqual([mockTransaction]);
      expect(apiClient.get).toHaveBeenCalledWith('/transactions');
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.get).mockRejectedValueOnce(new Error('API Error'));
      await expect(transactionsService.getTransactions()).rejects.toThrow('API Error');
    });
  });

  describe('getTransaction', () => {
    it('应该返回单个交易', async () => {
      vi.mocked(apiClient.get).mockResolvedValueOnce({
        data: mockTransaction
      });

      const result = await transactionsService.getTransaction(1);
      expect(result).toEqual(mockTransaction);
      expect(apiClient.get).toHaveBeenCalledWith('/transactions/1');
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.get).mockRejectedValueOnce(new Error('API Error'));
      await expect(transactionsService.getTransaction(1)).rejects.toThrow('API Error');
    });
  });

  describe('createTransaction', () => {
    it('应该创建新交易', async () => {
      vi.mocked(apiClient.post).mockResolvedValueOnce({
        data: mockTransaction
      });

      const result = await transactionsService.createTransaction(mockTransactionRequest);
      expect(result).toEqual(mockTransaction);
      expect(apiClient.post).toHaveBeenCalledWith('/transactions', mockTransactionRequest);
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.post).mockRejectedValueOnce(new Error('API Error'));
      await expect(transactionsService.createTransaction(mockTransactionRequest)).rejects.toThrow('API Error');
    });
  });

  describe('updateTransaction', () => {
    it('应该更新交易', async () => {
      vi.mocked(apiClient.put).mockResolvedValueOnce({
        data: mockTransaction
      });

      const result = await transactionsService.updateTransaction(1, mockTransactionRequest);
      expect(result).toEqual(mockTransaction);
      expect(apiClient.put).toHaveBeenCalledWith('/transactions/1', mockTransactionRequest);
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.put).mockRejectedValueOnce(new Error('API Error'));
      await expect(transactionsService.updateTransaction(1, mockTransactionRequest)).rejects.toThrow('API Error');
    });
  });

  describe('deleteTransaction', () => {
    it('应该删除交易', async () => {
      vi.mocked(apiClient.delete).mockResolvedValueOnce({});

      await transactionsService.deleteTransaction(1);
      expect(apiClient.delete).toHaveBeenCalledWith('/transactions/1');
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.delete).mockRejectedValueOnce(new Error('API Error'));
      await expect(transactionsService.deleteTransaction(1)).rejects.toThrow('API Error');
    });
  });
}); 