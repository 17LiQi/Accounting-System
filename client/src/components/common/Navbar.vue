<template>
  <nav class="navbar">
    <div class="navbar-brand">
      <router-link to="/" class="navbar-item">
        家庭记账系统
      </router-link>
    </div>
    <div class="navbar-menu">
      <div class="navbar-start">
        <router-link to="/accounts" class="navbar-item">
          账户管理
        </router-link>
        <router-link to="/transactions" class="navbar-item">
          交易记录
        </router-link>
        <router-link to="/statistics" class="navbar-item">
          统计分析
        </router-link>
        <router-link v-if="isAdmin" to="/users" class="navbar-item">
          用户管理
        </router-link>
      </div>
      <div class="navbar-end">
        <LogoutButton />
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { authService } from '@/api/services/auth';
import LogoutButton from '@/components/auth/LogoutButton.vue';

const isAdmin = computed(() => authService.getUserRole() === 'ADMIN');
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 1rem;
  height: 60px;
  background-color: #1976d2;
  color: white;
}

.navbar-brand {
  font-size: 1.5rem;
  font-weight: bold;
}

.navbar-menu {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex: 1;
  margin-left: 2rem;
}

.navbar-start {
  display: flex;
  gap: 1rem;
}

.navbar-end {
  margin-left: auto;
}

.navbar-item {
  color: white;
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.navbar-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.router-link-active {
  background-color: rgba(255, 255, 255, 0.2);
}
</style> 