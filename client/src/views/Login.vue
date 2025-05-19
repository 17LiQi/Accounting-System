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
import { authService } from '@/api/services/auth';
import { isUsingMock } from '@/api/client';

const router = useRouter();
const username = ref('');
const password = ref('');
const loading = ref(false);
const error = ref('');

console.log('Login component mounted, mock mode:', isUsingMock);

const handleLogin = async () => {
  console.log('Login attempt:', { 
    username: username.value, 
    passwordLength: password.value.length 
  });
  
  try {
    loading.value = true;
    error.value = '';
    
    const response = await authService.login({
      username: username.value,
      password: password.value
    });
    
    console.log('Login successful:', response);
    router.push('/');
  } catch (err: any) {
    console.error('Login error:', err);
    error.value = err.response?.data?.message || '登录失败，请检查用户名和密码';
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