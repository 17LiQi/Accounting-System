<template>
  <div class="users-view">
    <h1>用户管理</h1>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else>
      <div class="users-list">
        <div v-for="user in users" :key="user.id" class="user-item">
          <div class="user-info">
            <h3>{{ user.username }}</h3>
            <p>角色: {{ user.role }}</p>
            <p>创建时间: {{ user.createdAt }}</p>
          </div>
          <div class="actions">
            <button @click="editUser(user)">编辑</button>
            <button @click="deleteUser(user.id)" class="delete">删除</button>
          </div>
        </div>
      </div>
      <button @click="showAddForm = true" class="add-button">添加用户</button>
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

const usersStore = useUsersStore();
const loading = ref(false);
const error = ref<string | null>(null);
const showAddForm = ref(false);
const showEditDialog = ref(false);
const editingUser = ref<UserDTO | null>(null);
const password = ref('');

const fetchUsers = async () => {
  loading.value = true;
  error.value = null;
  try {
    await usersStore.fetchUsers();
  } catch (err: any) {
    error.value = err.message || '获取用户列表失败';
  } finally {
    loading.value = false;
  }
};

onMounted(fetchUsers);

const editUser = (user: UserDTO) => {
  editingUser.value = { ...user };
  password.value = '';
  showEditDialog.value = true;
};

const saveUser = async () => {
  if (!editingUser.value?.username) {
    error.value = '用户名不能为空';
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
      await usersStore.updateUser(editingUser.value.userId, userRequest);
    } else {
      await usersStore.createUser(userRequest);
    }

    showEditDialog.value = false;
    editingUser.value = null;
    password.value = '';
    await fetchUsers();
  } catch (error) {
    console.error('保存用户失败:', error);
  }
};

async function deleteUser(userId: number) {
  if (!confirm('确定要删除这个用户吗？')) return;

  try {
    loading.value = true;
    await usersStore.deleteUser(userId);
  } catch (err: any) {
    error.value = err.message || '删除用户失败';
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.users-view {
  padding: 20px;
}

.loading,
.error {
  text-align: center;
  padding: 20px;
  font-size: 18px;
}

.error {
  color: red;
}

.users-list {
  display: grid;
  gap: 20px;
  margin-bottom: 20px;
}

.user-item {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info h3 {
  margin: 0 0 10px 0;
}

.user-info p {
  margin: 5px 0;
  color: #666;
}

.actions {
  display: flex;
  gap: 10px;
}

button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background: #4CAF50;
  color: white;
}

button.delete {
  background: #f44336;
}

button:hover {
  opacity: 0.9;
}

.add-button {
  margin-top: 20px;
  background: #2196F3;
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