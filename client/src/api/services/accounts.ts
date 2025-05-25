import type { AccountDTO } from '../models/accounts';
import type { AccountRequest } from '../models/accounts/account-request';
import type { AccountType } from '../models/accounts/account-type';
import type { SubAccountDTO } from '../models/sub-accounts/sub-account-dto';
import type { SubAccountRequest } from '../models/sub-accounts/sub-account-request';
import type { TransactionDTO } from '../models/transactions/transaction-dto';
import type { UserDTO } from '../models/user/user-dto';
import { AccountRequestTypeEnum } from '../models/accounts/account-request';
import { apiClient } from '../client';
import { useAuthStore } from '@/store/modules/auth';
import { usersService } from './users';

interface UserSubAccountDTO {
  userId: number;
  subAccountId: number;
}

// 类型转换函数
function normalizeAccountType(account: AccountDTO): AccountDTO {
  if (!account.type) {
    return account;
  }

  // 如果已经是对象类型，直接返回
  if (typeof account.type === 'object') {
    return account;
  }

  // 如果是字符串类型，转换为对象
  const typeMap: Record<string, AccountType> = {
    '现金': { typeId: 1, typeName: '现金' },
    '银行卡': { typeId: 2, typeName: '银行卡' },
    '支付宝': { typeId: 3, typeName: '支付宝' },
    '微信': { typeId: 4, typeName: '微信' }
  };

  const typeStr = account.type;
  const typeInfo = typeMap[typeStr] || { typeId: account.typeId, typeName: account.type };

  return {
    ...account,
    type: typeInfo.typeName || account.type
  };
}

class AccountsService {
  async testConnection(): Promise<any> {
      try {
          const response = await apiClient.get('/accounts');
          return {
              success: true,
              data: response.data
          };
      } catch (error) {
          console.error('测试连接失败:', error);
          throw error;
      }
  }

  async getAccounts(): Promise<AccountDTO[]> {
      const response = await apiClient.get<AccountDTO[]>('/accounts');
      return response.data;
  }

  async getAccount(id: number): Promise<AccountDTO> {
      const response = await apiClient.get<AccountDTO>(`/accounts/${id}`);
      return response.data;
  }

  async getAllSubAccounts(): Promise<SubAccountDTO[]> {
      const response = await apiClient.get<SubAccountDTO[]>('/sub-accounts');
      return response.data;
  }

  async getSubAccount(id: number): Promise<SubAccountDTO> {
      const response = await apiClient.get<SubAccountDTO>(`/sub-accounts/${id}`);
      return response.data;
  }

  async recalculateBalance(subAccountId: number): Promise<SubAccountDTO> {
      const currentAccount = await this.getSubAccount(subAccountId);
      let allTransactions: TransactionDTO[] = [];
      let page = 0;
      const pageSize = 100;

      while (true) {
          const response = await apiClient.get(`/transactions?subAccountId=${subAccountId}&size=${pageSize}&page=${page}`);
          const transactions = response.data.transactions;
          allTransactions = allTransactions.concat(transactions);
          if (transactions.length < pageSize) {
              break;
          }
          page++;
      }

      let newBalance = 0;
      for (const transaction of allTransactions) {
          if (transaction.isIncome) {
              newBalance += Number(transaction.amount);
          } else {
              newBalance -= Number(transaction.amount);
          }
      }

      const updateRequest = {
          accountId: currentAccount.accountId,
          accountName: currentAccount.accountName,
          accountNumber: currentAccount.accountNumber,
          cardType: currentAccount.cardType,
          balance: newBalance.toFixed(2)
      };

      console.log('更新请求数据:', updateRequest);
      const response = await apiClient.put(`/sub-accounts/${subAccountId}`, updateRequest);
      return response.data;
  }

  async createAccount(request: AccountRequest): Promise<AccountDTO> {
      try {
          console.log('创建账户请求数据:', request);
          const response = await apiClient.post<AccountDTO>('/accounts', request);
          return response.data;
      } catch (error: any) {
          console.error('创建账户失败:', error);
          if (error.response?.data) {
              console.error('错误详情:', error.response.data);
              throw new Error(error.response.data.message || error.response.data.error || '创建账户失败');
          }
          throw error;
      }
  }

  async updateAccount(id: number, request: AccountRequest): Promise<AccountDTO> {
      const response = await apiClient.put<AccountDTO>(`/accounts/${id}`, request);
      return response.data;
  }

  async deleteAccount(id: number): Promise<void> {
      await apiClient.delete(`/accounts/${id}`);
  }

  async getSubAccounts(): Promise<SubAccountDTO[]> {
      const response = await apiClient.get<SubAccountDTO[]>('/sub-accounts');
      return response.data;
  }
}

export const accountsService = new AccountsService();