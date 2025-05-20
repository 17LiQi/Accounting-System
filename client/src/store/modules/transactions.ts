import { defineStore } from 'pinia';
import { transactionsService } from '@/api/services/transactions';
import type { TransactionDTO, TransactionRequest } from '@/api/models/transactions';

interface TransactionState {
  transactions: TransactionDTO[];
  loading: boolean;
  error: string | null;
}

export const useTransactionStore = defineStore('transactions', {
  state: (): TransactionState => ({
    transactions: [],
    loading: false,
    error: null
  }),

  actions: {
    async fetchTransactions() {
      this.loading = true;
      this.error = null;
      try {
        const transactions = await transactionsService.getTransactions();
        this.transactions = transactions;
      } catch (error) {
        this.error = error instanceof Error ? error.message : '获取交易列表失败';
      } finally {
        this.loading = false;
      }
    },

    async createTransaction(request: TransactionRequest) {
      this.loading = true;
      this.error = null;
      try {
        const transaction = await transactionsService.createTransaction(request);
        this.transactions.push(transaction);
      } catch (error) {
        this.error = error instanceof Error ? error.message : '创建交易失败';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async updateTransaction(id: number, request: TransactionRequest) {
      this.loading = true;
      this.error = null;
      try {
        const transaction = await transactionsService.updateTransaction(id, request);
        const index = this.transactions.findIndex(t => t.transactionId === id);
        if (index !== -1) {
          this.transactions[index] = transaction;
        }
      } catch (error) {
        this.error = error instanceof Error ? error.message : '更新交易失败';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async deleteTransaction(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await transactionsService.deleteTransaction(id);
        this.transactions = this.transactions.filter(t => t.transactionId !== id);
      } catch (error) {
        this.error = error instanceof Error ? error.message : '删除交易失败';
        throw error;
      } finally {
        this.loading = false;
      }
    }
  }
}); 