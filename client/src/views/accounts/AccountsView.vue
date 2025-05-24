<template>
  <div class="accounts-view">
    <div class="header">
      <h1>账户管理</h1>
      <div class="header-actions">
        <button @click="debugUserAssociations" class="debug-btn">调试: 打印用户关联</button>
        <button @click="showCreateModal = true" class="create-btn">创建账户</button>
      </div>
    </div>

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else-if="error" class="error">
      {{ error }}
    </div>

    <div v-else class="accounts-container">
      <!-- 账户列表 -->
      <div class="accounts-list">
        <div v-for="account in accounts" :key="account.accountId" class="account-card">
          <div class="account-header">
            <h3>{{ account.accountName }}</h3>
            <div class="account-actions">
              <button @click="editAccount(account)" class="edit-btn">编辑</button>
              <button v-if="account.accountId" @click="deleteAccount(account.accountId)" class="delete-btn">删除</button>
            </div>
          </div>
          <div class="account-info">
            <p><strong>类型：</strong>{{ account.type }}</p>
            <p><strong>所属用户：</strong>{{ getAssociatedUsers(subAccounts[account.accountId]) }}</p>
            <div v-if="subAccounts[account.accountId]?.length > 0" class="sub-accounts">
              <h4>子账户列表：</h4>
              <div v-for="subAccount in subAccounts[account.accountId]" :key="subAccount.subAccountId" class="sub-account-item">
                <div class="sub-account-info">
                  <p><strong>账户名称：</strong>{{ subAccount.accountName }}</p>
                  <p><strong>账号：</strong>{{ subAccount.accountNumber }}</p>
                  <p><strong>卡类型：</strong>{{ subAccount.cardType }}</p>
                  <p><strong>余额：</strong>¥{{ Number(subAccount.balance).toFixed(2) }}</p>
                  <p><strong>关联用户：</strong>{{ getAssociatedUsers([subAccount]) }}</p>
                </div>
                <div class="sub-account-actions">
                  <button @click="viewSubAccountDetails(subAccount)" class="view-btn">查看详情</button>
                </div>
              </div>
            </div>
            <div v-else class="no-sub-accounts">
              <p>暂无子账户</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 创建/编辑账户模态框 -->
    <div v-if="showCreateModal" class="modal">
      <div class="modal-content">
        <h2>{{ editingAccount ? '编辑账户' : '创建账户' }}</h2>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="accountName">账户名称</label>
            <input
              id="accountName"
              v-model="form.accountName"
              type="text"
              required
              placeholder="请输入账户名称"
            />
          </div>

          <div class="form-group">
            <label for="type">账户类型</label>
            <select id="type" v-model="form.type" required>
              <option :value="AccountRequestTypeEnum.Cash">现金账户</option>
              <option :value="AccountRequestTypeEnum.Bank">银行账户</option>
              <option :value="AccountRequestTypeEnum.Wechat">微信</option>
              <option :value="AccountRequestTypeEnum.Alipay">支付宝</option>
            </select>
          </div>

          <div class="modal-actions">
            <button type="button" @click="closeModal" class="cancel-btn">取消</button>
            <button type="submit" class="submit-btn">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useAccountsStore } from '@/store/modules/accounts';
import type { AccountRequest } from '@/api/models/accounts/account-request';
import { AccountRequestTypeEnum } from '@/api/models/accounts/account-request';
import type { AccountDTO } from '@/api/models/accounts';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import type { AccountType } from '@/api/models/accounts/account-type';
import { accountsService } from '@/api/services/accounts';
import { useRouter } from 'vue-router';
import { usersService } from '@/api/services/users';
import type { UserDTO } from '@/api/models/user/user-dto';
import { subAccountsService } from '@/api/services/subAccounts';

// 扩展 SubAccountDTO 类型以包含用户信息
interface SubAccountWithUsers extends SubAccountDTO {
  users?: UserDTO[];
}

const accountsStore = useAccountsStore();
const router = useRouter();
const loading = ref(false);
const error = ref<string | null>(null);
const showCreateModal = ref(false);
const editingAccount = ref<AccountDTO | null>(null);
const debugInfo = ref<any>(null);

const form = ref<AccountRequest>({
  accountName: '',
  typeId: 1,
  type: AccountRequestTypeEnum.Bank
});

const accounts = ref<AccountDTO[]>([]);
const users = ref<UserDTO[]>([]);
const subAccounts = ref<Record<number, SubAccountWithUsers[]>>({});

const getAccountTypeName = (type: string | undefined): string => {
  if (!type) return '未知';
  return type;
};

const getAccountTypeId = (type: string | undefined): number => {
  if (!type) return 1;
  const typeMap: Record<string, number> = {
    '现金': 1,
    '银行卡': 2,
    '支付宝': 3,
    '微信': 4
  };
  return typeMap[type] || 1;
};

const getAccountRequestType = (type: string | undefined): AccountRequestTypeEnum => {
  if (!type) return AccountRequestTypeEnum.Bank;
  const typeMap: Record<string, AccountRequestTypeEnum> = {
    '现金': AccountRequestTypeEnum.Cash,
    '银行卡': AccountRequestTypeEnum.Bank,
    '支付宝': AccountRequestTypeEnum.Alipay,
    '微信': AccountRequestTypeEnum.Wechat
  };
  return typeMap[type] || AccountRequestTypeEnum.Bank;
};

const getUserName = (userId: number): string => {
  const user = users.value.find(u => u.userId === userId);
  return user ? user.username : '未知用户';
};

const fetchAccounts = async () => {
  loading.value = true;
  error.value = null;
  try {
    // 首先获取所有用户信息
    const usersList = await usersService.getUsers();
    users.value = usersList;
    console.log('获取到的用户列表:', usersList);

    // 获取所有账户
    const accountsList = await accountsService.getAccounts();
    console.log('获取到的账户列表:', accountsList);
    accounts.value = accountsList;

    // 获取所有子账户
    const allSubAccounts = await accountsService.getAllSubAccounts();
    console.log('获取到的子账户列表:', allSubAccounts);
    
    // 按账户ID分组子账户
    subAccounts.value = accountsList.reduce((acc, account) => {
      acc[account.accountId] = allSubAccounts.filter(
        subAccount => subAccount.accountId === account.accountId
      );
      return acc;
    }, {} as Record<number, SubAccountWithUsers[]>);

    // 为每个子账户关联用户信息
    for (const account of accountsList) {
      const accountSubAccounts = subAccounts.value[account.accountId] || [];
      for (const subAccount of accountSubAccounts) {
        try {
          if (!subAccount.subAccountId) {
            console.error('子账户ID无效:', subAccount);
            continue;
          }

          // 获取子账户详情
          const subAccountDetail = await accountsService.getSubAccount(subAccount.subAccountId);
          console.log(`子账户 ${subAccount.subAccountId} 的原始数据:`, subAccountDetail);
          
          // 处理用户信息
          const subAccountUsers = (subAccountDetail as any)?.users || [];
          subAccount.users = Array.isArray(subAccountUsers) ? subAccountUsers : Array.from(subAccountUsers);
          
          // 如果没有关联用户，则关联账户所属用户
          if (!subAccount.users || subAccount.users.length === 0) {
            // 如果账户没有指定用户，使用第一个用户作为默认用户
            const defaultUser = usersList[0];
            if (defaultUser) {
              subAccount.users = [defaultUser];
              console.log(`为子账户 ${subAccount.subAccountId} 关联默认用户:`, defaultUser.username);
            }
          }
        } catch (err) {
          console.error(`获取子账户 ${subAccount.subAccountId} 的用户信息失败:`, err);
          // 如果获取失败，使用默认用户
          const defaultUser = usersList[0];
          subAccount.users = defaultUser ? [defaultUser] : [];
        }
      }
    }

  } catch (err: any) {
    console.error('获取账户列表失败:', err);
    error.value = err.message || '获取账户列表失败';
  } finally {
    loading.value = false;
  }
};

const getAccountName = (accountId: number): string => {
  const account = accounts.value.find(a => a.accountId === accountId);
  return account ? account.accountName : '未知账户';
};

const editAccount = (account: AccountDTO) => {
  editingAccount.value = account;
  form.value = {
    accountName: account.accountName || '',
    typeId: getAccountTypeId(account.type),
    type: getAccountRequestType(account.type)
  };
  showCreateModal.value = true;
};

const deleteAccount = async (accountId: number) => {
  if (!confirm('确定要删除这个账户吗？')) return;
  
  try {
    await accountsStore.deleteAccount(accountId);
    await fetchAccounts();
  } catch (err: any) {
    error.value = err.message || '删除账户失败';
  }
};

const handleSubmit = async () => {
  try {
    if (editingAccount.value?.accountId) {
      await accountsStore.updateAccount(editingAccount.value.accountId, form.value);
    } else {
      await accountsStore.createAccount(form.value);
    }
    await fetchAccounts();
    closeModal();
  } catch (err: any) {
    error.value = err.message || '保存账户失败';
  }
};

const closeModal = () => {
  showCreateModal.value = false;
  editingAccount.value = null;
  form.value = {
    accountName: '',
    typeId: 1,
    type: AccountRequestTypeEnum.Bank
  };
};

const viewSubAccountDetails = (subAccount: SubAccountDTO) => {
  router.push(`/sub-accounts/${subAccount.subAccountId}`);
};

const getAssociatedUsers = (subAccounts: SubAccountWithUsers[] | undefined): string => {
  if (!subAccounts?.length) return '无关联用户';
  
  const usernames = new Set<string>();
  for (const subAccount of subAccounts) {
    if (subAccount.users?.length) {
      subAccount.users.forEach(user => {
        if (typeof user === 'object' && user !== null && 'username' in user) {
          usernames.add(user.username);
        }
      });
    }
  }
  
  return usernames.size > 0 ? Array.from(usernames).join(', ') : '无关联用户';
};

const debugUserAssociations = async () => {
  console.group('账户用户关联调试信息');
  
  // 打印所有账户信息
  console.log('所有账户:', accounts.value.map(account => ({
    accountId: account.accountId,
    accountName: account.accountName,
    userId: account.userId,
    username: users.value.find(u => u.userId === account.userId)?.username || '未指定用户'
  })));

  // 打印所有用户信息
  console.log('所有用户:', users.value.map(user => ({
    userId: user.userId,
    username: user.username,
    role: user.role
  })));

  // 打印子账户和用户关联信息
  console.log('子账户用户关联:');
  Object.entries(subAccounts.value).forEach(([accountId, subAccountsList]) => {
    console.group(`账户 ${accountId} 的子账户:`);
    subAccountsList.forEach(subAccount => {
      console.log(`子账户 ${subAccount.subAccountId}:`, {
        accountName: subAccount.accountName,
        accountNumber: subAccount.accountNumber,
        associatedUsers: subAccount.users?.map(user => ({
          userId: user.userId,
          username: user.username,
          role: user.role
        })) || []
      });
    });
    console.groupEnd();
  });

  // 打印用户关联统计
  const userStats = new Map<number, { username: string; role: string; subAccountCount: number }>();
  Object.values(subAccounts.value).flat().forEach(subAccount => {
    subAccount.users?.forEach(user => {
      if (!userStats.has(user.userId)) {
        userStats.set(user.userId, {
          username: user.username,
          role: user.role,
          subAccountCount: 0
        });
      }
      userStats.get(user.userId)!.subAccountCount++;
    });
  });
  console.log('用户关联统计:', Object.fromEntries(userStats));

  console.groupEnd();
};

onMounted(fetchAccounts);
</script>

<style scoped>
.accounts-view {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.create-btn {
  background-color: var(--primary-color);
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

.create-btn:hover {
  background-color: #45a049;
}

.loading {
  text-align: center;
  padding: 40px;
}

.spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid var(--primary-color);
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error {
  color: var(--danger-color);
  text-align: center;
  padding: 20px;
}

.accounts-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.account-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.account-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.account-header h3 {
  margin: 0;
  color: #333;
}

.account-actions {
  display: flex;
  gap: 10px;
  margin: 10px 0;
}

.edit-btn,
.delete-btn {
  padding: 5px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.edit-btn {
  background-color: #2196F3;
  color: white;
}

.edit-btn:hover {
  background-color: #1976D2;
}

.delete-btn {
  background-color: var(--danger-color);
  color: white;
}

.delete-btn:hover {
  background-color: #d32f2f;
}

.account-info {
  display: grid;
  gap: 10px;
}

.account-info p {
  margin: 0;
  color: #666;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 8px;
  width: 100%;
  max-width: 500px;
}

.modal-content h2 {
  margin: 0 0 20px 0;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #666;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.cancel-btn,
.submit-btn {
  padding: 8px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

.cancel-btn {
  background-color: #f5f5f5;
  color: #333;
}

.cancel-btn:hover {
  background-color: #e0e0e0;
}

.submit-btn {
  background-color: var(--primary-color);
  color: white;
}

.submit-btn:hover {
  background-color: #45a049;
}

.debug-info {
  margin: 20px 0;
  padding: 15px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow-x: auto;
}

.debug-info pre {
  margin: 0;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.sub-accounts {
  margin-top: 15px;
  border-top: 1px solid #eee;
  padding-top: 15px;
}

.sub-account-item {
  background: #f9f9f9;
  border-radius: 4px;
  padding: 10px;
  margin-bottom: 10px;
}

.sub-account-info {
  margin-bottom: 10px;
}

.sub-account-info p {
  margin: 5px 0;
}

.sub-account-actions {
  display: flex;
  justify-content: flex-end;
}

.view-btn {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
}

.view-btn:hover {
  background-color: #45a049;
}

.no-sub-accounts {
  color: #666;
  font-style: italic;
  text-align: center;
  padding: 10px;
}

.debug-btn {
  background-color: #2196F3;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  margin-right: 10px;
}

.debug-btn:hover {
  background-color: #1976D2;
}
</style> 