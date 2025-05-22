<template>
  <div class="account-detail-view">
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else-if="error" class="error">
      {{ error }}
    </div>

    <div v-else-if="account" class="account-detail">
      <div class="header">
        <h1>{{ account.accountName }}</h1>
        <div class="actions">
          <button @click="editAccount" class="edit-btn">编辑</button>
          <button @click="deleteAccount" class="delete-btn">删除</button>
        </div>
      </div>

      <div class="info-section">
        <div class="info-card">
          <h3>基本信息</h3>
          <div class="info-content">
            <p><strong>账户类型：</strong>{{ getAccountTypeName(account.type) }}</p>
            <p><strong>当前余额：</strong>¥{{ account.subAccounts?.[0]?.balance || '0.00' }}</p>
            <p><strong>所属用户：</strong>{{ account.username || '未知用户' }}</p>
            <p><strong>描述：</strong>{{ account.description || '无' }}</p>
          </div>
        </div>

        <div class="info-card">
          <h3>交易记录</h3>
          <div class="transactions-list">
            <div v-if="transactions.length === 0" class="no-data">
              暂无交易记录
            </div>
            <div v-else v-for="transaction in transactions" :key="transaction.transactionId" class="transaction-item">
              <div class="transaction-info">
                <span class="amount" :class="getIncomeClass(transaction.typeId)">
                  {{ isIncome(transaction.typeId) ? '+' : '-' }}¥{{ transaction.amount }}
                </span>
                <span class="description">{{ transaction.description }}</span>
              </div>
              <span class="date">{{ formatTransactionDate(transaction.transactionDate) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑账户模态框 -->
    <div v-if="showEditModal" class="modal">
      <div class="modal-content">
        <h2>编辑账户</h2>
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
              <option :value="AccountRequestTypeEnum.Bank">银行账户</option>
              <option :value="AccountRequestTypeEnum.Wechat">微信</option>
              <option :value="AccountRequestTypeEnum.Alipay">支付宝</option>
              <option :value="AccountRequestTypeEnum.Cash">现金</option>
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
import { useRoute, useRouter } from 'vue-router';
import { useAccountsStore } from '@/store/modules/accounts';
import { useTransactionStore } from '@/store/modules/transactions';
import type { AccountRequest } from '@/api/models/accounts/account-request';
import { AccountRequestTypeEnum } from '@/api/models/accounts/account-request';
import type { AccountDTO } from '@/api/models/accounts';
import type { AccountType } from '@/api/models/accounts/account-type';
import type { TransactionDTO } from '@/api/models/transactions';
import type { TransactionTypeDTO } from '@/api/models/transactions/transaction-type-dto';
import { transactionsService } from '@/api/services/transactions';

const route = useRoute();
const router = useRouter();
const accountsStore = useAccountsStore();
const transactionStore = useTransactionStore();

const loading = ref(false);
const error = ref<string | null>(null);
const showEditModal = ref(false);

const account = computed(() => {
  const accountId = Number(route.params.id);
  return accountsStore.accounts.find(a => a.accountId === accountId);
});

const transactions = computed(() => {
  const accountId = Number(route.params.id);
  return transactionStore.transactions.filter(t => t.accountId === accountId);
});

const form = ref<AccountRequest>({
  accountName: '',
  typeId: 1,
  type: AccountRequestTypeEnum.Bank
});

const transactionTypes = ref<TransactionTypeDTO[]>([]);

const getAccountTypeName = (type: string | undefined): string => {
  if (!type) return '未知';
  return type;
};

const formatDate = (date: string | undefined): string => {
  if (!date) return '未知';
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const formatTransactionDate = (date: string): string => {
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const fetchData = async () => {
  loading.value = true;
  error.value = null;
  try {
    await Promise.all([
      accountsStore.fetchAccounts(),
      transactionStore.fetchTransactions()
    ]);
  } catch (err: any) {
    error.value = err.message || '获取数据失败';
  } finally {
    loading.value = false;
  }
};

const fetchTransactionTypes = async () => {
  try {
    transactionTypes.value = await transactionsService.getTransactionTypes();
  } catch (err: any) {
    // 可选：处理错误
  }
};

const isIncome = (typeId: number) => {
  const type = transactionTypes.value.find(t => t.typeId === typeId);
  return type ? type.isIncome : false;
};

const getIncomeClass = (typeId: number) => isIncome(typeId) ? 'income' : 'expense';

const editAccount = () => {
  if (!account.value) return;
  
  form.value = {
    accountName: account.value.accountName || '',
    typeId: account.value.typeId || 1,
    type: account.value.type === '银行账户' ? AccountRequestTypeEnum.Bank :
          account.value.type === '微信' ? AccountRequestTypeEnum.Wechat :
          account.value.type === '支付宝' ? AccountRequestTypeEnum.Alipay :
          AccountRequestTypeEnum.Cash
  };
  showEditModal.value = true;
};

const deleteAccount = async () => {
  if (!account.value?.accountId) return;
  
  if (!confirm('确定要删除这个账户吗？')) return;
  
  try {
    await accountsStore.deleteAccount(account.value.accountId);
    router.push('/accounts');
  } catch (err: any) {
    error.value = err.message || '删除账户失败';
  }
};

const handleSubmit = async () => {
  if (!account.value?.accountId) return;
  
  try {
    await accountsStore.updateAccount(account.value.accountId, form.value);
    await fetchData();
    closeModal();
  } catch (err: any) {
    error.value = err.message || '更新账户失败';
  }
};

const closeModal = () => {
  showEditModal.value = false;
  form.value = {
    accountName: '',
    typeId: 1,
    type: AccountRequestTypeEnum.Bank
  };
};

onMounted(async () => {
  await fetchData();
  await fetchTransactionTypes();
});
</script>

<style scoped>
.account-detail-view {
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

.actions {
  display: flex;
  gap: 10px;
}

.edit-btn,
.delete-btn {
  padding: 8px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
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

.info-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.info-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.info-card h3 {
  margin: 0 0 15px 0;
  color: #333;
}

.info-content {
  display: grid;
  gap: 10px;
}

.info-content p {
  margin: 0;
  color: #666;
}

.transactions-list {
  display: grid;
  gap: 10px;
}

.no-data {
  text-align: center;
  color: #666;
  padding: 20px;
}

.transaction-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background: #f5f5f5;
  border-radius: 4px;
}

.transaction-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.amount {
  font-weight: bold;
}

.amount.income {
  color: #4CAF50;
}

.amount.expense {
  color: #f44336;
}

.description {
  color: #666;
}

.date {
  color: #999;
  font-size: 0.9rem;
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
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
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
</style> 