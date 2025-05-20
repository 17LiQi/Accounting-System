<template>
  <div class="app">
    <nav v-if="isAuthenticated" class="nav">
      <router-link to="/" class="nav-item">首页</router-link>
      <router-link to="/accounts" class="nav-item">账户管理</router-link>
      <router-link to="/sub-accounts" class="nav-item">子账户管理</router-link>
      <router-link to="/transactions" class="nav-item">交易记录</router-link>
      <router-link to="/statistics" class="nav-item">统计</router-link>
      <router-link v-if="isAdmin" to="/users" class="nav-item">用户管理</router-link>
      <button @click="logout" class="nav-item logout">退出</button>
    </nav>
    <main class="main">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '@/api/services/auth';

const router = useRouter();

const isAuthenticated = computed(() => authService.isAuthenticated());
const isAdmin = computed(() => authService.getUserRole() === 'ADMIN');

function logout() {
  authService.logout();
  router.push('/login');
}
</script>

<style>
:root {
  --primary-color: #4CAF50;
  --danger-color: #f44336;
  --text-color: #333;
  --border-color: #ddd;
  --background-color: #f5f5f5;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  color: var(--text-color);
  background-color: var(--background-color);
  line-height: 1.6;
}

.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.nav {
  background: white;
  padding: 1rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  gap: 1rem;
  align-items: center;
}

.nav-item {
  text-decoration: none;
  color: var(--text-color);
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.nav-item:hover {
  background-color: var(--background-color);
}

.nav-item.router-link-active {
  background-color: var(--primary-color);
  color: white;
}

.nav-item.logout {
  margin-left: auto;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1rem;
}

.nav-item.logout:hover {
  color: var(--danger-color);
}

.main {
  flex: 1;
  padding: 1rem;
}

h1 {
  margin-bottom: 1.5rem;
  color: var(--text-color);
}

button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  background: var(--primary-color);
  color: white;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s;
}

button:hover {
  background-color: #45a049;
}

button.delete {
  background: var(--danger-color);
}

button.delete:hover {
  background-color: #da190b;
}

input, select {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  font-size: 1rem;
}

input:focus, select:focus {
  outline: none;
  border-color: var(--primary-color);
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: var(--text-color);
}

.error {
  color: var(--danger-color);
  margin-bottom: 1rem;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: var(--text-color);
}
</style>