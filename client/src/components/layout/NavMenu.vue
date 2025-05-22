<template>
  <nav class="nav-menu">
    <router-link to="/" class="nav-item">首页</router-link>
    <router-link to="/accounts" class="nav-item">账户管理</router-link>
    <router-link to="/transactions" class="nav-item">交易记录</router-link>
  </nav>
</template>

<style scoped>
.nav-menu {
  display: flex;
  gap: 20px;
  padding: 10px 20px;
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
}

.nav-item {
  text-decoration: none;
  color: #495057;
  font-weight: 500;
  padding: 5px 10px;
  border-radius: 4px;
  transition: all 0.2s;
}

.nav-item:hover {
  color: var(--primary-color);
  background-color: rgba(0, 123, 255, 0.1);
}

.nav-item.router-link-active {
  color: var(--primary-color);
  background-color: rgba(0, 123, 255, 0.1);
}
</style> 