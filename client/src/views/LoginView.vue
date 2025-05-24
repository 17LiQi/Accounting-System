<template>
  <div class="login-container">
    <h1>登录</h1>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">用户名</label>
        <input
          type="text"
          id="username"
          v-model="username"
          required
        />
      </div>
      <div class="form-group">
        <label for="password">密码</label>
        <input
          type="password"
          id="password"
          v-model="password"
          required
        />
      </div>
      <div v-if="error" class="error-message">{{ error }}</div>
      <button type="submit" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/store/modules/auth';
import { isUsingMock } from '@/api/client';
import { useAccountsStore } from '@/store/modules/accounts';
import { useStatisticsStore } from '@/store/modules/statistics';
import { useAccountTypesStore } from '@/store/modules/accountTypes';

const router = useRouter();
const authStore = useAuthStore();
const username = ref('');
const password = ref('');
const loading = ref(false);
const error = ref('');

console.log('Login component mounted, mock mode:', isUsingMock);

const handleLogin = async () => {
  try {
    loading.value = true;
    error.value = '';
    
    console.log('Login attempt:', {
      username: username.value,
      passwordLength: password.value.length
    });
    
    const response = await authStore.login(username.value, password.value);
    console.log('Login successful:', response);
    
    // 登录成功后直接跳转到首页
    console.log('登录成功，准备跳转到首页...');
    try {
      await router.replace({ name: 'home' });
      console.log('路由跳转成功');
    } catch (routerError) {
      console.error('路由跳转失败:', routerError);
      // 如果命名路由失败，尝试使用路径
      await router.replace('/');
      console.log('使用路径跳转成功');
    }
    
  } catch (err: any) {
    console.error('操作失败:', err);
    if (err.response?.status === 403) {
      error.value = '用户名或密码错误';
    } else if (err.message === 'Network Error') {
      error.value = '网络连接失败，请检查后端服务是否正常运行';
    } else {
      error.value = err.message || '操作失败';
    }
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 100px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #45a049;
}

.error-message {
  color: #f44336;
  margin-bottom: 10px;
  padding: 8px;
  background-color: #ffebee;
  border-radius: 4px;
}
</style> 