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
import { subAccountsService } from '@/api/services/subAccounts';
import type { AccountDTO } from '@/api/models/accounts';
import type { TransactionDTO } from '@/api/models/transactions/transaction-dto';
import type { TransactionTypeDTO } from '@/api/models/transactions/transaction-type-dto';
import type { UserDTO } from '@/api/models/user/user-dto';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import type { AccountTypeDTO } from '@/api/models/account-types/account-type-dto';
import type { TransactionQueryParams } from '@/api/models/transactions/transaction-query-params';
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
    const groups: Record<number, SubAccountDTO[]> = {};
    accountTypes.value.forEach(type => {
        groups[type.typeId] = [];
    });
    groups[0] = [];
    subAccounts.value.forEach(subAccount => {
        const account = accounts.value.find(a => a.accountId === subAccount.accountId);
        const typeId = account?.typeId || 0;
        if (!account) {
            console.warn(`子账户 ${subAccount.subAccountId} 的账户 ${subAccount.accountId} 未找到`);
        }
        if (!groups[typeId]) {
            groups[typeId] = [];
        }
        if (!groups[typeId].some(sa => sa.subAccountId === subAccount.subAccountId)) {
            groups[typeId].push(subAccount);
        }
    });
    console.log('分组子账户:', groups);
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
    loading.value = true;
    error.value = null;
    try {
        accounts.value = await accountsService.getAccounts();
        console.log('账户列表:', accounts.value);
        const allSubAccounts = await subAccountsService.getAllSubAccounts();
        subAccounts.value = allSubAccounts.map((subAccount: SubAccountDTO) => ({
            ...subAccount,
            users: subAccount.users || []
        }));
        console.log('子账户列表:', subAccounts.value);
        if (isAdmin.value) {
            users.value = await usersService.getUsers();
        } else {
            users.value = [await usersService.getCurrentUser()];
            // 为普通用户补齐 users
            subAccounts.value = subAccounts.value.map((subAccount: SubAccountDTO) => ({
                ...subAccount,
                users: subAccount.users?.length ? subAccount.users : [
                    { userId: currentUser.value.userId, username: currentUser.value.username }
                ]
            }));
        }
    } catch (err: any) {
        error.value = err.message || '获取账户列表失败';
    } finally {
        loading.value = false;
    }
};

const fetchRecentTransactions = async () => {
    try {
        const params: TransactionQueryParams = {
            page: 0,
            size: 5,
            sort: 'desc'
        };
        if (!isAdmin.value && currentUser.value?.userId) {
            params.userId = currentUser.value.userId;
        }
        const result = await transactionsService.getTransactions(params);
        if (!result.transactions?.length) return;

        const userIds = [...new Set(result.transactions.map(t => t.userId).filter(id => id))];
        const fetchedUsers = await Promise.all(
            userIds.map(id => usersService.getUser(id).catch(err => {
                console.warn(`获取用户 ${id} 失败:`, err);
                return null;
            }))
        ).then(users => users.filter(u => u !== null) as UserDTO[]);

        const transactionsWithUsers: TransactionDTO[] = result.transactions.map(transaction => {
            if (!subAccounts.value.some(sa => sa.subAccountId === transaction.subAccountId)) {
                console.warn(`交易 ${transaction.transactionId} 的子账户 ${transaction.subAccountId} 未找到`);
            }
            return {
                ...transaction,
                user: fetchedUsers.find(u => u.userId === transaction.userId) || users.value.find(u => u.userId === transaction.userId)
            };
        });

        transactionStore.transactions = transactionsWithUsers;
        console.log('最近交易:', transactionsWithUsers.map(t => ({
            transactionId: t.transactionId,
            userId: t.userId,
            username: t.user?.username || '未知用户',
            subAccountId: t.subAccountId,
            accountName: getAccountName(t.subAccountId)
        })));
    } catch (err) {
        console.error('获取最近交易记录失败:', err);
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
    const types = await accountTypesService.getAccountTypes();
    accountTypes.value = types;
  } catch (err: any) {
    console.error('获取账户类型失败:', err);
    error.value = err.message || '获取账户类型失败';
  }
};

const fetchUsers = async () => {
  if (!isAdmin.value) return;
  try {
    const usersList = await usersService.getUsers();
    users.value = [...users.value, ...usersList];
  } catch (err: any) {
    error.value = err.message || '获取用户列表失败';
  }
};

const getAccountName = (subAccountId: number) => {
  const subAccount = subAccounts.value.find(sa => sa.subAccountId === subAccountId);
  if (!subAccount) {
    console.warn(`子账户 ${subAccountId} 未找到在 subAccounts.value`);
    return '未知账户';
  }
  
  const account = accounts.value.find(a => a.accountId === subAccount.accountId);
  if (!account) {
    console.warn(`账户 ${subAccount.accountId} 未找到在 accounts.value`);
    return '未知账户';
  }
  
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
  if (!account) return;
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
  return user?.username || `未知用户(${userId})`;
};
const getAssociatedUsers = (subAccount: SubAccountDTO): string => {
    if (subAccount.users?.length) {
        return subAccount.users.map(u => u.username || getUserName(u.userId)).join(', ');
    }
    console.warn(`子账户 ${subAccount.subAccountId} 无关联用户`);
    return '无关联用户';
};

const debugSubAccount = async (subAccount: SubAccountDTO) => {
  try {
    console.log(`调试子账户 ${subAccount.subAccountId}...`);
    const account = accounts.value.find(a => a.accountId === subAccount.accountId);
    console.log('子账户信息:', {
      subAccountId: subAccount.subAccountId,
      accountName: subAccount.accountName,
      accountId: subAccount.accountId,
      accountNumber: subAccount.accountNumber,
      users: subAccount.users?.map(u => ({ userId: u.userId, username: u.username })) || []
    });
    console.log('所属账户信息:', {
      accountId: account?.accountId,
      accountName: account?.accountName,
      userId: account?.userId,
      username: account?.userId ? getUserName(account.userId) : '未知用户'
    });
    alert(`子账户信息:\n${JSON.stringify({
      subAccountId: subAccount.subAccountId,
      accountName: subAccount.accountName,
      accountNumber: subAccount.accountNumber,
      users: subAccount.users?.map(u => u.username) || []
    }, null, 2)}\n\n所属账户信息:\n${JSON.stringify({
      accountId: account?.accountId,
      accountName: account?.accountName,
      username: account?.userId ? getUserName(account.userId) : '未知用户'
    }, null, 2)}`);
  } catch (err: any) {
    console.error(`调试子账户失败:`, err);
    alert(`调试失败: ${err.message}`);
  }
};

const debugAccountOverview = async () => {
  try {
    console.log('调试账户概览...');
    console.log('当前用户:', currentUser.value);
    console.log('账户类型列表:', accountTypes.value);
    console.log('用户列表:', users.value.map(u => ({ userId: u.userId, username: u.username })));
    console.log('账户列表:', accounts.value.map(a => ({
      accountId: a.accountId,
      accountName: a.accountName,
      userId: a.userId,
      username: a.userId ? getUserName(a.userId) : '未知用户'
    })));
    console.log('子账户列表:', subAccounts.value.map(sa => ({
      subAccountId: sa.subAccountId,
      accountName: sa.accountName,
      accountId: sa.accountId,
      accountNumber: sa.accountNumber,
      users: sa.users?.map(u => u.username) || [],
      accountUser: accounts.value.find(a => a.accountId === sa.accountId)?.userId
        ? getUserName(accounts.value.find(a => a.accountId === sa.accountId)!.userId)
        : '无用户'
    })));
    console.log('分组后的子账户:', Object.entries(groupedSubAccounts.value).map(([typeId, subAccounts]) => ({
      typeId,
      typeName: typeId === '0' ? '未分类' : getAccountTypeName(Number(typeId)),
      subAccounts: subAccounts.map(sa => ({
        subAccountId: sa.subAccountId,
        accountName: sa.accountName,
        accountNumber: sa.accountNumber,
        accountUser: accounts.value.find(a => a.accountId === sa.accountId)?.userId
          ? getUserName(accounts.value.find(a => a.accountId === sa.accountId)!.userId)
          : '无用户'
      }))
    })));

    accountTypes.value.forEach(type => {
      const subAccountsInType = groupedSubAccounts.value[type.typeId] || [];
      console.log(`账户类型 ${type.typeName} (${type.typeId}) 的子账户数量:`, subAccountsInType.length);
      if (subAccountsInType.length === 0) {
        console.log(`警告: 账户类型 ${type.typeName} 没有子账户`);
        const accountsInType = accounts.value.filter(a => a.typeId === type.typeId);
        console.log(`该类型的账户数量:`, accountsInType.length);
        console.log('该类型的账户:', accountsInType.map(a => ({
          accountId: a.accountId,
          accountName: a.accountName,
          userId: a.userId,
          username: a.userId ? getUserName(a.userId) : '未知用户'
        })));
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
    await authStore.checkAuth();
    if (!authStore.currentUser) {
      error.value = '请先登录';
      router.push({ name: 'login' });
      return;
    }
    await fetchAccountTypes();
    await fetchAccounts();
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
        <div v-for="type in [...accountTypes, { typeId: 0, typeName: '未分类' }]" 
             :key="type.typeId" 
             class="account-type-section">
          <h2 class="account-type-title">{{ type.typeName }}</h2>
          <div class="sub-accounts-grid">
            <div v-if="groupedSubAccounts[type.typeId]?.length > 0">
              <div v-for="subAccount in groupedSubAccounts[type.typeId]" 
                   :key="subAccount.subAccountId" 
                   class="account-card"
                   @click="navigateToAccount(accounts.find(a => a.accountId === subAccount.accountId))">
                <div class="account-header">
                  <h3>{{ subAccount.accountName }}</h3>
                  <button @click.stop="debugSubAccount(subAccount)" class="debug-btn">调试</button>
                </div>
                <div class="account-info">
                  <div class="account-number">账号: {{ subAccount.accountNumber }}</div>
                  <div class="account-balance">余额: ¥{{ Number(subAccount.balance).toFixed(2) }}</div>
                  <div class="account-users">用户: {{ getAssociatedUsers(subAccount) }}</div>
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

.account-number, .account-users {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 5px;
}

.account-balance {
  font-size: 1.1rem;
  color: #2196F3;
  font-weight: bold;
  margin-bottom: 5px;
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
</style>