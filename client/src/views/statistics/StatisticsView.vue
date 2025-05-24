<template>
  <div class="statistics-view">
    <h1>统计信息</h1>

    <!-- 筛选区域 -->
    <div class="filters">
      <div class="filter-item">
        <label>周期</label>
        <select v-model="filters.period" @change="fetchStatistics">
          <option value="DAILY">每日</option>
          <option value="WEEKLY">每周</option>
          <option value="MONTHLY">每月</option>
          <option value="YEARLY">每年</option>
        </select>
      </div>
      <div class="filter-item">
        <label>年份</label>
        <input type="number" v-model="filters.year" min="2000" max="2100" @change="fetchStatistics" />
      </div>
      <div class="filter-item" v-if="filters.period === 'MONTHLY' || filters.period === 'DAILY'">
        <label>月份</label>
        <input type="number" v-model="filters.month" min="1" max="12" @change="fetchStatistics" />
      </div>
      <div class="filter-item" v-if="filters.period === 'WEEKLY'">
        <label>周次</label>
        <input type="number" v-model="filters.week" min="1" max="53" @change="fetchStatistics" />
      </div>
      <div class="filter-item" v-if="filters.period === 'DAILY'">
        <label>日期</label>
        <input type="number" v-model="filters.day" min="1" max="31" @change="fetchStatistics" />
      </div>
      <div class="filter-item" v-if="isAdmin">
        <label>用户</label>
        <select v-model="selectedUsername" @change="updateUserId">
          <option value="">所有用户</option>
          <option v-for="user in usersStore.users" :key="user.userId" :value="user.username">
            {{ user.username }}
          </option>
        </select>
      </div>
      <div class="filter-item">
        <label>账户</label>
        <select v-model="selectedAccountName" @change="updateAccountId">
          <option value="">所有账户</option>
          <option v-for="account in accountsStore.accounts" :key="account.accountId" :value="account.accountName">
            {{ account.accountName }}
          </option>
        </select>
      </div>
      <button class="refresh-btn" @click="fetchStatistics">刷新</button>
    </div>

    <!-- 加载和错误 -->
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else>
      <!-- 时间范围 -->
      <div class="time-range">
        <h2>{{ formatTimeRange() }}</h2>
      </div>

      <!-- 统计卡片 -->
      <div class="statistics-cards">
        <div class="stat-card">
          <h3>总收入</h3>
          <p class="amount income">{{ formatCurrency(statistics?.totalIncome || '0.00') }}</p>
        </div>
        <div class="stat-card">
          <h3>总支出</h3>
          <p class="amount expense">{{ formatCurrency(statistics?.totalExpense || '0.00') }}</p>
        </div>
        <div class="stat-card">
          <h3>净收入</h3>
          <p
            class="amount"
            :class="{ income: netIncome > 0, expense: netIncome < 0 }"
          >
            {{ formatCurrency(netIncome.toFixed(2)) }}
          </p>
        </div>
      </div>

      <!-- 收入按类型 -->
      <div v-if="statistics?.incomeByType?.length" class="income-by-type">
        <h2>收入按类型</h2>
        <div class="type-list">
          <div
            v-for="income in statistics.incomeByType"
            :key="income.typeId"
            class="type-item"
          >
            <h4>{{ income.typeName }}</h4>
            <p class="amount income">{{ formatCurrency(income.amount) }}</p>
          </div>
        </div>
        <canvas id="incomeChart"></canvas>
      </div>

      <!-- 支出按类型 -->
      <div v-if="statistics?.expenseByType?.length" class="expense-by-type">
        <h2>支出按类型</h2>
        <div class="type-list">
          <div
            v-for="expense in statistics.expenseByType"
            :key="expense.typeId"
            class="type-item"
          >
            <h4>{{ expense.typeName }}</h4>
            <p class="amount expense">{{ formatCurrency(expense.amount) }}</p>
          </div>
        </div>
        <canvas id="expenseChart"></canvas>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { statisticsService } from '@/api/services/statistics';
import { useUsersStore } from '@/store/modules/users';
import { useAccountsStore } from '@/store/modules/accounts';
import { useAuthStore } from '@/store/modules/auth';
import type { StatisticsResponse } from '@/api/models/statistics';
import Chart from 'chart.js/auto';

const loading = ref(false);
const error = ref<string | null>(null);
const statistics = ref<StatisticsResponse | null>(null);
const selectedUsername = ref<string>('');
const selectedAccountName = ref<string>('');
const filters = ref({
  period: 'MONTHLY',
  year: new Date().getFullYear(),
  month: new Date().getMonth() + 1,
  week: undefined as number | undefined,
  day: undefined as number | undefined,
  userId: undefined as number | undefined,
  accountId: undefined as number | undefined,
});

// 使用 Pinia stores
const usersStore = useUsersStore();
const accountsStore = useAccountsStore();
const authStore = useAuthStore();

// 判断是否为管理员
const isAdmin = computed(() => authStore.currentUser?.role === 'ADMIN');

// 计算净收入
const netIncome = computed(() => {
  if (!statistics.value) return 0;
  const income = parseFloat(statistics.value.totalIncome || '0');
  const expense = parseFloat(statistics.value.totalExpense || '0');
  return income - expense;
});

// 格式化时间范围
const formatTimeRange = () => {
  if (!statistics.value) return '';
  const { period, year, month, week, day } = statistics.value;
  switch (period) {
    case 'DAILY':
      return `${year}年${month}月${day}日`;
    case 'WEEKLY':
      return `${year}年第${week}周`;
    case 'MONTHLY':
      return `${year}年${month}月`;
    case 'YEARLY':
      return `${year}年`;
    default:
      return '';
  }
};

// 格式化货币
const formatCurrency = (value: string | number) => {
  const num = typeof value === 'string' ? parseFloat(value) : value;
  return `¥${num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
};

// 获取用户和账户列表
const fetchUsersAndAccounts = async () => {
  try {
    if (isAdmin.value && !usersStore.users.length) {
      await usersStore.fetchUsers();
    }
    if (!accountsStore.accounts.length) {
      await accountsStore.fetchAccounts();
    }
  } catch (err: any) {
    error.value = err.message || '获取用户或账户列表失败';
  }
};

// 更新 userId
const updateUserId = () => {
  const user = usersStore.users.find((u) => u.username === selectedUsername.value);
  filters.value.userId = user ? user.userId : undefined;
  fetchStatistics();
};

// 更新 accountId
const updateAccountId = () => {
  const account = accountsStore.accounts.find((a) => a.accountName === selectedAccountName.value);
  filters.value.accountId = account ? account.accountId : undefined;
  fetchStatistics();
};

// 获取统计数据
const fetchStatistics = async () => {
  loading.value = true;
  error.value = null;
  try {
    // 对于非管理员，强制使用当前用户 ID
    const effectiveUserId = isAdmin.value ? filters.value.userId : authStore.currentUser?.userId;

    const params = {
      period: filters.value.period,
      year: filters.value.year,
      month: filters.value.month,
      week: filters.value.week,
      day: filters.value.day,
      userId: effectiveUserId,
      accountId: filters.value.accountId,
    };

    console.log('Sending request to /statistics with params:', params);
    const response = await statisticsService.getStatistics(params);
    statistics.value = response;
    console.log('Response received:', response);

    // 更新图表
    updateCharts();
  } catch (err: any) {
    console.error('Request failed:', err);
    if (err.response?.status === 403) {
      error.value = '无权限访问统计数据，请联系管理员或尝试重新登录';
    } else if (err.response?.status === 400) {
      error.value = err.response?.data?.message || '请求参数错误';
    } else {
      error.value = err.response?.data?.message || err.message || '获取统计信息失败';
    }
  } finally {
    loading.value = false;
  }
};

// 更新饼图
const updateCharts = () => {
  if (!statistics.value) return;

  // 收入饼图
  const incomeCtx = document.getElementById('incomeChart') as HTMLCanvasElement;
  if (incomeCtx && statistics.value.incomeByType) {
    new Chart(incomeCtx, {
      type: 'pie',
      data: {
        labels: statistics.value.incomeByType.map((item) => item.typeName),
        datasets: [{
          data: statistics.value.incomeByType.map((item) => parseFloat(item.amount)),
          backgroundColor: ['#4CAF50', '#81C784', '#A5D6A7', '#C8E6C9'],
        }],
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: 'top' },
          title: { display: true, text: '收入类型分布' },
        },
      },
    });
  }

  // 支出饼图
  const expenseCtx = document.getElementById('expenseChart') as HTMLCanvasElement;
  if (expenseCtx && statistics.value.expenseByType) {
    new Chart(expenseCtx, {
      type: 'pie',
      data: {
        labels: statistics.value.expenseByType.map((item) => item.typeName),
        datasets: [{
          data: statistics.value.expenseByType.map((item) => parseFloat(item.amount)),
          backgroundColor: ['#F44336', '#EF5350', '#E57373', '#EF9A9A'],
        }],
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: 'top' },
          title: { display: true, text: '支出类型分布' },
        },
      },
    });
  }
};

// 监听周期变化，调整参数
watch(
  () => filters.value.period,
  (newPeriod) => {
    if (newPeriod !== 'MONTHLY') filters.value.month = undefined;
    if (newPeriod !== 'WEEKLY') filters.value.week = undefined;
    if (newPeriod !== 'DAILY') filters.value.day = undefined;
    fetchStatistics();
  }
);

// 初始加载
onMounted(async () => {
  // 检查认证状态
  authStore.checkAuth();

  // 如果未登录，显示错误
  if (!authStore.currentUser) {
    error.value = '请先登录';
    return;
  }

  await fetchUsersAndAccounts();
  // 如果非管理员，初始化 selectedUsername 为当前用户
  if (!isAdmin.value && authStore.currentUser?.username) {
    selectedUsername.value = authStore.currentUser.username;
    updateUserId();
  }
  await fetchStatistics();
});
</script>

<style scoped>
.statistics-view {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 20px;
}

.filter-item {
  display: flex;
  flex-direction: column;
  min-width: 150px;
}

.filter-item label {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.filter-item select,
.filter-item input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.refresh-btn {
  padding: 8px 16px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  align-self: flex-end;
}

.refresh-btn:hover {
  background: #45a049;
}

.time-range {
  text-align: center;
  margin-bottom: 20px;
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
  margin-bottom: 30px;
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

.income-by-type,
.expense-by-type {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.type-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.type-item {
  padding: 15px;
  background: #f5f5f5;
  border-radius: 4px;
}

.type-item h4 {
  margin: 0 0 10px 0;
  color: #333;
}

canvas {
  max-width: 400px;
  margin: 20px auto;
}

@media (max-width: 600px) {
  .filters {
    flex-direction: column;
  }

  .statistics-cards {
    grid-template-columns: 1fr;
  }
}
</style>