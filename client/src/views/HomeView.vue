<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useAuthStore } from '@/store/modules/auth';
import { useAccountsStore } from '@/store/modules/accounts';
import { useTransactionStore } from '@/store/modules/transactions';
import { useRouter } from 'vue-router';
import { accountsService } from '@/api/services/accounts';
import { transactionsService } from '@/api/services/transactions';
import { usersService } from '@/api/services/users';
import { accountTypesService } from '@/api/services/accountTypes';
import type { AccountDTO } from '@/api/models/accounts';
import type { TransactionDTO } from '@/api/models/transactions/transaction-dto';
import type { TransactionTypeDTO } from '@/api/models/transactions/transaction-type-dto';
import type { UserDTO } from '@/api/models/user/user-dto';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import type { AccountTypeDTO } from '@/api/models/account-types/account-type-dto';
import AccountForm from '@/components/accounts/AccountForm.vue';

const authStore = useAuthStore();
const accountsStore = useAccountsStore();
const transactionStore = useTransactionStore();
const router = useRouter();

const loading = ref(false);
const error = ref<string | null>(null);

const currentUser = computed(() => authStore.currentUser);
const isAdmin = computed(() => currentUser.value?.role === 'ADMIN');
const accounts = ref<AccountDTO[]>([]);
const subAccounts = ref<SubAccountDTO[]>([]);
const recentTransactions = computed(() => transactionStore.transactions.slice(0, 5));
const transactionTypes = ref<TransactionTypeDTO[]>([]);
const accountTypes = ref<AccountTypeDTO[]>([]);
const users = ref<UserDTO[]>([]);

// 按账户类型分组的子账户
const groupedSubAccounts = computed(() => {
  console.log('开始分组子账户...');
  console.log('当前账户类型:', accountTypes.value);
  console.log('当前账户列表:', accounts.value);
  console.log('当前子账户列表:', subAccounts.value);

  const groups: Record<number, SubAccountDTO[]> = {};
  
  // 初始化所有账户类型的分组
  accountTypes.value.forEach(type => {
    groups[type.typeId] = [];
  });
  
  // 将子账户按类型分组
  subAccounts.value.forEach(subAccount => {
    const account = accounts.value.find(a => a.accountId === subAccount.accountId);
    if (account) {
      console.log(`处理子账户 ${subAccount.subAccountId}:`, {
        subAccount,
        account,
        typeId: account.typeId
      });
      
      // 确保该类型的分组存在
      if (!groups[account.typeId]) {
        groups[account.typeId] = [];
      }
      
      // 检查是否已经添加过该子账户
      const existingIndex = groups[account.typeId].findIndex(
        sa => sa.subAccountId === subAccount.subAccountId
      );
      
      if (existingIndex === -1) {
        groups[account.typeId].push(subAccount);
        console.log(`已将子账户 ${subAccount.subAccountId} 添加到类型 ${account.typeId}`);
      } else {
        console.log(`子账户 ${subAccount.subAccountId} 已存在于类型 ${account.typeId}`);
      }
    } else {
      console.warn(`未找到子账户 ${subAccount.subAccountId} (${subAccount.accountName}) 对应的账户`);
    }
  });

  // 验证每个账户类型的子账户数量
  Object.entries(groups).forEach(([typeId, subAccounts]) => {
    const type = accountTypes.value.find(t => t.typeId === Number(typeId));
    console.log(`账户类型 ${type?.typeName} (${typeId}) 的子账户数量:`, subAccounts.length);
    if (subAccounts.length > 0) {
      console.log(`该类型的子账户:`, subAccounts.map(sa => ({
        id: sa.subAccountId,
        name: sa.accountName,
        accountId: sa.accountId
      })));
    }
  });

  console.log('分组结果:', groups);
  return groups;
});

interface TransactionWithUser extends TransactionDTO {
  user?: UserDTO;
}

const fetchData = async () => {
  loading.value = true;
  error.value = null;
  try {
    await Promise.all([
      fetchAccounts(),
      fetchRecentTransactions(),
      fetchTransactionTypes(),
      fetchAccountTypes(),
      fetchUsers()
    ]);
  } catch (err: any) {
    error.value = err.message || '获取数据失败';
  } finally {
    loading.value = false;
  }
};

const fetchAccounts = async () => {
  try {
    console.log('开始获取账户信息...');
    // 获取所有账户
    const accountsList = await accountsService.getAccounts();
    console.log('获取到的账户列表:', accountsList);
    accounts.value = accountsList;

    // 获取所有子账户
    const allSubAccounts = await accountsService.getAllSubAccounts();
    console.log('获取到的子账户列表:', allSubAccounts);
    subAccounts.value = allSubAccounts;

    // 验证账户和子账户的关联
    allSubAccounts.forEach(subAccount => {
      const account = accountsList.find(a => a.accountId === subAccount.accountId);
      if (!account) {
        console.warn(`警告: 子账户 ${subAccount.subAccountId} (${subAccount.accountName}) 未找到对应的账户`);
      } else {
        console.log(`子账户 ${subAccount.subAccountId} (${subAccount.accountName}) 关联到账户 ${account.accountId} (${account.accountName})`);
      }
    });

  } catch (err: any) {
    console.error('获取账户列表失败:', err);
    error.value = err.message || '获取账户列表失败';
  }
};

const fetchRecentTransactions = async () => {
  try {
    console.group('调试：获取最近交易记录');
    console.log('开始获取最近5条交易记录...');
    
    const result = await transactionsService.getTransactions({
      page: 0,
      size: 5,
      userId: currentUser.value?.userId
    });
    
    console.log('API返回结果:', result);
    
    if (!result.transactions || result.transactions.length === 0) {
      console.log('没有找到交易记录');
      console.groupEnd();
      return;
    }
    
    console.log('交易记录数量:', result.transactions.length);
    console.log('交易记录详情:', result.transactions);
    
    // 获取所有相关用户的信息
    const userIds = [...new Set(result.transactions.map(t => t.userId))];
    console.log('相关用户ID:', userIds);
    
    const users = await Promise.all(
      userIds.map(id => usersService.getUser(id))
    );
    console.log('用户信息:', users);
    
    // 将用户信息添加到交易记录中
    const transactionsWithUsers: TransactionWithUser[] = result.transactions.map(transaction => ({
      ...transaction,
      user: users.find(u => u.userId === transaction.userId)
    }));
    
    console.log('添加用户信息后的交易记录:', transactionsWithUsers);
    transactionStore.transactions = transactionsWithUsers;
    
    console.groupEnd();
  } catch (err) {
    console.error('获取最近交易记录失败:', err);
    console.groupEnd();
  }
};

const fetchTransactionTypes = async () => {
  try {
    transactionTypes.value = await transactionsService.getTransactionTypes();
  } catch (err) {
    console.error('获取交易类型列表失败:', err);
  }
};

const fetchAccountTypes = async () => {
  try {
    console.log('开始获取账户类型...');
    const types = await accountTypesService.getAccountTypes();
    console.log('获取到的账户类型:', types);
    accountTypes.value = types;
  } catch (err: any) {
    console.error('获取账户类型失败:', err);
    error.value = err.message || '获取账户类型失败';
  }
};

const fetchUsers = async () => {
  try {
    users.value = await usersService.getUsers();
  } catch (err: any) {
    error.value = err.message || '获取用户列表失败';
  }
};

const getAccountName = (subAccountId: number) => {
  const subAccount = subAccounts.value.find(sa => sa.subAccountId === subAccountId);
  if (!subAccount) return '未知账户';
  
  const account = accounts.value.find(a => a.accountId === subAccount.accountId);
  if (!account) return '未知账户';
  
  return `${account.accountName} (${subAccount.accountNumber})`;
};

const getTransactionTypeName = (typeId: number): string => {
  const type = transactionTypes.value.find(t => t.typeId === typeId);
  return type ? type.typeName : '未知类型';
};

const isIncome = (typeId: number) => {
  const type = transactionTypes.value.find(t => t.typeId === typeId);
  return type ? type.isIncome : false;
};

const getIncomeClass = (typeId: number) => isIncome(typeId) ? 'income' : 'expense';

const formatTime = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const handleAccountSubmit = async (account: AccountDTO) => {
  await fetchAccounts();
};

const navigateToAccount = (account: AccountDTO) => {
  router.push({
    name: 'account-details',
    params: { id: account.accountId }
  });
};

const navigateToTransactions = () => {
  router.push({ name: 'transactions' });
};

const debugGetUser = async (userId: number) => {
  try {
    const user = await usersService.getUser(userId);
    console.log('获取到的用户信息:', user);
    alert(`用户信息: ${JSON.stringify(user, null, 2)}`);
  } catch (err: any) {
    console.error('获取用户信息失败:', err);
    alert(`获取用户信息失败: ${err.message}`);
  }
};

const getAccountTypeName = (typeId: number): string => {
  const accountType = accountTypes.value.find(type => type.typeId === typeId);
  return accountType?.typeName || '未知类型';
};

const getUserName = (userId: number): string => {
  const user = users.value.find(u => u.userId === userId);
  return user?.username || '未知用户';
};

const getAssociatedUsers = (subAccounts: SubAccountDTO[] | undefined): string => {
  if (!subAccounts?.length) return '无关联用户';
  
  const usernames = new Set<string>();
  for (const subAccount of subAccounts) {
    const users = (subAccount as any).users as UserDTO[] | undefined;
    if (users?.length) {
      users.forEach(user => usernames.add(user.username));
    }
  }
  
  return usernames.size > 0 ? Array.from(usernames).join(', ') : '无关联用户';
};

const debugSubAccount = async (subAccount: SubAccountDTO) => {
  try {
    console.log(`开始获取子账户 ${subAccount.subAccountId} 的详细信息...`);
    const account = accounts.value.find(a => a.accountId === subAccount.accountId);
    console.log('子账户信息:', subAccount);
    console.log('所属账户信息:', account);
    alert(`子账户信息:\n${JSON.stringify(subAccount, null, 2)}\n\n所属账户信息:\n${JSON.stringify(account, null, 2)}`);
  } catch (err: any) {
    console.error(`获取子账户信息失败:`, err);
    alert(`获取失败: ${err.message}`);
  }
};

const debugAccountOverview = async () => {
  try {
    console.log('开始调试账户概览...');
    console.log('账户类型列表:', accountTypes.value);
    console.log('账户列表:', accounts.value);
    console.log('子账户列表:', subAccounts.value);
    console.log('分组后的子账户:', groupedSubAccounts.value);
    
    // 检查每个账户类型的子账户数量
    accountTypes.value.forEach(type => {
      const subAccountsInType = groupedSubAccounts.value[type.typeId] || [];
      console.log(`账户类型 ${type.typeName} (${type.typeId}) 的子账户数量:`, subAccountsInType.length);
      if (subAccountsInType.length === 0) {
        console.log(`警告: 账户类型 ${type.typeName} 没有子账户`);
        // 检查是否有账户属于该类型
        const accountsInType = accounts.value.filter(a => a.typeId === type.typeId);
        console.log(`该类型的账户数量:`, accountsInType.length);
        if (accountsInType.length > 0) {
          console.log('该类型的账户:', accountsInType);
        }
      }
    });
    
    alert('调试信息已输出到控制台，请按F12查看');
  } catch (err: any) {
    console.error('调试失败:', err);
    alert(`调试失败: ${err.message}`);
  }
};

onMounted(async () => {
  try {
    loading.value = true;
    // 先获取账户类型
    await fetchAccountTypes();
    // 然后获取账户和子账户
    await fetchAccounts();
    // 最后获取其他数据
    await Promise.all([
      transactionStore.fetchTransactions(),
      fetchRecentTransactions(),
      fetchTransactionTypes(),
      fetchUsers()
    ]);
  } catch (err: any) {
    error.value = err.message || '加载数据失败';
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="home-view">
    <div class="header">
      <h1>账户概览</h1>
      <div class="header-actions">
        <button @click="debugAccountOverview" class="debug-btn">调试账户概览</button>
        <button @click="fetchRecentTransactions" class="debug-btn">调试最近交易</button>
      </div>
    </div>

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else-if="error" class="error">
      {{ error }}
    </div>

    <div v-else>
      <!-- 账户卡片列表 -->
      <div class="accounts-grid">
        <div v-for="type in accountTypes" :key="type.typeId" class="account-type-section">
          <h2 class="account-type-title">{{ type.typeName }}</h2>
          <div class="sub-accounts-grid">
            <div v-if="groupedSubAccounts[type.typeId]?.length > 0">
              <div v-for="subAccount in groupedSubAccounts[type.typeId]" 
                   :key="subAccount.subAccountId" 
                   class="account-card"
                   @click="navigateToAccount(accounts.find(a => a.accountId === subAccount.accountId) || accounts[0])">
                <div class="account-header">
                  <h3>{{ subAccount.accountName }}</h3>
                </div>
                <div class="account-info">
                  <div class="account-number">账号: {{ subAccount.accountNumber }}</div>
                  <div class="account-balance">余额: ¥{{ Number(subAccount.balance).toFixed(2) }}</div>
                </div>
              </div>
            </div>
            <div v-else class="no-accounts">
              <p>暂无账户</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 最近交易记录 -->
      <div class="recent-transactions">
        <div class="section-header">
          <h2>最近交易</h2>
          <div class="header-actions">
            <button @click="fetchRecentTransactions" class="debug-btn">调试最近交易</button>
            <button @click="navigateToTransactions" class="view-all-btn">查看全部</button>
          </div>
        </div>
        <div v-if="recentTransactions.length > 0" class="transactions-list">
          <div v-for="transaction in recentTransactions" :key="transaction.transactionId" class="transaction-item">
            <div class="transaction-info">
              <div class="transaction-header">
                <span class="transaction-time">{{ formatTime(transaction.time) }}</span>
                <span class="transaction-amount" :class="getIncomeClass(transaction.typeId)">
                  {{ transaction.isIncome ? '+' : '-' }}¥{{ Number(transaction.amount).toFixed(2) }}
                </span>
              </div>
              <div class="transaction-details">
                <p class="transaction-account">
                  <span class="label">账户:</span>
                  {{ getAccountName(transaction.subAccountId) }}
                </p>
                <p class="transaction-type">
                  <span class="label">类型:</span>
                  {{ getTransactionTypeName(transaction.typeId) }}
                </p>
                <p class="transaction-user">
                  <span class="label">用户:</span>
                  {{ getUserName(transaction.userId) }}
                </p>
                <p class="transaction-remarks">
                  <span class="label">备注:</span>
                  {{ transaction.remarks || '无备注' }}
                </p>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="no-transactions">
          <p>暂无交易记录</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-view {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.header h1 {
  margin: 0;
  color: #333;
}

.accounts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.account-type-section {
  margin-bottom: 30px;
}

.account-type-title {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 2px solid #eee;
}

.sub-accounts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.account-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.2s;
}

.account-card:hover {
  transform: translateY(-2px);
}

.account-card h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.account-info {
  margin-top: 10px;
}

.account-number {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 5px;
}

.account-balance {
  font-size: 1.1rem;
  color: #2196F3;
  font-weight: bold;
}

.recent-transactions {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  margin: 0;
  color: #333;
}

.view-all-btn {
  background-color: #2196F3;
  color: white;
  border: none;
  padding: 6px 14px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.95rem;
}

.view-all-btn:hover {
  background-color: #1976D2;
}

.transactions-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.transaction-item {
  background: #f9f9f9;
  border-radius: 4px;
  padding: 15px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.transaction-item:hover {
  background-color: #f0f0f0;
}

.transaction-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.transaction-time {
  color: #666;
  font-size: 0.9rem;
}

.transaction-amount {
  font-weight: bold;
  font-size: 1.1rem;
}

.transaction-amount.income {
  color: #4CAF50;
}

.transaction-amount.expense {
  color: #f44336;
}

.transaction-details {
  color: #666;
}

.transaction-details p {
  margin: 5px 0;
  display: flex;
  align-items: center;
  gap: 5px;
}

.no-transactions {
  text-align: center;
  color: #666;
  padding: 20px;
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

.debug-btn {
  background-color: #ff9800;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
}

.debug-btn:hover {
  background-color: #f57c00;
}

.no-accounts {
  text-align: center;
  color: #666;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
  margin: 10px 0;
}

.account-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.debug-btn {
  background-color: #ff9800;
  color: white;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
}

.debug-btn:hover {
  background-color: #f57c00;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.debug-btn {
  background-color: #ff9800;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.debug-btn:hover {
  background-color: #f57c00;
}
</style>