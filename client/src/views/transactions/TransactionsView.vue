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
      <div class="sort-controls">
        <button @click="toggleSortOrder" class="sort-btn">
          {{ sortOrder === 'asc' ? '按最早日期排序' : '按最近日期排序' }}
        </button>
      </div>

      <div class="transactions-list">
        <div v-for="(group, date) in groupedTransactions" :key="date" class="transaction-group">
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
        <div v-if="!Object.keys(groupedTransactions).length" class="no-transactions">
          暂无交易记录
        </div>
      </div>

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
import { useRouter, useRoute } from 'vue-router';
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
const route = useRoute();
const loading = ref(false);
const error = ref<string | null>(null);
const transactions = ref<TransactionWithUser[]>([]);
const subAccounts = ref<SubAccountDTO[]>([]);
const transactionTypes = ref<TransactionTypeDTO[]>([]);
const users = ref<UserDTO[]>([]);
const accounts = ref<AccountDTO[]>([]);
const showAddTransaction = ref(false);
const editingTransaction = ref<TransactionWithUser | undefined>();
const currentPage = ref(1);
const pageSize = 10;
const totalItems = ref(0);
const dateRange = ref<{ start: string; end: string } | null>(null);
const selectedSubAccountId = ref<number | undefined>(undefined);
const selectedUserId = ref<number | undefined>(undefined);
const sortOrder = ref<'asc' | 'desc'>('desc');
const selectedAccountTypeId = ref<number | undefined>(undefined);

const authStore = useAuthStore();
const isAdmin = computed(() => authStore.currentUser?.role === 'ADMIN');

const accountCache = ref<Record<number, { accountName: string; accountNumber: string }>>({});

const preloadAccountInfo = async (subAccountId: number) => {
    if (accountCache.value[subAccountId]) return;
    try {
        const subAccount = await accountsService.getSubAccount(subAccountId);
        const account = await accountsService.getAccount(subAccount.accountId);
        accountCache.value[subAccountId] = {
            accountName: account.accountName,
            accountNumber: subAccount.accountNumber
        };
    } catch (err: any) {
        console.warn(`预加载子账户 ${subAccountId} 失败:`, err);
        accountCache.value[subAccountId] = {
            accountName: `未知账户 (ID: ${subAccountId})`,
            accountNumber: ''
        };
    }
};

const groupedTransactions = computed(() => {
    const groups: { [key: string]: TransactionWithUser[] } = {};
    transactions.value.forEach(transaction => {
        const date = new Date(transaction.time).toISOString().split('T')[0];
        if (!groups[date]) {
            groups[date] = [];
        }
        groups[date].push(transaction);
    });
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
            users.value = await usersService.getUsers();
        } else {
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
        const userIds = [...new Set(result.transactions.map(t => t.userId))];
        const usersList = await Promise.all(
            userIds.map(id => usersService.getUser(id))
        );
        transactions.value = result.transactions.map(transaction => ({
            ...transaction,
            user: usersList.find(u => u.userId === transaction.userId)
        }));
        totalItems.value = result.total;
        await Promise.all(
            [...new Set(transactions.value.map(t => t.subAccountId))].map(id => preloadAccountInfo(id))
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
    if (accountCache.value[subAccountId]) {
        const { accountName, accountNumber } = accountCache.value[subAccountId];
        return `${accountName} (${accountNumber})`;
    }
    const subAccount = subAccounts.value.find(sa => sa.subAccountId === subAccountId);
    if (subAccount) {
        const account = accounts.value.find(a => a.accountId === subAccount.accountId);
        if (account) {
            return `${account.accountName} (${subAccount.accountNumber})`;
        }
    }
    return `未知账户 (ID: ${subAccountId})`;
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
        await accountsService.recalculateBalance(transaction.subAccountId);
        await fetchTransactions();
    } catch (err: any) {
        error.value = '删除失败: ' + (err.message || '未知错误');
    }
};

const handleTransactionSubmit = async (transaction: TransactionWithUser) => {
    closeTransactionForm();
    await fetchTransactions();
    if (transactions.value.length === 0 && currentPage.value > 1) {
        currentPage.value = 1;
        await fetchTransactions();
    }
};

const closeTransactionForm = () => {
    showAddTransaction.value = false;
    editingTransaction.value = undefined;
};

const formatGroupDate = (dateString: string) => {
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

const totalPages = computed(() => Math.ceil(totalItems.value / pageSize));

const changePage = async (page: number) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
    await fetchTransactions();
};

const toggleSortOrder = () => {
    sortOrder.value = sortOrder.value === 'desc' ? 'asc' : 'desc';
    currentPage.value = 1;
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
        await Promise.all([
            fetchUsers(),
            fetchTransactions(),
            fetchSubAccounts(),
            fetchTransactionTypes(),
            fetchAccounts()
        ]);
        console.log('调试数据:', {
            transactions: transactions.value,
            subAccounts: subAccounts.value,
            accountCache: accountCache.value
        });
    } catch (err: any) {
        console.error('调试过程出错:', err);
    }
};

onMounted(async () => {
    const routeUserId = route.query.userId;
    if (!isAdmin.value && Number(routeUserId) !== authStore.currentUser?.userId) {
        selectedUserId.value = authStore.currentUser?.userId;
    } else if (routeUserId) {
        selectedUserId.value = Number(routeUserId);
    }
    try {
        loading.value = true;
        await Promise.all([
            fetchUsers(),
            fetchTransactions(),
            fetchSubAccounts(),
            fetchTransactionTypes(),
            fetchAccounts()
        ]);
    } catch (err: any) {
        error.value = err.message || '加载数据失败';
    } finally {
        loading.value = false;
    }
});
</script>

<style scoped>
.transactions-view {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h1 {
  margin: 0;
  color: #333;
  font-size: 1.8rem;
}

.header-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.add-btn {
  background-color: #2196f3;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.add-btn:hover {
  background-color: #1976d2;
}

.sort-controls {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 15px;
}

.sort-btn {
  background-color: #2196f3;
  color: white;
  border: none;
  padding: 6px 14px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.95rem;
}

.sort-btn:hover {
  background-color: #1976d2;
}

.transactions-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.transaction-group {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.date-header {
  background-color: #f5f5f5;
  padding: 10px 15px;
  font-weight: bold;
  color: #333;
  border-bottom: 1px solid #eee;
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
  margin-bottom: 8px;
}

.transaction-time {
  color: #666;
  font-size: 0.85rem;
}

.transaction-amount {
  font-weight: bold;
  font-size: 1.1rem;
}

.transaction-amount.income {
  color: #2e7d32;
}

.transaction-amount.expense {
  color: #f44336;
}

.transaction-details p {
  margin: 5px 0;
  color: #555;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 8px;
}

.label {
  color: #888;
  min-width: 50px;
  font-size: 0.85rem;
}

.transaction-actions {
  display: flex;
  gap: 8px;
  margin-top: 10px;
}

.edit-btn,
.delete-btn {
  padding: 4px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85rem;
}

.edit-btn {
  background-color: #2196f3;
  color: white;
}

.edit-btn:hover {
  background-color: #1976d2;
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
  gap: 15px;
  margin-top: 20px;
}

.page-btn {
  background-color: #2196f3;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.page-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.page-btn:hover:not(:disabled) {
  background-color: #1976d2;
}

.page-info {
  color: #666;
  font-size: 0.9rem;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
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
  padding: 20px;
}

.loading {
  text-align: center;
  padding: 40px;
}

.spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #2196f3;
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
  color: #f44336;
  text-align: center;
  padding: 20px;
}

.no-transactions {
  text-align: center;
  color: #666;
  padding: 20px;
  font-size: 1rem;
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

.user-select {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 0.9rem;
  min-width: 120px;
}
</style>