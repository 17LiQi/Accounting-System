<template>
  <div class="login-form">
    <h2>用户登录</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="username">用户名</label>
        <input
          id="username"
          v-model="form.username"
          type="text"
          required
          placeholder="请输入用户名"
        />
      </div>
      <div class="form-group">
        <label for="password">密码</label>
        <input
          id="password"
          v-model="form.password"
          type="password"
          required
          placeholder="请输入密码"
        />
      </div>
      <div class="error-message" v-if="error">{{ error }}</div>
      <button type="submit" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>
    </form>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '@/api/services/auth';
import type { LoginRequest } from '@/api/models/auth';

export default defineComponent({
  name: 'LoginForm',
  setup() {
    const router = useRouter();
    const loading = ref(false);
    const error = ref('');
    const form = reactive<LoginRequest>({
      username: '',
      password: ''
    });

    const handleSubmit = async () => {
      try {
        loading.value = true;
        error.value = '';
        const response = await authService.login(form);
        
        // 保存token和用户角色
        localStorage.setItem('token', response.token);
        localStorage.setItem('userRole', response.role);
        
        // 登录成功后跳转到首页
        router.push('/');
      } catch (err: any) {
        error.value = err.response?.data?.message || '登录失败，请重试';
      } finally {
        loading.value = false;
      }
    };

    return {
      form,
      loading,
      error,
      handleSubmit
    };
  }
});
</script>

<style scoped>
.login-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
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

.error-message {
  color: red;
  margin-bottom: 10px;
}
</style> 