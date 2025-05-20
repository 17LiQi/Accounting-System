<template>
  <div class="transactions-view">
    <h1>交易记录</h1>
    <div class="actions">
      <button class="add-transaction" @click="showCreateDialog = true">新建交易</button>
    </div>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="transactions-list">
      <div v-for="transaction in transactions" :key="transaction.transactionId" class="transaction-card">
        <div class="transaction-header">
          <h3>{{ transaction.description }}</h3>
          <span :class="['amount', transaction.typeId === 1 ? 'income' : 'expense']">
            {{ transaction.amount }}
          </span>
        </div>
        <p>日期: {{ transaction.transactionDate }}</p>
        <p>类型: {{ transactionTypes.find(t => t.typeId === transaction.typeId)?.typeName }}</p>
        <div class="actions">
          <button class="edit" @click="editTransaction(transaction)">编辑</button>
          <button @click="deleteTransaction(transaction.transactionId)" class="delete">删除</button>
        </div>
      </div>
    </div>

    <!-- 新建交易对话框 -->
    <div v-if="showCreateDialog" class="dialog">
      <div class="dialog-content">
        <h2>新建交易</h2>
        <form @submit.prevent="createTransaction">
          <div class="form-group">
            <label>描述</label>
            <input v-model="newTransaction.description" required>
          </div>
          <div class="form-group">
            <label>类型</label>
            <select v-model="newTransaction.typeId" required>
              <option v-for="type in transactionTypes" :key="type.typeId" :value="type.typeId">
                {{ type.typeName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>金额</label>
            <input v-model.number="newTransaction.amount" type="number" required>
          </div>
          <div class="form-group">
            <label>日期</label>
            <input v-model="newTransaction.transactionDate" type="date" required>
          </div>
          <div class="dialog-actions">
            <button type="submit">保存</button>
            <button type="button" @click="showCreateDialog = false">取消</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 编辑交易对话框 -->
    <div v-if="showEditDialog" class="dialog">
      <div class="dialog-content">
        <h2>编辑交易</h2>
        <form v-if="editingTransaction" @submit.prevent="saveTransaction">
          <div class="form-group">
            <label>描述</label>
            <input v-model="editingTransaction.description" required>
          </div>
          <div class="form-group">
            <label>类型</label>
            <select v-model="editingTransaction.typeId" required>
              <option v-for="type in transactionTypes" :key="type.typeId" :value="type.typeId">
                {{ type.typeName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>金额</label>
            <input v-model.number="editingTransaction.amount" type="number" required>
          </div>
          <div class="form-group">
            <label>日期</label>
            <input v-model="editingTransaction.transactionDate" type="date" required>
          </div>
          <div class="dialog-actions">
            <button type="submit">保存</button>
            <button type="button" @click="showEditDialog = false">取消</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useTransactionStore } from '@/store/modules/transactions';
import { useTransactionTypesStore } from '@/store/modules/transactionTypes';
import type { TransactionDTO, TransactionRequest } from '@/api/models/transactions';
import type { TransactionTypeDTO } from '@/api/models/transactionTypes';

const transactionsStore = useTransactionStore();
const transactionTypesStore = useTransactionTypesStore();

const loading = ref(false);
const error = ref<string | null>(null);
const transactions = ref<TransactionDTO[]>([]);
const transactionTypes = ref<TransactionTypeDTO[]>([]);
const showEditDialog = ref(false);
const editingTransaction = ref<TransactionDTO | null>(null);
const showCreateDialog = ref(false);
const newTransaction = ref<TransactionRequest>({
  description: '',
  typeId: 1,
  amount: 0,
  transactionDate: new Date().toISOString().split('T')[0],
  accountId: 1
});

onMounted(async () => {
  try {
    loading.value = true;
    await transactionsStore.fetchTransactions();
    transactions.value = transactionsStore.transactions;
    transactionTypes.value = await transactionTypesStore.getTransactionTypes();
    loading.value = false;
  } catch (err: any) {
    error.value = err.message || '加载交易列表失败';
    loading.value = false;
  }
});

async function editTransaction(transaction: TransactionDTO) {
  editingTransaction.value = { ...transaction };
  showEditDialog.value = true;
}

async function saveTransaction() {
  if (!editingTransaction.value) return;

  try {
    loading.value = true;
    const request: TransactionRequest = {
      description: editingTransaction.value.description,
      typeId: editingTransaction.value.typeId,
      amount: editingTransaction.value.amount,
      transactionDate: editingTransaction.value.transactionDate,
      accountId: editingTransaction.value.accountId
    };
    await transactionsStore.updateTransaction(editingTransaction.value.transactionId, request);
    showEditDialog.value = false;
  } catch (err: any) {
    error.value = err.message || '保存交易失败';
  } finally {
    loading.value = false;
  }
}

async function deleteTransaction(id: number) {
  if (!confirm('确定要删除这条交易记录吗？')) return;

  try {
    loading.value = true;
    await transactionsStore.deleteTransaction(id);
  } catch (err: any) {
    error.value = err.message || '删除交易失败';
  } finally {
    loading.value = false;
  }
}

async function createTransaction() {
  try {
    loading.value = true;
    await transactionsStore.createTransaction(newTransaction.value);
    showCreateDialog.value = false;
    await transactionsStore.fetchTransactions();
    transactions.value = transactionsStore.transactions;
  } catch (err: any) {
    error.value = err.message || '创建交易失败';
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.transactions-view {
  padding: 20px;
}

.loading, .error {
  text-align: center;
  padding: 20px;
}

.error {
  color: red;
}

.transactions-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.transaction-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  background: white;
}

.transaction-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.transaction-header h3 {
  margin: 0;
}

.amount {
  font-size: 1.2em;
  font-weight: bold;
}

.amount.income {
  color: #4CAF50;
}

.amount.expense {
  color: #f44336;
}

.actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
  margin-bottom: 20px;
}

button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  background: #4CAF50;
  color: white;
  cursor: pointer;
}

button.delete {
  background: #f44336;
}

.add-transaction {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  background: #4CAF50;
  color: white;
  cursor: pointer;
}

.dialog {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.dialog-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 100%;
  max-width: 500px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style> 