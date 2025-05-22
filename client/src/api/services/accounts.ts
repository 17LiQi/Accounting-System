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
    const authStore = useAuthStore();
    const isAdmin = authStore.currentUser?.role === 'ADMIN';
    
    if (!isAdmin) {
      throw new Error('需要管理员权限');
    }
    
    // 获取所有顶级账户
    const response = await apiClient.get<AccountDTO[]>('/accounts');
    return response.data.map(account => normalizeAccountType(account));
  }

  async getAccount(id: number): Promise<AccountDTO> {
    const response = await apiClient.get<AccountDTO>(`/accounts/${id}`);
    return normalizeAccountType(response.data);
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
    // 1. 先获取当前子账户信息
    const currentAccount = await this.getSubAccount(subAccountId);
    
    // 2. 获取该子账户的所有交易记录（使用分页）
    let allTransactions: TransactionDTO[] = [];
    let page = 0;
    const pageSize = 100;
    
    while (true) {
      const response = await apiClient.get(`/transactions?subAccountId=${subAccountId}&size=${pageSize}&page=${page}`);
      const transactions = response.data.transactions;
      allTransactions = allTransactions.concat(transactions);
      
      // 如果返回的交易记录数小于页面大小，说明已经到达最后一页
      if (transactions.length < pageSize) {
        break;
      }
      page++;
    }
    
    // 3. 计算新的余额
    let newBalance = 0;
    for (const transaction of allTransactions) {
      if (transaction.isIncome) {
        newBalance += Number(transaction.amount);
      } else {
        newBalance -= Number(transaction.amount);
      }
    }
    
    // 4. 更新子账户余额
    const updateRequest = {
      accountId: currentAccount.accountId,
      accountName: currentAccount.accountName,
      accountNumber: currentAccount.accountNumber,
      cardType: currentAccount.cardType,
      balance: newBalance.toFixed(2)
    };
    
    console.log('更新请求数据:', updateRequest); // 添加调试日志
    
    const response = await apiClient.put(`/sub-accounts/${subAccountId}`, updateRequest);
    return response.data;
  }

  async createAccount(request: AccountRequest): Promise<AccountDTO> {
    const response = await apiClient.post<AccountDTO>('/accounts', request);
    return normalizeAccountType(response.data);
  }

  async updateAccount(id: number, request: AccountRequest): Promise<AccountDTO> {
    const response = await apiClient.put<AccountDTO>(`/accounts/${id}`, request);
    return normalizeAccountType(response.data);
  }

  async deleteAccount(id: number): Promise<void> {
    await apiClient.delete(`/accounts/${id}`);
  }

  async getSubAccounts(): Promise<SubAccountDTO[]> {
    // 获取所有子账户(普通用户只能看到与自己关联的子账户)
    const response = await apiClient.get<SubAccountDTO[]>('/sub-accounts');
    return response.data;
  }
}

export const accountsService = new AccountsService(); 