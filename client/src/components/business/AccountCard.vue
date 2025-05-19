<!-- src/components/business/AccountCard.vue -->
<template>
  <div class="account-card">
    <div class="account-info">
      <h3>{{ account.accountName }}</h3>
      <p class="type">类型：{{ account.accountType?.typeName || '未知' }}</p>
      <p class="sub-accounts">子账户数量：{{ account.subAccounts?.length || 0 }}</p>
    </div>
    <div class="account-actions">
      <button @click="$emit('edit', account)">编辑</button>
      <button @click="handleDelete" class="delete">删除</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Account } from '@/api/models/accounts/account';

const props = defineProps<{
  account: Account;
}>();

const emit = defineEmits<{
  (e: 'edit', account: Account): void;
  (e: 'delete', accountId: number): void;
}>();

const handleDelete = () => {
  if (props.account.accountId) {
    emit('delete', props.account.accountId);
  }
};
</script>

<script lang="ts">
export default {
  name: 'AccountCard'
};
</script>

<style scoped>
.account-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.account-info {
  margin-bottom: 12px;
}

.account-info h3 {
  margin: 0 0 8px 0;
  color: #333;
}

.type {
  color: #666;
  margin: 4px 0;
}

.sub-accounts {
  font-size: 1.2em;
  font-weight: bold;
  color: #4CAF50;
  margin: 4px 0;
}

.account-actions {
  display: flex;
  gap: 8px;
}

button {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: #4CAF50;
  color: white;
}

button:hover {
  opacity: 0.9;
}

button.delete {
  background-color: #f44336;
}
</style>