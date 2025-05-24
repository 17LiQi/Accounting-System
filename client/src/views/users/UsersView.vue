<template>
  <div class="users-view">
    <h1>用户管理</h1>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else>
      <!-- 管理员：显示用户列表 -->
      <div v-if="isAdmin" class="users-list">
        <div v-for="user in usersStore.users" :key="user.userId" class="user-item">
          <div class="user-info">
            <h3>{{ user.username }}</h3>
            <p>角色: {{ user.role }}</p>
            <p>创建时间: {{ formatDate(user.createdAt) }}</p>
          </div>
          <div class="actions">
            <button @click="editUser(user)">编辑</button>
            <button @click="deleteUser(user.userId)" class="delete">删除</button>
          </div>
        </div>
        <button @click="addUser" class="add-button">添加用户</button>
      </div>
      <!-- 非管理员：显示个人信息 -->
      <div v-else class="user-item">
        <div class="user-info">
          <h3>{{ authStore.currentUser?.username }}</h3>
          <p>角色: {{ authStore.currentUser?.role }}</p>
          <p>创建时间: {{ formatDate(authStore.currentUser?.createdAt) }}</p>
        </div>
        <div class="actions">
          <button @click="editPassword">更改密码</button>
        </div>
      </div>
    </div>

    <!-- 编辑用户/密码对话框 -->
    <div class="dialog" v-if="showEditDialog">
      <div class="dialog-content">
        <h3>{{ dialogTitle }}</h3>
        <form @submit.prevent="saveUser">
          <!-- 管理员：创建时输入用户名 -->
          <div v-if="isAdmin && !editingUser?.userId" class="form-group">
            <label>用户名</label>
            <input v-model="editingUser!.username" required />
          </div>
          <!-- 非管理员：显示用户名（禁用） -->
          <div v-else class="form-group">
            <label>用户名</label>
            <input :value="editingUser?.username" disabled />
          </div>
          <!-- 密码字段 -->
          <div class="form-group">
            <label>{{ isAdmin && !editingUser?.userId ? '密码' : '新密码' }}</label>
            <input v-model="password" type="password" :required="isAdmin && !editingUser?.userId" />
          </div>
          <!-- 非管理员：旧密码 -->
          <div v-if="!isAdmin" class="form-group">
            <label>旧密码</label>
            <input v-model="oldPassword" type="password" required />
          </div>
          <!-- 管理员：角色 -->
          <div v-if="isAdmin" class="form-group">
            <label>角色</label>
            <select v-model="editingUser!.role" required>
              <option value="ADMIN">管理员</option>
              <option value="USER">普通用户</option>
            </select>
          </div>
          <div class="dialog-actions">
            <button type="button" @click="cancelEdit">取消</button>
            <button type="submit">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useUsersStore } from '@/store/modules/users';
import { useAuthStore } from '@/store/modules/auth';
import type { UserDTO } from '@/api/models/user/user-dto';
import type { UserRequest } from '@/api/models/user/user-request';
import { usersService } from '@/api/services/users';

const usersStore = useUsersStore();
const authStore = useAuthStore();
const loading = ref(false);
const error = ref<string | null>(null);
const showEditDialog = ref(false);
const editingUser = ref<UserDTO | null>(null);
const password = ref('');
const oldPassword = ref('');

// 判断是否为管理员
const isAdmin = computed(() => authStore.currentUser?.role === 'ADMIN');

// 对话框标题
const dialogTitle = computed(() => {
  if (!isAdmin.value) return '更改密码';
  return editingUser.value?.userId ? '编辑用户' : '新建用户';
});

// 格式化日期
const formatDate = (date: string | undefined) => {
  if (!date) return '';
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 加载用户数据
const fetchUsers = async () => {
  loading.value = true;
  error.value = null;
  try {
    if (isAdmin.value) {
      await usersStore.fetchUsers();
    } else {
      const user = await usersService.getCurrentUser();
      authStore.currentUser = user;
    }
  } catch (err: any) {
    error.value = err.message || '获取用户数据失败';
  } finally {
    loading.value = false;
  }
};

// 添加用户
const addUser = () => {
  editingUser.value = { userId: 0, username: '', role: 'USER', createdAt: '', updatedAt: '' };
  password.value = '';
  showEditDialog.value = true;
};

// 编辑用户
const editUser = (user: UserDTO) => {
  editingUser.value = { ...user };
  password.value = '';
  showEditDialog.value = true;
};

// 编辑密码（非管理员）
const editPassword = () => {
  editingUser.value = { ...authStore.currentUser! };
  password.value = '';
  oldPassword.value = '';
  showEditDialog.value = true;
};

// 取消编辑
const cancelEdit = () => {
  showEditDialog.value = false;
  editingUser.value = null;
  password.value = '';
  oldPassword.value = '';
};

// 保存用户/密码
const saveUser = async () => {
  if (!editingUser.value) return;

  try {
    if (isAdmin.value) {
      // 管理员：创建或更新用户
      const userRequest: UserRequest = {
        username: editingUser.value.username,
        password: password.value || undefined,
        role: editingUser.value.role
      };
      if (editingUser.value.userId) {
        await usersStore.updateUser(editingUser.value.userId, userRequest);
      } else {
        if (!password.value) {
          error.value = '新建用户时密码不能为空';
          return;
        }
        await usersStore.createUser(userRequest);
      }
    } else {
      // 非管理员：更新密码
      if (!oldPassword.value || !password.value) {
        error.value = '旧密码和新密码不能为空';
        return;
      }
      await usersService.updatePassword(authStore.currentUser!.userId, oldPassword.value, password.value);
    }

    showEditDialog.value = false;
    editingUser.value = null;
    password.value = '';
    oldPassword.value = '';
    await fetchUsers();
  } catch (err: any) {
    error.value = err.message || (isAdmin.value ? '保存用户失败' : '更改密码失败');
  }
};

// 删除用户
const deleteUser = async (userId: number) => {
  if (!confirm('确定要删除这个用户吗？')) return;
  try {
    await usersStore.deleteUser(userId);
  } catch (err: any) {
    error.value = err.message || '删除用户失败';
  }
};

// 初始加载
onMounted(async () => {
  authStore.checkAuth();
  if (!authStore.currentUser) {
    error.value = '请先登录';
    return;
  }
  await fetchUsers();
});
</script>

<style scoped>
.users-view {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
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
  font-weight: bold;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-group input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.dialog-actions button {
  padding: 8px 16px;
}

.dialog-actions button[type="button"] {
  background: #666;
}
</style>