<!-- src/views/Accounts.vue -->
<template>
  <div class="accounts-container">
    <h1>账户管理</h1>
    <div v-if="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="accounts-list">
      <AccountCard
        v-for="account in accounts"
        :key="account.accountId"
        :account="account"
        @edit="handleEdit"
        @delete="handleDelete"
      />
    </div>

    <div class="create-account">
      <h2>创建账户</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>账户名称:</label>
          <input v-model="form.accountName" required />
        </div>
        <div class="form-group">
          <label>账户类型:</label>
          <select v-model="form.type" required>
            <option v-for="type in accountTypes" :key="type" :value="type">{{ type }}</option>
          </select>
        </div>
        <Button type="submit" :disabled="loading">创建</Button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useAccountStore } from '@/store/modules/accounts';
import type { Account, AccountRequest } from '@/api/models/accounts';
import AccountCard from '@/components/business/AccountCard.vue';
import Button from '@/components/common/Button.vue';

const store = useAccountStore();
const accounts = computed(() => store.accounts);
const loading = computed(() => store.loading);
const error = computed(() => store.error);

// 账户类型
const accountTypes = ['BANK', 'WECHAT', 'ALIPAY'];

const form = ref<AccountRequest>({
  accountName: '',
  typeId: 1,
  type: 'BANK',
});

const handleSubmit = async () => {
  try {
    await store.createAccount(form.value);
    form.value = { accountName: '', typeId: 1, type: 'BANK' };
  } catch (error) {
    console.error('创建账户失败:', error);
  }
};

const handleEdit = (account: Account) => {
  // TODO: 实现编辑功能
  console.log('编辑账户:', account);
};

const handleDelete = async (accountId: number) => {
  if (confirm('确定要删除这个账户吗？')) {
    try {
      await store.deleteAccount(accountId);
    } catch (error) {
      console.error('删除账户失败:', error);
    }
  }
};

// 初始加载账户列表
store.fetchAccounts();
</script>

<style scoped>
.accounts-container {
  padding: 20px;
}

.accounts-list {
  margin: 20px 0;
}

.create-account {
  margin-top: 40px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.error {
  color: red;
  margin: 20px 0;
}
</style>