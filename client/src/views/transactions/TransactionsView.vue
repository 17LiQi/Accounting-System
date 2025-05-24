<template>
  <div class="transactions-view">
    <div class="header">
      <h1>交易记录</h1>
      <div class="header-actions">
        <select 
          v-if="isAdmin" 
          v-model="selectedUserId" 
          @change="fetchTransactions" 
          class="user-select"
        >
          <option :value="undefined">所有用户</option>
          <option v-for="user in users" :key="user.userId" :value="user.userId">
            {{ user.username }}
          </option>
        </select>
        <button @click="debugFetchData" class="debug-btn">调试: 重新获取数据</button>
        <button @click="showAddTransaction = true" class="add-btn">新增交易</button>
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
      <!-- 排序按钮 -->
      <div class="sort-controls">
        <button @click="toggleSortOrder" class="sort-btn">
          {{ sortOrder === 'asc' ? '按最早日期排序' : '按最近日期排序' }}
        </button>
      </div>

      <!-- 按日期分组的交易记录 -->
      <div class="transactions-list">
        <div v-for="(group, date) in groupedTransactions" :key="String(date)" class="transaction-group">
          <div class="date-header">{{ formatGroupDate(date) }}</div>
          <div class="transaction-items">
            <div 
              v-for="transaction in group" 
              :key="transaction.transactionId" 
              class="transaction-item"
              @click="navigateToSubAccount(transaction)"
            >
              <div class="transaction-info">
                <div class="transaction-header">
                  <span class="transaction-time">{{ formatTime(transaction.time) }}</span>
                  <span class="transaction-amount" :class="{ 'income': transaction.isIncome, 'expense': !transaction.isIncome }">
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
                <div class="transaction-actions">
                  <button @click.stop="editTransaction(transaction)" class="edit-btn">编辑</button>
                  <button @click.stop="deleteTransaction(transaction)" class="delete-btn">删除</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页控制 -->
      <div class="pagination-controls">
        <button 
          @click="changePage(currentPage - 1)" 
          :disabled="currentPage === 1"
          class="page-btn"
        >
          上一页
        </button>
        <span class="page-info">第 {{ currentPage }} 页，共 {{ totalPages }} 页</span>
        <button 
          @click="changePage(currentPage + 1)" 
          :disabled="currentPage >= totalPages"
          class="page-btn"
        >
          下一页
        </button>
      </div>
    </div>

    <!-- 新增/编辑交易记录对话框 -->
    <div v-if="showAddTransaction" class="modal">
      <div class="modal-content">
        <TransactionForm
          :transaction="editingTransaction"
          @submit="handleTransactionSubmit"
          @cancel="closeTransactionForm"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { transactionsService } from '@/api/services/transactions';
import { accountsService } from '@/api/services/accounts';
import { usersService } from '@/api/services/users';
import { useAuthStore } from '@/store/modules/auth';
import type { TransactionDTO } from '@/api/models/transactions/transaction-dto';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import type { TransactionTypeDTO } from '@/api/models/transactions/transaction-type-dto';
import type { UserDTO } from '@/api/models/user/user-dto';
import type { AccountDTO } from '@/api/models/accounts/account-dto';
import TransactionForm from '@/components/transactions/TransactionForm.vue';
import { useRoute } from 'vue-router';

const route = useRoute();

interface TransactionWithUser extends TransactionDTO {
  user?: UserDTO;
}

interface TransactionQueryParams {
  page: number;
  size: number;
  subAccountId?: number;
  startDate?: string;
  endDate?: string;
  userId?: number;
  sort: 'asc' | 'desc';
}

const router = useRouter();
const loading = ref(false);
const error = ref<string | null>(null);
const transactions = ref<TransactionDTO[]>([]);
const subAccounts = ref<SubAccountDTO[]>([]);
const transactionTypes = ref<TransactionTypeDTO[]>([]);
const users = ref<UserDTO[]>([]);
const showAddTransaction = ref(false);
const editingTransaction = ref<TransactionWithUser | undefined>();
const currentPage = ref(1);
const pageSize = 10;
const totalItems = ref(0);
const dateRange = ref<{ start: string; end: string } | null>(null);
const selectedSubAccountId = ref<number | undefined>(undefined);
const selectedUserId = ref<number | undefined>(undefined);
const sortOrder = ref<'asc' | 'desc'>('desc');
const accounts = ref<AccountDTO[]>([]);
const selectedAccountTypeId = ref<number | undefined>(undefined);

const authStore = useAuthStore();
const isAdmin = computed(() => authStore.currentUser?.role === 'ADMIN');

// 添加账户信息缓存
const accountCache = ref<Record<number, { accountName: string; accountNumber: string }>>({});

// 预加载账户信息
const preloadAccountInfo = async (subAccountId: number) => {
  if (accountCache.value[subAccountId]) return;
  
  try {
    const subAccount = await accountsService.getSubAccount(subAccountId);
    const account = await accountsService.getAccount(subAccount.accountId);
    accountCache.value[subAccountId] = {
      accountName: account.accountName,
      accountNumber: subAccount.accountNumber
    };
  } catch (err) {
    console.error('预加载账户信息失败:', err);
  }
};

// 按日期分组的交易记录
const groupedTransactions = computed(() => {
  // 分组
  const groups: { [key: string]: TransactionWithUser[] } = {};
  transactions.value.forEach(transaction => {
    const date = new Date(transaction.time).toISOString().split('T')[0];
    if (!groups[date]) {
      groups[date] = [];
    }
    groups[date].push(transaction);
  });
  
  // 日期分组排序
  const ordered = Object.entries(groups).sort((a, b) => {
    return sortOrder.value === 'desc'
      ? new Date(b[0]).getTime() - new Date(a[0]).getTime()
      : new Date(a[0]).getTime() - new Date(b[0]).getTime();
  });
  
  return Object.fromEntries(ordered);
});

const fetchUsers = async () => {
  try {
    if (isAdmin.value) {
      // 管理员获取所有用户列表
      const usersList = await usersService.getUsers();
      users.value = usersList;
    } else {
      // 普通用户只能看到自己
      const currentUser = authStore.currentUser;
      if (currentUser) {
        users.value = [currentUser];
        selectedUserId.value = currentUser.userId;
      }
    }
  } catch (err) {
    console.error('获取用户列表失败:', err);
  }
};

const fetchTransactions = async () => {
  loading.value = true;
  error.value = null;
  try {
    const params: TransactionQueryParams = {
      page: Math.max(0, currentPage.value - 1),
      size: pageSize,
      sort: sortOrder.value
    };
    
    if (selectedSubAccountId.value) {
      params.subAccountId = selectedSubAccountId.value;
    }
    
    if (dateRange.value?.start) {
      params.startDate = dateRange.value.start;
    }
    
    if (dateRange.value?.end) {
      params.endDate = dateRange.value.end;
    }
    
    if (selectedUserId.value) {
      params.userId = selectedUserId.value;
    }

    
    console.log('发送请求参数:', params);
    
    const result = await transactionsService.getTransactions(params);
    
    // 获取所有相关用户的信息
    const userIds = [...new Set(result.transactions.map(t => t.userId))];
    const usersList = await Promise.all(
      userIds.map(id => usersService.getUser(id))
    );
    
    // 将用户信息添加到交易记录中
    const transactionsWithUsers = result.transactions.map(transaction => ({
      ...transaction,
      user: usersList.find(u => u.userId === transaction.userId)
    }));
    
    // 更新交易记录
    transactions.value = transactionsWithUsers;
    totalItems.value = result.total;

    // 预加载所有交易记录的账户信息
    await Promise.all(
      transactionsWithUsers.map(t => preloadAccountInfo(t.subAccountId))
    );
  } catch (err: any) {
    console.error('获取交易记录失败:', err);
    error.value = err.message || '获取交易记录失败';
  } finally {
    loading.value = false;
  }
};

const fetchSubAccounts = async () => {
  try {
    subAccounts.value = await accountsService.getAllSubAccounts();
  } catch (err) {
    console.error('获取子账户列表失败:', err);
  }
};

const fetchTransactionTypes = async () => {
  try {
    transactionTypes.value = await transactionsService.getTransactionTypes();
  } catch (err) {
    console.error('获取交易类型列表失败:', err);
  }
};

const fetchAccounts = async () => {
  try {
    accounts.value = await accountsService.getAccounts();
  } catch (err) {
    console.error('获取账户列表失败:', err);
  }
};

const getAccountName = (subAccountId: number) => {
  const subAccount = subAccounts.value.find(sa => sa.subAccountId === subAccountId);
  if (!subAccount) return '未知账户';
  
  const account = accounts.value.find(a => a.accountId === subAccount.accountId);
  if (!account) return '未知账户';
  
  return `${account.accountName} (${subAccount.accountNumber})`;
};

const getTransactionTypeName = (typeId: number) => {
  const type = transactionTypes.value.find(t => t.typeId === typeId);
  return type ? type.typeName : '未知类型';
};

const editTransaction = (transaction: TransactionWithUser) => {
  editingTransaction.value = transaction;
  showAddTransaction.value = true;
};

const deleteTransaction = async (transaction: TransactionWithUser) => {
  if (!confirm('确定要删除这条交易记录吗？')) return;
  
  try {
    await transactionsService.deleteTransaction(transaction.transactionId);
    // 更新子账户余额
    await accountsService.recalculateBalance(transaction.subAccountId);
    // 刷新交易记录
    await fetchTransactions();
  } catch (err: any) {
    alert('删除失败: ' + (err.message || '未知错误'));
  }
};

const handleTransactionSubmit = async (transaction: TransactionWithUser) => {
  closeTransactionForm();
  // 强制重新获取数据
  await fetchTransactions();
  // 如果当前页面没有数据，自动跳转到第一页
  if (transactions.value.length === 0 && currentPage.value > 1) {
    currentPage.value = 1;
    await fetchTransactions();
  }
};

const closeTransactionForm = () => {
  showAddTransaction.value = false;
  editingTransaction.value = undefined;
};

const formatGroupDate = (dateString: string | number) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    timeZone: 'Asia/Shanghai'
  });
};

const formatTime = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit',
    timeZone: 'Asia/Shanghai'
  });
};

// 添加总页数计算属性
const totalPages = computed(() => Math.ceil(totalItems.value / pageSize));

// 修改changePage函数
const changePage = async (page: number) => {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
  await fetchTransactions();
};

// 修改toggleSortOrder函数
const toggleSortOrder = () => {
  sortOrder.value = sortOrder.value === 'desc' ? 'asc' : 'desc';
  currentPage.value = 1; // 切换排序时重置到第一页
  fetchTransactions();
};

const navigateToSubAccount = (transaction: TransactionWithUser) => {
  router.push({
    name: 'sub-account-details',
    params: { id: transaction.subAccountId }
  });
};

const debugFetchData = async () => {
  try {
    await fetchUsers();
    await fetchTransactions();
  } catch (err: any) {
    console.error('调试过程出错:', err);
  }
};

const handleUserFilterChange = () => {
  currentPage.value = 0;
  fetchTransactions();
};

const loadMore = async () => {
  if (loading.value || currentPage.value * pageSize >= totalItems.value) return;
  currentPage.value++;
  await fetchTransactions();
};

onMounted(async () => {
  const routeUserId = route.query.userId;
  if (!isAdmin.value && Number(routeUserId) !== authStore.currentUser?.userId) {
    selectedUserId.value = authStore.currentUser?.userId;
  } else if (routeUserId) {
    selectedUserId.value = Number(routeUserId);
  }
  await Promise.all([
    fetchUsers(),
    fetchTransactions(),
    fetchSubAccounts(),
    fetchTransactionTypes(),
    fetchAccounts()
  ]);
});
</script>

<style scoped>
.transactions-view {
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

.sort-controls {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.sort-btn {
  background-color: #2196F3;
  color: white;
  border: none;
  padding: 6px 14px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.95rem;
}

.sort-btn:hover {
  background-color: #1976D2;
}

.transactions-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.transaction-group {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.date-header {
  background-color: #f8f9fa;
  padding: 10px 15px;
  font-weight: bold;
  color: #333;
  border-bottom: 1px solid #dee2e6;
}

.transaction-items {
  display: flex;
  flex-direction: column;
}

.transaction-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background-color 0.2s;
}

.transaction-item:hover {
  background-color: #f8f9fa;
}

.transaction-item:last-child {
  border-bottom: none;
}

.transaction-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
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
  margin: 10px 0;
}

.transaction-details p {
  margin: 5px 0;
  display: flex;
  align-items: center;
  gap: 5px;
}

.label {
  color: #999;
  min-width: 50px;
}

.transaction-actions {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}

.edit-btn,
.delete-btn {
  padding: 4px 8px;
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
  background-color: #f44336;
  color: white;
}

.delete-btn:hover {
  background-color: #d32f2f;
}

.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.page-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  background-color: var(--primary-color);
  color: white;
  cursor: pointer;
}

.page-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.page-info {
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

.user-select {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 10px;
  min-width: 120px;
}
</style> 