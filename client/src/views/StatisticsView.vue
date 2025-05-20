<template>
  <div class="statistics-view">
    <h1>统计分析</h1>
    <div class="date-range">
      <input type="date" v-model="startDate" @change="fetchStatistics">
      <input type="date" v-model="endDate" @change="fetchStatistics">
    </div>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="statistics" class="statistics-content">
      <div class="summary-cards">
        <div class="card income">
          <h3>总收入</h3>
          <p>{{ statistics.totalIncome }}</p>
        </div>
        <div class="card expense">
          <h3>总支出</h3>
          <p>{{ statistics.totalExpense }}</p>
        </div>
        <div class="card balance">
          <h3>结余</h3>
          <p>{{ statistics.totalIncome - statistics.totalExpense }}</p>
        </div>
      </div>

      <div class="charts">
        <div class="chart-container">
          <h3>月度收支趋势</h3>
          <canvas ref="monthlyTrendChart"></canvas>
        </div>
        <div class="chart-container">
          <h3>账户余额分布</h3>
          <canvas ref="accountBalanceChart"></canvas>
        </div>
        <div class="chart-container">
          <h3>收支类型占比</h3>
          <canvas ref="typeDistributionChart"></canvas>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useStatisticsStore } from '@/store/modules/statistics';
import type { StatisticsDTO } from '@/api/models/statistics';
import Chart from 'chart.js/auto';

const statisticsStore = useStatisticsStore();
const loading = ref(false);
const error = ref<string | null>(null);
const statistics = ref<StatisticsDTO | null>(null);
const startDate = ref(new Date(new Date().getFullYear(), 0, 1).toISOString().split('T')[0]);
const endDate = ref(new Date().toISOString().split('T')[0]);

let monthlyTrendChart: Chart | null = null;
let accountBalanceChart: Chart | null = null;
let typeDistributionChart: Chart | null = null;

async function fetchStatistics() {
  try {
    loading.value = true;
    statistics.value = await statisticsStore.fetchStatistics(startDate.value, endDate.value);
    updateCharts();
  } catch (err: any) {
    error.value = err.message || '获取统计数据失败';
  } finally {
    loading.value = false;
  }
}

function updateCharts() {
  if (!statistics.value) return;

  // 更新月度趋势图
  if (monthlyTrendChart) {
    monthlyTrendChart.destroy();
  }
  const monthlyCtx = document.querySelector('#monthlyTrendChart') as HTMLCanvasElement;
  if (monthlyCtx) {
    monthlyTrendChart = new Chart(monthlyCtx, {
      type: 'line',
      data: {
        labels: statistics.value.monthlyStatistics.map(ms => ms.month),
        datasets: [
          {
            label: '收入',
            data: statistics.value.monthlyStatistics.map(ms => ms.income),
            borderColor: '#4CAF50',
            fill: false
          },
          {
            label: '支出',
            data: statistics.value.monthlyStatistics.map(ms => ms.expense),
            borderColor: '#f44336',
            fill: false
          }
        ]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: '月度收支趋势'
          }
        }
      }
    });
  }

  // 更新账户余额分布图
  if (accountBalanceChart) {
    accountBalanceChart.destroy();
  }
  const balanceCtx = document.querySelector('#accountBalanceChart') as HTMLCanvasElement;
  if (balanceCtx) {
    accountBalanceChart = new Chart(balanceCtx, {
      type: 'pie',
      data: {
        labels: statistics.value.accountBalances.map(ab => ab.accountName),
        datasets: [{
          data: statistics.value.accountBalances.map(ab => ab.balance),
          backgroundColor: [
            '#4CAF50',
            '#2196F3',
            '#FFC107',
            '#9C27B0',
            '#FF5722'
          ]
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: '账户余额分布'
          }
        }
      }
    });
  }

  // 更新收支类型占比图
  if (typeDistributionChart) {
    typeDistributionChart.destroy();
  }
  const typeCtx = document.querySelector('#typeDistributionChart') as HTMLCanvasElement;
  if (typeCtx) {
    typeDistributionChart = new Chart(typeCtx, {
      type: 'doughnut',
      data: {
        labels: ['收入', '支出'],
        datasets: [{
          data: [statistics.value.totalIncome, statistics.value.totalExpense],
          backgroundColor: ['#4CAF50', '#f44336']
        }]
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: '收支类型占比'
          }
        }
      }
    });
  }
}

onMounted(() => {
  fetchStatistics();
});

watch([startDate, endDate], () => {
  fetchStatistics();
});
</script>

<style scoped>
.statistics-view {
  padding: 20px;
}

.date-range {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.date-range input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.loading, .error {
  text-align: center;
  padding: 20px;
}

.error {
  color: red;
}

.summary-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.card {
  padding: 20px;
  border-radius: 8px;
  color: white;
  text-align: center;
}

.card h3 {
  margin: 0;
  font-size: 1.2em;
}

.card p {
  margin: 10px 0 0;
  font-size: 1.5em;
  font-weight: bold;
}

.card.income {
  background-color: #4CAF50;
}

.card.expense {
  background-color: #f44336;
}

.card.balance {
  background-color: #2196F3;
}

.charts {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.chart-container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.chart-container h3 {
  margin: 0 0 20px;
  text-align: center;
}
</style> 