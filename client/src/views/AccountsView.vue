<template>
  <div class="accounts-view">
    <h1>账户管理</h1>
    <div class="actions">
      <button @click="showCreateDialog = true">添加账户</button>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="accounts-list">
      <AccountCard
        v-for="account in accounts"
        :key="account.accountId"
        :account="account"
        @edit="editAccount"
        @delete="deleteAccount"
      />
    </div>

    <!-- 创建/编辑账户对话框 -->
    <div v-if="showCreateDialog || showEditDialog" class="dialog">
      <div class="dialog-content">
        <h2>{{ showEditDialog ? '编辑账户' : '添加账户' }}</h2>
        <form @submit.prevent="saveAccount">
          <div class="form-group">
            <label>账户名称</label>
            <input v-if="editingAccount" v-model="editingAccount.accountName" required>
          </div>
          <div class="form-group">
            <label>账户类型</label>
            <select v-if="editingAccount" v-model="editingAccount.accountType" required>
              <option v-for="type in accountTypes" :key="type.typeId" :value="type.typeName">
                {{ type.typeName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>描述</label>
            <textarea v-if="editingAccount" v-model="editingAccount.description" rows="3"></textarea>
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
import { useAccountStore } from '@/store/modules/accounts';
import { useAccountTypesStore } from '@/store/modules/accountTypes';
import type { AccountDTO } from '@/api/models/accounts';
import type { AccountTypeDTO } from '@/api/models/accountTypes';
import { AccountRequestTypeEnum } from '@/api/models/accounts/account-request';
import type { AccountRequest } from '@/api/models/accounts/account-request';
import AccountCard from '@/components/business/AccountCard.vue';

const accountStore = useAccountStore();
const accountTypesStore = useAccountTypesStore();

const accounts = ref<AccountDTO[]>([]);
const accountTypes = ref<AccountTypeDTO[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);
const showCreateDialog = ref(false);
const showEditDialog = ref(false);
const editingAccount = ref<AccountDTO | null>(null);
const newAccount = ref<AccountRequest>({
  accountName: '',
  typeId: 1,
  type: AccountRequestTypeEnum.Bank
});

onMounted(async () => {
  try {
    loading.value = true;
    error.value = null;
    await Promise.all([
      accountStore.fetchAccounts(),
      accountTypesStore.fetchAccountTypes()
    ]);
    accounts.value = accountStore.accounts;
    accountTypes.value = accountTypesStore.accountTypes;
  } catch (err: any) {
    error.value = err.message || '加载数据失败';
    console.error('加载数据失败:', err);
  } finally {
    loading.value = false;
  }
});

function getTypeClass(typeName: string): string {
  // 可根据类型名自定义颜色
  switch (typeName) {
    case 'BANK': return 'type-bank';
    case 'WECHAT': return 'type-wechat';
    case 'ALIPAY': return 'type-alipay';
    case 'OTHER': return 'type-other';
    default: return '';
  }
}

function editAccount(account: AccountDTO) {
  editingAccount.value = { ...account };
  showEditDialog.value = true;
}

async function saveAccount() {
  if (!editingAccount.value) return;
  try {
    loading.value = true;
    error.value = null;
    const request: AccountRequest = {
      accountName: editingAccount.value.accountName,
      typeId: accountTypes.value.find(t => t.typeName === editingAccount.value!.accountType)?.typeId || 1,
      type: editingAccount.value.accountType as AccountRequestTypeEnum
    };
    if (showEditDialog.value) {
      await accountStore.updateAccount(editingAccount.value.accountId, request);
    } else {
      await accountStore.createAccount(request);
    }
    closeDialog();
    accounts.value = await accountStore.fetchAccounts();
  } catch (err: any) {
    error.value = err.message || '保存账户失败';
    console.error('保存账户失败:', err);
  } finally {
    loading.value = false;
  }
}

async function deleteAccount(accountId: number) {
  if (!confirm('确定要删除这个账户吗？')) return;
  try {
    loading.value = true;
    error.value = null;
    await accountStore.deleteAccount(accountId);
    accounts.value = await accountStore.fetchAccounts();
  } catch (err: any) {
    error.value = err.message || '删除账户失败';
    console.error('删除账户失败:', err);
  } finally {
    loading.value = false;
  }
}

function closeDialog() {
  showCreateDialog.value = false;
  showEditDialog.value = false;
  editingAccount.value = null;
}

const handleCreateAccount = async () => {
  try {
    await accountStore.createAccount(newAccount.value);
    showCreateDialog.value = false;
    newAccount.value = {
      accountName: '',
      typeId: 1,
      type: AccountRequestTypeEnum.Bank
    };
  } catch (error) {
    console.error('创建账户失败:', error);
  }
};

const handleUpdateAccount = async () => {
  if (!editingAccount.value) return;
  
  try {
    const request: AccountRequest = {
      accountName: editingAccount.value.accountName,
      typeId: accountTypes.value.find(t => t.typeName === editingAccount.value!.accountType)?.typeId || 1,
      type: editingAccount.value.accountType as AccountRequestTypeEnum
    };
    await accountStore.updateAccount(editingAccount.value.accountId, request);
    showEditDialog.value = false;
  } catch (error) {
    console.error('更新账户失败:', error);
  }
};

const handleDeleteAccount = async (accountId: number) => {
  if (!confirm('确定要删除这个账户吗？')) return;
  
  try {
    await accountStore.deleteAccount(accountId);
  } catch (error) {
    console.error('删除账户失败:', error);
  }
};

const openEditDialog = (account: AccountDTO) => {
  editingAccount.value = { ...account };
  showEditDialog.value = true;
};

const closeEditDialog = () => {
  showEditDialog.value = false;
  editingAccount.value = null;
};
</script>

<style scoped>
.accounts-view {
  padding: 20px;
}

.actions {
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

.loading, .error {
  text-align: center;
  padding: 20px;
}

.error {
  color: red;
}

.accounts-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
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
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-group textarea {
  resize: vertical;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>