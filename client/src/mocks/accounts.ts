import type { Account } from '@/api/models/accounts/account';
import type { AccountType } from '@/api/models/accounts/account-type';
import type { AccountRequest, AccountRequestTypeEnum } from '@/api/models/accounts/account-request';

// 模拟账户数据
const mockAccounts: Account[] = [
  {
    accountId: 1,
    accountName: '现金账户',
    accountType: {
      typeId: 1,
      typeName: '现金'
    },
    subAccounts: []
  },
  {
    accountId: 2,
    accountName: '银行账户',
    accountType: {
      typeId: 2,
      typeName: '银行'
    },
    subAccounts: []
  }
];

// 模拟API响应
export const mockAccountsApi = {
  // 获取账户列表
  list: () => {
    return Promise.resolve({
      data: mockAccounts,
      status: 200,
      statusText: 'OK'
    });
  },

  // 创建账户
  create: (request: AccountRequest) => {
    const newAccount: Account = {
      accountId: mockAccounts.length + 1,
      accountName: request.accountName,
      accountType: {
        typeId: request.typeId,
        typeName: request.type
      },
      subAccounts: []
    };
    mockAccounts.push(newAccount);
    return Promise.resolve({
      data: newAccount,
      status: 201,
      statusText: 'Created'
    });
  },

  // 更新账户
  update: (id: string, request: AccountRequest) => {
    const accountId = parseInt(id);
    const index = mockAccounts.findIndex(acc => acc.accountId === accountId);
    if (index === -1) {
      return Promise.reject({
        status: 404,
        statusText: 'Not Found',
        data: { message: '账户不存在' }
      });
    }

    const updatedAccount = {
      ...mockAccounts[index],
      accountName: request.accountName,
      accountType: {
        typeId: request.typeId,
        typeName: request.type
      }
    };
    mockAccounts[index] = updatedAccount;

    return Promise.resolve({
      data: updatedAccount,
      status: 200,
      statusText: 'OK'
    });
  },

  // 删除账户
  delete: (id: string) => {
    const accountId = parseInt(id);
    const index = mockAccounts.findIndex(acc => acc.accountId === accountId);
    if (index === -1) {
      return Promise.reject({
        status: 404,
        statusText: 'Not Found',
        data: { message: '账户不存在' }
      });
    }

    mockAccounts.splice(index, 1);
    return Promise.resolve({
      data: null,
      status: 204,
      statusText: 'No Content'
    });
  }
}; 