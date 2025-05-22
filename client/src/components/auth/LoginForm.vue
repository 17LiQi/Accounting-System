<template>
  <div class="login-form">
    <h2>用户登录</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="username">用户名</label>
        <input
          id="username"
          v-model="formData.username"
          type="text"
          required
          placeholder="请输入用户名"
        />
      </div>
      <div class="form-group">
        <label for="password">密码</label>
        <input
          id="password"
          v-model="formData.password"
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
import type { LoginRequest } from '@/api/models';

export default defineComponent({
  name: 'LoginForm',
  setup() {
    const router = useRouter();
    const loading = ref(false);
    const error = ref('');
    const formData = ref<LoginRequest>({
      username: '',
      password: ''
    });

    const handleSubmit = async () => {
      try {
        loading.value = true;
        error.value = '';
        console.log('登录请求数据:', formData.value);
        const response = await authService.login(formData.value);
        console.log('登录响应:', response);
        authService.setToken(response.token);
        localStorage.setItem('userRole', response.role);
        console.log('Token 和角色已保存');
        console.log('当前认证状态:', authService.isAuthenticated());
        console.log('准备跳转到首页...');
        try {
          await router.push({ name: 'home' });
          console.log('跳转完成');
        } catch (routerError) {
          console.error('路由跳转错误:', routerError);
          error.value = '页面跳转失败';
        }
      } catch (err: any) {
        console.error('登录错误:', err);
        if (err instanceof Error) {
          error.value = err.message;
        } else if (err.response?.data?.message) {
          error.value = err.response.data.message;
        } else {
          error.value = '登录失败，请检查网络连接';
        }
      } finally {
        loading.value = false;
      }
    };

    return {
      formData,
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