<template>
  <div class="account-detail">
    <h1>账户详情</h1>
    <div v-if="loading">加载中...</div>
    <div v-else-if="error">{{ error }}</div>
    <div v-else>
      <div class="account-info">
        <h2>{{ account.accountName }}</h2>
        <p>账户类型：{{ account.accountType.typeName }}</p>
        <p>余额：{{ account.balance }}</p>
      </div>
      <div class="transactions">
        <h3>交易记录</h3>
        <table>
          <thead>
            <tr>
              <th>日期</th>
              <th>类型</th>
              <th>金额</th>
              <th>描述</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="transaction in transactions" :key="transaction.id">
              <td>{{ transaction.date }}</td>
              <td>{{ transaction.type }}</td>
              <td>{{ transaction.amount }}</td>
              <td>{{ transaction.description }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { AccountService } from '@/api/services/AccountService';
import type { Account, Transaction } from '@/models/accounts';

const route = useRoute();
const account = ref<Account>({} as Account);
const transactions = ref<Transaction[]>([]);
const loading = ref(true);
const error = ref('');

onMounted(async () => {
  try {
    const accountId = Number(route.params.id);
    const [accountResponse, transactionsResponse] = await Promise.all([
      AccountService.getAccount(accountId),
      AccountService.getAccountTransactions(accountId),
    ]);
    account.value = accountResponse.data;
    transactions.value = transactionsResponse.data;
  } catch (e) {
    error.value = '加载账户详情失败';
    console.error(e);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.account-detail {
  padding: 2rem;
}

.account-info {
  margin-bottom: 2rem;
}

.transactions {
  margin-top: 2rem;
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
</style> 