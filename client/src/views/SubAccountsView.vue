<template>
  <div class="sub-accounts-view">
    <h1>子账户管理</h1>
    <div class="account-selector">
      <div class="form-group">
        <label>选择主账户</label>
        <select v-model="selectedAccountId" @change="loadSubAccounts">
          <option v-for="account in accounts" :key="account.accountId" :value="account.accountId">
            {{ account.accountName }}
          </option>
        </select>
      </div>
      <button @click="showCreateDialog = true">添加子账户</button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="sub-accounts-list">
      <div v-for="subAccount in subAccounts" :key="subAccount.subAccountId" class="sub-account-card">
        <div class="sub-account-header">
          <h3>{{ subAccount.accountName }}</h3>
          <span :class="['balance', parseFloat(subAccount.balance) >= 0 ? 'positive' : 'negative']">
            {{ subAccount.balance }}
          </span>
        </div>
        <div class="sub-account-info">
          <p>账号: {{ subAccount.accountNumber }}</p>
          <p>类型: {{ subAccount.cardType }}</p>
        </div>
        <div class="actions">
          <button @click="editSubAccount(subAccount)">编辑</button>
          <button @click="deleteSubAccount(subAccount.subAccountId)" class="delete">删除</button>
        </div>
      </div>
    </div>

    <!-- 创建/编辑子账户对话框 -->
    <div v-if="showCreateDialog || showEditDialog" class="dialog">
      <div class="dialog-content">
        <h2>{{ showEditDialog ? '编辑子账户' : '添加子账户' }}</h2>
        <form @submit.prevent="saveSubAccount">
          <div class="form-group">
            <label>账户名称</label>
            <input v-if="editingSubAccount" v-model="editingSubAccount.accountName" required>
          </div>
          <div class="form-group">
            <label>账号</label>
            <input v-if="editingSubAccount" v-model="editingSubAccount.accountNumber" required>
          </div>
          <div class="form-group">
            <label>卡类型</label>
            <select v-if="editingSubAccount" v-model="editingSubAccount.cardType" required>
              <option value="DEBIT">借记卡</option>
              <option value="CREDIT">信用卡</option>
            </select>
          </div>
          <div class="form-group">
            <label>余额</label>
            <input v-if="editingSubAccount" v-model="editingSubAccount.balance" type="number" step="0.01" required>
          </div>
          <div class="dialog-actions">
            <button type="submit">保存</button>
            <button type="button" @click="closeDialog">取消</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useSubAccountsStore } from '@/store/modules/subAccounts';
import { useAccountStore } from '@/store/modules/accounts';
import type { SubAccountDTO } from '@/api/models/sub-accounts/sub-account-dto';
import type { SubAccountRequest } from '@/api/models/sub-accounts/sub-account-request';
import type { AccountDTO } from '@/api/models/accounts';

const subAccountsStore = useSubAccountsStore();
const accountsStore = useAccountStore();

const loading = ref(false);
const error = ref<string | null>(null);
const accounts = ref<AccountDTO[]>([]);
const selectedAccountId = ref<number | null>(null);
const subAccounts = ref<SubAccountDTO[]>([]);
const showCreateDialog = ref(false);
const showEditDialog = ref(false);
const editingSubAccount = ref<SubAccountDTO | null>(null);

onMounted(async () => {
  try {
    loading.value = true;
    accounts.value = await accountsStore.fetchAccounts();
    if (accounts.value.length > 0) {
      selectedAccountId.value = accounts.value[0].accountId;
      await loadSubAccounts();
    }
  } catch (err: any) {
    error.value = err.message || '加载账户列表失败';
  } finally {
    loading.value = false;
  }
});

async function loadSubAccounts() {
  if (!selectedAccountId.value) return;
  
  try {
    loading.value = true;
    subAccounts.value = await subAccountsStore.fetchSubAccounts(selectedAccountId.value);
  } catch (err: any) {
    error.value = err.message || '加载子账户列表失败';
  } finally {
    loading.value = false;
  }
}

function editSubAccount(subAccount: SubAccountDTO) {
  editingSubAccount.value = { ...subAccount };
  showEditDialog.value = true;
}

async function saveSubAccount() {
  if (!selectedAccountId.value || !editingSubAccount.value) return;
  
  try {
    loading.value = true;
    const request: SubAccountRequest = {
      accountId: selectedAccountId.value,
      accountName: editingSubAccount.value.accountName,
      accountNumber: editingSubAccount.value.accountNumber,
      cardType: editingSubAccount.value.cardType,
      balance: editingSubAccount.value.balance
    };
    
    if (showEditDialog.value) {
      await subAccountsStore.updateSubAccount(
        selectedAccountId.value,
        editingSubAccount.value.subAccountId,
        request
      );
    } else {
      await subAccountsStore.createSubAccount(selectedAccountId.value, request);
    }
    
    closeDialog();
    await loadSubAccounts();
  } catch (err: any) {
    error.value = err.message || '保存子账户失败';
  } finally {
    loading.value = false;
  }
}

async function deleteSubAccount(subAccountId: number) {
  if (!selectedAccountId.value) return;
  if (!confirm('确定要删除这个子账户吗？')) return;
  
  try {
    loading.value = true;
    await subAccountsStore.deleteSubAccount(selectedAccountId.value, subAccountId);
    await loadSubAccounts();
  } catch (err: any) {
    error.value = err.message || '删除子账户失败';
  } finally {
    loading.value = false;
  }
}

function closeDialog() {
  showCreateDialog.value = false;
  showEditDialog.value = false;
  editingSubAccount.value = null;
}
</script>

<style scoped>
.sub-accounts-view {
  padding: 20px;
}

.account-selector {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  align-items: flex-end;
}

.form-group {
  flex: 1;
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

button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  background: #4CAF50;
  color: white;
  cursor: pointer;
  height: 36px;
}

button.delete {
  background: #f44336;
}

.loading, .error {
  text-align: center;
  padding: 20px;
}

.error {
  color: red;
}

.sub-accounts-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.sub-account-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  background: white;
}

.sub-account-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.sub-account-header h3 {
  margin: 0;
}

.balance {
  font-size: 1.2em;
  font-weight: bold;
}

.balance.positive {
  color: #4CAF50;
}

.balance.negative {
  color: #f44336;
}

.actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
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

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.sub-account-info {
  margin: 10px 0;
  color: #666;
}

.sub-account-info p {
  margin: 5px 0;
}
</style> 