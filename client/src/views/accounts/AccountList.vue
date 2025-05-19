<template>
  <div class="account-list">
    <h1>账户列表</h1>
    <div v-if="loading">加载中...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else>
      <table>
        <thead>
          <tr>
            <th>账户名称</th>
            <th>账户类型</th>
            <th>余额</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="account in accounts" :key="account.accountId">
            <td>{{ account.accountName }}</td>
            <td>{{ account.accountType.typeName }}</td>
            <td>{{ account.balance }}</td>
            <td>
              <router-link :to="`/accounts/${account.accountId}`">查看详情</router-link>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { AccountService } from '@/api/services/AccountService';
import type { Account } from '@/models/accounts';

const accounts = ref<Account[]>([]);
const loading = ref(true);
const error = ref('');

onMounted(async () => {
  try {
    const response = await AccountService.getAccounts();
    accounts.value = response.data;
  } catch (e) {
    error.value = '加载账户列表失败';
    console.error(e);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.account-list {
  padding: 2rem;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

th, td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f5f5f5;
}

a {
  color: #42b983;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style> 