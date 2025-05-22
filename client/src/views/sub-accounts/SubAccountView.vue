<template>
  <div class="sub-account-view">
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else-if="error" class="error">
      {{ error }}
    </div>

    <div v-else-if="subAccount" class="sub-account-details">
      <div class="header">
        <h1>{{ subAccount.accountName }}</h1>
        <div class="header-actions">
          <button @click="showAddTransaction = true" class="add-btn">新增交易</button>
          <button @click="refreshBalance" class="refresh-btn">刷新余额</button>
          <button @click="debugFetchTransactions" class="debug-btn">调试获取交易</button>
          <button @click="goBack" class="back-btn">返回</button>
        </div>
      </div>

      <div v-if="debugInfo" class="debug-info">
        <h3>调试信息</h3>
        <pre>{{ JSON.stringify(debugInfo, null, 2) }}</pre>
      </div>

      <div class="info-card">
        <h2>账户信息</h2>
        <div class="info-grid">
          <div class="info-item">
            <label>账户名称</label>
            <span>{{ subAccount.accountName }}</span>
          </div>
          <div class="info-item">
            <label>账号</label>
            <span>{{ subAccount.accountNumber }}</span>
          </div>
          <div class="info-item">
            <label>卡类型</label>
            <span>{{ subAccount.cardType }}</span>
          </div>
          <div class="info-item">
            <label>余额</label>
            <span class="balance" :class="{ 'updating': balanceUpdating }">
              ¥{{ Number(subAccount.balance).toFixed(2) }}
            </span>
          </div>
        </div>
      </div>

      <div class="transactions-card">
        <h2>交易记录</h2>
        <!-- 排序按钮 -->
        <div class="sort-controls">
          <button @click="toggleSortOrder" class="sort-btn">
            {{ sortOrder === 'asc' ? '按最早日期排序' : '按最近日期排序' }}
          </button>
        </div>

        <div v-if="transactions.length > 0" class="transactions-list">
          <div v-for="(group, date) in groupedTransactions" :key="String(date)" class="transaction-group">
            <div class="date-header">{{ formatGroupDate(date) }}</div>
            <div class="transaction-items">
              <div v-for="transaction in group" :key="transaction.transactionId" class="transaction-item">
                <div class="transaction-info">
                  <div class="transaction-header">
                    <span class="transaction-time">{{ formatTime(transaction.time) }}</span>
                    <span class="transaction-amount" :class="{ 'income': transaction.isIncome, 'expense': !transaction.isIncome }">
                      {{ transaction.isIncome ? '+' : '-' }}¥{{ Number(transaction.amount).toFixed(2) }}
                    </span>
                  </div>
                  <div class="transaction-details">
                    <p class="transaction-type">
                      <span class="label">类型:</span>
                      {{ getTransactionTypeName(transaction.typeId) }}
                    </p>
                    <p class="transaction-remarks">
                      <span class="label">备注:</span>
                      {{ transaction.remarks || '无备注' }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="no-transactions">
          <p>暂无交易记录</p>
        </div>

        <!-- 分页控制 -->
        <div class="pagination-controls">
          <button 
            @click="changePage(currentPage - 1)" 
            :disabled="currentPage === 0"
            class="page-btn"
          >
            上一页
          </button>
          <span class="page-info">第 {{ currentPage + 1 }} 页</span>
          <button 
            @click="changePage(currentPage + 1)" 
            :disabled="!hasMorePages"
            class="page-btn"
          >
            下一页
          </button>
        </div>
      </div>
    </div>

    <!-- 新增交易记录对话框 -->
    <div v-if="showAddTransaction && subAccount" class="modal">
      <div class="modal-content">
        <TransactionForm
          :fixed-sub-account="subAccount"
          @submit="handleTransactionSubmit"
          @cancel="showAddTransaction = false"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { accountsService } from '@/api/services/accounts';
import { transactionsService } from '@/api/services/transactions';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import type { TransactionDTO } from '@/api/models/transactions/transaction-dto';
import type { TransactionTypeDTO } from '@/api/models/transactions/transaction-type-dto';
import TransactionForm from '@/components/transactions/TransactionForm.vue';

const route = useRoute();
const router = useRouter();
const loading = ref(true);
const error = ref<string | null>(null);
const subAccount = ref<SubAccountDTO | null>(null);
const transactions = ref<TransactionDTO[]>([]);
const debugInfo = ref<any>(null);
const balanceUpdating = ref(false);
const showAddTransaction = ref(false);
const currentPage = ref(0);
const pageSize = 10;
const hasMorePages = ref(true);
const transactionTypes = ref<TransactionTypeDTO[]>([]);
const sortOrder = ref<'desc' | 'asc'>('asc'); // 默认正序(最近日期在前)

// 按日期分组的交易记录，排序可切换
const groupedTransactions = computed(() => {
  // 先排序
  const sorted = [...transactions.value].sort((a, b) => {
    const t1 = new Date(a.time).getTime();
    const t2 = new Date(b.time).getTime();
    return sortOrder.value === 'asc' ? t2 - t1 : t1 - t2;
  });
  // 分组
  const groups: { [key: string]: TransactionDTO[] } = {};
  sorted.forEach(transaction => {
    const date = new Date(transaction.time).toISOString().split('T')[0];
    if (!groups[date]) {
      groups[date] = [];
    }
    groups[date].push(transaction);
  });
  // 日期分组排序
  const ordered = Object.entries(groups).sort((a, b) => {
    return sortOrder.value === 'asc'
      ? new Date(b[0]).getTime() - new Date(a[0]).getTime()
      : new Date(a[0]).getTime() - new Date(b[0]).getTime();
  });
  // 返回对象，保持v-for兼容
  return Object.fromEntries(ordered);
});

const fetchSubAccount = async () => {
  try {
    const id = Number(route.params.id);
    if (isNaN(id)) {
      throw new Error('无效的子账户ID');
    }
    subAccount.value = await accountsService.getSubAccount(id);
    // 获取交易记录
    const result = await transactionsService.getTransactions({
      subAccountId: id,
      size: pageSize,
      page: currentPage.value
    });
    transactions.value = result.transactions;
    hasMorePages.value = result.transactions.length === pageSize;
  } catch (err: any) {
    error.value = err.message || '获取子账户信息失败';
  } finally {
    loading.value = false;
  }
};

const fetchTransactionTypes = async () => {
  try {
    transactionTypes.value = await transactionsService.getTransactionTypes();
  } catch (err) {
    console.error('获取交易类型列表失败:', err);
  }
};

const getTransactionTypeName = (typeId: number) => {
  const type = transactionTypes.value.find(t => t.typeId === typeId);
  return type ? type.typeName : '未知类型';
};

const formatGroupDate = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
};

const formatTime = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  });
};

const changePage = async (page: number) => {
  if (page < 0 || (page > currentPage.value && !hasMorePages.value)) return;
  currentPage.value = page;
  await fetchSubAccount();
};

const toggleSortOrder = () => {
  sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
};

const refreshBalance = async () => {
  if (!subAccount.value) return;
  
  try {
    balanceUpdating.value = true;
    error.value = null;
    
    const id = subAccount.value.subAccountId;
    // 使用重新计算余额的API
    const updatedAccount = await accountsService.recalculateBalance(id);
    subAccount.value = updatedAccount;
    
    // 同时刷新交易记录
    const result = await transactionsService.getTransactions({
      subAccountId: id,
      size: 10,
      page: 0
    });
    transactions.value = result.transactions;
    
    // 显示成功消息
    alert(`余额已更新为：¥${Number(updatedAccount.balance).toFixed(2)}`);
  } catch (err: any) {
    console.error('刷新余额失败:', err);
    const errorMessage = err.response?.data?.message || err.message || '未知错误';
    error.value = `刷新余额失败: ${errorMessage}`;
    alert(`刷新余额失败: ${errorMessage}`);
  } finally {
    balanceUpdating.value = false;
  }
};

const handleTransactionSubmit = async (transaction: TransactionDTO) => {
  showAddTransaction.value = false;
  await fetchSubAccount(); // 刷新账户信息和交易记录
};

const debugFetchTransactions = async () => {
  if (!subAccount.value) return;
  
  try {
    loading.value = true;
    error.value = null;
    debugInfo.value = null;

    console.log('开始获取交易记录...');
    const result = await transactionsService.getTransactions({
      subAccountId: subAccount.value.subAccountId,
      size: 10,
      page: 0
    });
    
    console.log('获取交易记录成功:', result);
    debugInfo.value = {
      success: true,
      subAccountId: subAccount.value.subAccountId,
      transactions: result,
      requestInfo: {
        url: '/transactions',
        params: {
          subAccountId: subAccount.value.subAccountId,
          size: 10,
          page: 0
        }
      }
    };
    
    transactions.value = result.transactions;
  } catch (err: any) {
    console.error('获取交易记录失败:', err);
    error.value = err.message || '获取交易记录失败';
    debugInfo.value = {
      success: false,
      error: err,
      requestInfo: {
        url: '/transactions',
        params: {
          subAccountId: subAccount.value.subAccountId,
          size: 10,
          page: 0
        }
      }
    };
  } finally {
    loading.value = false;
  }
};

const goBack = () => {
  router.back();
};

onMounted(async () => {
  await Promise.all([
    fetchSubAccount(),
    fetchTransactionTypes()
  ]);
});
</script>

<style scoped>
.sub-account-view {
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

.back-btn {
  background-color: #666;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.back-btn:hover {
  background-color: #555;
}

.debug-btn {
  background-color: #666;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
}

.debug-btn:hover {
  background-color: #555;
}

.refresh-btn {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
}

.refresh-btn:hover {
  background-color: #45a049;
}

.add-btn {
  background-color: var(--primary-color);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 10px;
}

.add-btn:hover {
  background-color: var(--primary-color-dark);
}

.info-card,
.transactions-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.info-card h2,
.transactions-card h2 {
  margin: 0 0 20px 0;
  color: #333;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.info-item label {
  color: #666;
  font-size: 0.9rem;
}

.info-item span {
  color: #333;
  font-size: 1.1rem;
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

.transaction-remarks {
  margin: 0;
  font-size: 0.9rem;
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

.debug-info {
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 15px;
  margin: 20px 0;
  overflow-x: auto;
}

.debug-info h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.debug-info pre {
  margin: 0;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: monospace;
  font-size: 0.9rem;
  color: #666;
}

.balance {
  font-weight: bold;
  transition: color 0.3s ease;
}

.balance.updating {
  color: #666;
  animation: pulse 1s infinite;
}

@keyframes pulse {
  0% { opacity: 1; }
  50% { opacity: 0.5; }
  100% { opacity: 1; }
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

.transaction-group {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
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
}

.transaction-item:last-child {
  border-bottom: none;
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
</style> 