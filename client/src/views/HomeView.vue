<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useAuthStore } from '@/store/modules/auth';
import { useAccountsStore } from '@/store/modules/accounts';
import { useTransactionStore } from '@/store/modules/transactions';
import { useRouter } from 'vue-router';
import { accountsService } from '@/api/services/accounts';
import { transactionsService } from '@/api/services/transactions';
import { usersService } from '@/api/services/users';
import type { AccountDTO } from '@/api/models/accounts';
import type { TransactionDTO } from '@/api/models/transactions/transaction-dto';
import type { TransactionTypeDTO } from '@/api/models/transactions/transaction-type-dto';
import type { UserDTO } from '@/api/models/user/user-dto';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import AccountForm from '@/components/accounts/AccountForm.vue';

const authStore = useAuthStore();
const accountsStore = useAccountsStore();
const transactionStore = useTransactionStore();
const router = useRouter();

const loading = ref(false);
const error = ref<string | null>(null);

const currentUser = computed(() => authStore.currentUser);
const isAdmin = computed(() => currentUser.value?.role === 'ADMIN');
const accounts = computed(() => accountsStore.accounts);
const recentTransactions = computed(() => transactionStore.transactions.slice(0, 5));
const transactionTypes = ref<TransactionTypeDTO[]>([]);
const showAddAccount = ref(false);

const ACCOUNT_TYPES = [
  { accountId: 1, typeId: 1, accountName: '现金账户' },
  { accountId: 2, typeId: 2, accountName: '工商银行' },
  { accountId: 3, typeId: 3, accountName: '支付宝账户' },
  { accountId: 4, typeId: 4, accountName: '微信钱包' }
] as const;

interface TransactionWithUser extends TransactionDTO {
  user?: UserDTO;
}

const fetchData = async () => {
  loading.value = true;
  error.value = null;
  try {
    await Promise.all([
      accountsStore.fetchAccounts(),
      transactionStore.fetchTransactions(),
      fetchTransactionTypes()
    ]);
  } catch (err: any) {
    error.value = err.message || '获取数据失败';
  } finally {
    loading.value = false;
  }
};

const fetchAccounts = async () => {
  try {
    if (isAdmin.value) {
      // 管理员获取所有顶级账户
      const result = await accountsService.getAccounts();
      accountsStore.accounts = result;
    } else {
      // 普通用户获取可访问的子账户
      const subAccounts = await accountsService.getSubAccounts();
      console.log('获取到的子账户:', subAccounts);
      
      // 将子账户按顶级账户分组
      const accountsMap = new Map<number, AccountDTO>();
      
      // 为每个顶级账户创建基础结构
      ACCOUNT_TYPES.forEach(account => {
        accountsMap.set(account.accountId, {
          accountId: account.accountId,
          accountName: account.accountName,
          typeId: account.typeId,
          type: getAccountTypeName(account.typeId),
          userId: 0,
          username: '',
          subAccounts: []
        });
      });
      
      // 将子账户添加到对应的顶级账户中
      for (const subAccount of subAccounts) {
        const account = accountsMap.get(subAccount.accountId);
        if (account) {
          account.subAccounts = account.subAccounts || [];
          account.subAccounts.push(subAccount);
        }
      }
      
      accountsStore.accounts = Array.from(accountsMap.values());
      console.log('处理后的账户结构:', accountsStore.accounts);
    }
  } catch (err: any) {
    error.value = err.message || '获取账户列表失败';
  }
};

const fetchRecentTransactions = async () => {
  try {
    // 获取最近5条交易记录
    const result = await transactionsService.getTransactions({
      size: 5,
      page: 0
    });
    
    // 获取所有相关用户的信息
    const userIds = [...new Set(result.transactions.map(t => t.userId))];
    const users = await Promise.all(
      userIds.map(id => usersService.getUser(id))
    );
    
    // 将用户信息添加到交易记录中
    const transactionsWithUsers: TransactionWithUser[] = result.transactions.map(transaction => ({
      ...transaction,
      user: users.find(u => u.userId === transaction.userId)
    }));
    
    transactionStore.transactions = transactionsWithUsers;
  } catch (err: any) {
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

const getAccountName = (subAccountId: number) => {
  const account = accountsStore.accounts.find(a => a.subAccounts?.some(sa => sa.subAccountId === subAccountId));
  if (!account) return '未知账户';
  const subAccount = account.subAccounts?.find(sa => sa.subAccountId === subAccountId);
  return subAccount ? `${subAccount.accountName} (${subAccount.accountNumber})` : '未知账户';
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
  showAddAccount.value = false;
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
  const accountType = ACCOUNT_TYPES.find(type => type.typeId === typeId);
  return accountType?.accountName || '未知类型';
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

onMounted(async () => {
  try {
    loading.value = true;
    await Promise.all([
      fetchAccounts(),
      fetchRecentTransactions(),
      fetchTransactionTypes()
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
        <button @click="debugGetUser(1)" class="debug-btn">调试: 获取用户1</button>
        <button @click="debugGetUser(2)" class="debug-btn">调试: 获取用户2</button>
        <button @click="showAddAccount = true" class="add-btn">新增账户</button>
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
        <div class="account-card" v-for="account in accounts" :key="account.accountId" @click="navigateToAccount(account)">
          <h3>{{ account.accountName }}</h3>
          <div class="account-info">
            <div class="account-balance">
              <span class="balance-label">余额：</span>
              <span class="balance-amount">￥{{ Number(account.subAccounts?.[0]?.balance || 0).toFixed(2) }}</span>
            </div>
            <div class="account-details">
              <div class="account-number">{{ account.subAccounts?.[0]?.accountNumber || '无账号' }}</div>
              <div class="account-type">{{ getAccountTypeName(account.typeId) }}</div>
              <div class="account-users">
                <span class="label">关联用户：</span>
                <span>{{ getAssociatedUsers(account.subAccounts) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 最近交易记录 -->
      <div class="recent-transactions">
        <div class="section-header">
          <h2>最近交易</h2>
          <button @click="navigateToTransactions" class="view-all-btn">查看全部</button>
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
                  {{ transaction.user?.username || '未知用户' }}
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

    <!-- 新增账户对话框 -->
    <div v-if="showAddAccount" class="modal">
      <div class="modal-content">
        <AccountForm
          @submit="handleAccountSubmit"
          @cancel="showAddAccount = false"
        />
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

.add-btn {
  background-color: var(--primary-color);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.add-btn:hover {
  background-color: var(--primary-color-dark);
}

.accounts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
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

.account-balance {
  margin-bottom: 8px;
}

.balance-label {
  color: #666;
  font-size: 0.9rem;
}

.balance-amount {
  font-size: 1.2rem;
  font-weight: bold;
  color: #333;
}

.account-details {
  font-size: 0.9rem;
  color: #666;
}

.account-number {
  margin-bottom: 4px;
}

.account-type {
  margin-bottom: 4px;
}

.account-users {
  margin-top: 4px;
  font-size: 0.9rem;
  color: #666;
}

.account-users .label {
  color: #999;
  margin-right: 4px;
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
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
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
</style>