import { defineStore } from 'pinia';
import { transactionsService } from '@/api/services/transactions';
import type { TransactionDTO, TransactionRequest } from '@/api/models/transactions';
import type { UserDTO } from '@/api/models/user/user-dto';
import { usersService } from '@/api/services/users';

interface TransactionWithUser extends TransactionDTO {
  user?: UserDTO;
}

interface TransactionState {
  transactions: TransactionWithUser[];
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
        const response = await transactionsService.getTransactions();
        
        // 获取所有相关用户的信息
        const userIds = [...new Set(response.transactions.map(t => t.userId))];
        const users = await Promise.all(
          userIds.map(id => usersService.getUser(id))
        );
        
        // 将用户信息添加到交易记录中
        const transactionsWithUsers: TransactionWithUser[] = response.transactions.map(transaction => ({
          ...transaction,
          user: users.find(u => u.userId === transaction.userId)
        }));
        
        this.transactions = transactionsWithUsers;
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
        // 获取用户信息
        const user = await usersService.getUser(transaction.userId);
        const transactionWithUser: TransactionWithUser = {
          ...transaction,
          user
        };
        this.transactions.push(transactionWithUser);
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
        // 获取用户信息
        const user = await usersService.getUser(transaction.userId);
        const transactionWithUser: TransactionWithUser = {
          ...transaction,
          user
        };
        const index = this.transactions.findIndex(t => t.transactionId === id);
        if (index !== -1) {
          this.transactions[index] = transactionWithUser;
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
    },

    setTransactions(transactions: TransactionWithUser[]) {
      this.transactions = transactions;
    }
  }
}); 