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
    </div>
  </div>
</template>

<style scoped>
.home-container {
  padding: 20px;
}

.menu {
  margin-top: 20px;
}

.menu-item {
  display: inline-block;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  margin-right: 10px;
}

.menu-item:hover {
  background-color: #45a049;
}
</style>