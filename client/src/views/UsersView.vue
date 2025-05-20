<template>
  <div class="users-view">
    <h1>用户管理</h1>
    <div v-if="formError" class="error-message">{{ formError }}</div>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="store.error" class="error-message">{{ store.error }}</div>
    <div v-else class="users-list">
      <div v-for="user in store.users" :key="user.userId" class="user-card">
        <h3>{{ user.username }}</h3>
        <p>角色: {{ user.role }}</p>
        <div class="actions">
          <button class="edit" @click="editUser(user)">编辑</button>
          <button class="delete" @click="deleteUser(user.userId)">删除</button>
        </div>
      </div>
    </div>

    <!-- 编辑用户对话框 -->
    <div class="dialog" v-if="showEditDialog && editingUser">
      <div class="dialog-content">
        <h3>{{ editingUser.userId ? '编辑用户' : '新建用户' }}</h3>
        <form @submit.prevent="saveUser">
          <div class="form-group">
            <label>用户名</label>
            <input v-model="editingUser.username" required>
          </div>
          <div class="form-group">
            <label>密码</label>
            <input v-model="password" type="password">
          </div>
          <div class="form-group">
            <label>角色</label>
            <select v-model="editingUser.role" required>
              <option value="ADMIN">管理员</option>
              <option value="USER">普通用户</option>
            </select>
          </div>
          <div class="dialog-actions">
            <button type="button" @click="showEditDialog = false">取消</button>
            <button type="submit">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useUsersStore } from '@/store/modules/users';
import type { UserDTO } from '@/api/models/user/user-dto';
import type { UserRequest } from '@/api/models/user/user-request';

const formError = ref('');
const store = useUsersStore();
const loading = ref(false);
const showEditDialog = ref(false);
const editingUser = ref<UserDTO | null>(null);
const password = ref('');

onMounted(async () => {
  try {
    loading.value = true;
    await store.fetchUsers();
    loading.value = false;
  } catch (err: any) {
    store.error = err.message || '加载用户列表失败';
    loading.value = false;
  }
});

const editUser = (user: UserDTO) => {
  editingUser.value = { ...user };
  password.value = '';
  showEditDialog.value = true;
};

const saveUser = async () => {
  formError.value = '';
  if (!editingUser.value?.username) {
    formError.value = '用户名不能为空';
    return;
  }

  if (!editingUser.value) return;

  try {
    const userRequest: UserRequest = {
      username: editingUser.value.username,
      password: password.value,
      role: editingUser.value.role
    };

    if (editingUser.value.userId) {
      await store.updateUser(editingUser.value.userId, userRequest);
    } else {
      await store.createUser(userRequest);
    }

    showEditDialog.value = false;
    editingUser.value = null;
    password.value = '';
    await store.fetchUsers();
  } catch (error) {
    console.error('保存用户失败:', error);
  }
};

async function deleteUser(userId: number) {
  if (!confirm('确定要删除这个用户吗？')) return;

  try {
    loading.value = true;
    await store.deleteUser(userId);
  } catch (err: any) {
    store.error = err.message || '删除用户失败';
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.users-view {
  padding: 20px;
}

.loading, .error-message {
  text-align: center;
  padding: 20px;
}

.error-message {
  color: red;
}

.users-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.user-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  background: white;
}

.user-card h3 {
  margin: 0 0 10px;
}

.actions {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  background: #4CAF50;
  color: white;
  cursor: pointer;
}

button.delete {
  background: #f44336;
}

.dialog {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.dialog-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 100%;
  max-width: 500px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>