<template>
  <div class="statistics-view">
    <h1>统计信息</h1>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else>
      <div class="statistics-cards">
        <div class="stat-card">
          <h3>总收入</h3>
          <p class="amount income">{{ statistics?.totalIncome || 0 }}</p>
        </div>
        <div class="stat-card">
          <h3>总支出</h3>
          <p class="amount expense">{{ statistics?.totalExpense || 0 }}</p>
        </div>
        <div class="stat-card">
          <h3>净收入</h3>
          <p class="amount" :class="{ income: statistics?.netIncome > 0, expense: statistics?.netIncome < 0 }">
            {{ statistics?.netIncome || 0 }}
          </p>
        </div>
      </div>

      <div v-if="statistics?.accountBalances?.length" class="account-balances">
        <h2>账户余额</h2>
        <div class="balance-list">
          <div v-for="account in statistics.accountBalances" :key="account.accountId" class="balance-item">
            <h4>{{ account.accountName }}</h4>
            <p class="amount">{{ account.balance }}</p>
          </div>
        </div>
      </div>

      <div v-if="statistics?.monthlyStatistics?.length" class="monthly-statistics">
        <h2>月度统计</h2>
        <div class="monthly-list">
          <div v-for="month in statistics.monthlyStatistics" :key="month.month" class="monthly-item">
            <h4>{{ month.month }}</h4>
            <p>收入: {{ month.income }}</p>
            <p>支出: {{ month.expense }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useStatisticsStore } from '@/store/modules/statistics';

const statisticsStore = useStatisticsStore();
const loading = ref(false);
const error = ref<string | null>(null);
const statistics = ref<any>(null);

const fetchStatistics = async () => {
  loading.value = true;
  error.value = null;
  try {
    const today = new Date();
    const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
    await statisticsStore.fetchStatistics(
      firstDayOfMonth.toISOString().split('T')[0],
      today.toISOString().split('T')[0]
    );
    statistics.value = statisticsStore.statistics;
  } catch (err: any) {
    error.value = err.message || '获取统计信息失败';
  } finally {
    loading.value = false;
  }
};

onMounted(fetchStatistics);
</script>

<style scoped>
.statistics-view {
  padding: 20px;
}

.loading,
.error {
  text-align: center;
  padding: 20px;
  font-size: 18px;
}

.error {
  color: red;
}

.statistics-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.stat-card h3 {
  margin: 0 0 10px 0;
  color: #666;
}

.amount {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

.income {
  color: #4CAF50;
}

.expense {
  color: #f44336;
}

.account-balances,
.monthly-statistics {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  margin: 0 0 20px 0;
  color: #333;
}

.balance-list,
.monthly-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.balance-item,
.monthly-item {
  padding: 15px;
  background: #f5f5f5;
  border-radius: 4px;
}

.balance-item h4,
.monthly-item h4 {
  margin: 0 0 10px 0;
  color: #333;
}

.monthly-item p {
  margin: 5px 0;
  color: #666;
}
</style> 