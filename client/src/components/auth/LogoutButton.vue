<template>
  <button
    class="logout-button"
    @click="handleLogout"
    :disabled="loading"
  >
    <span v-if="loading">登出中...</span>
    <span v-else>登出</span>
  </button>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '@/api/services/auth';

const router = useRouter();
const loading = ref(false);

const handleLogout = async () => {
  try {
    loading.value = true;
    await authService.logout();
    router.push('/login');
  } catch (error) {
    console.error('登出失败:', error);
  } finally {
    loading.value = false;
  }
};

defineOptions({
  name: 'LogoutButton'
});
</script>

<style scoped>
.logout-button {
  padding: 8px 16px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.logout-button:hover {
  background-color: #d32f2f;
}

.logout-button:disabled {
  background-color: #e57373;
  cursor: not-allowed;
}
</style> 