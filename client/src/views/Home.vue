<script setup lang="ts">
// 首页组件逻辑
</script>

<template>
  <div class="home-container">
    <h1>欢迎使用家庭记账系统</h1>
    <div class="menu">
      <router-link to="/accounts" class="menu-item">
        账户管理
      </router-link>
      <router-link to="/transactions" class="menu-item">
        交易记录
      </router-link>
      <router-link to="/statistics" class="menu-item">
        统计分析
      </router-link>
      <router-link to="/users" class="menu-item">
        用户管理
      </router-link>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  padding: 20px;
}

.menu {
  margin-top: 20px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background-color: #4CAF50;
  color: white;
  text-decoration: none;
  border-radius: 8px;
  font-size: 1.2em;
  transition: background-color 0.3s;
}

.menu-item:hover {
  background-color: #45a049;
}
</style>