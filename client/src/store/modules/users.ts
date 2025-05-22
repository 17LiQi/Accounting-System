import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { UserDTO } from '@/api/models/user/user-dto';
import type { UserRequest } from '@/api/models/user/user-request';
import { usersService } from '@/api/services/users';

export const useUsersStore = defineStore('users', () => {
  const users = ref<UserDTO[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const fetchUsers = async () => {
    loading.value = true;
    error.value = null;
    try {
      const response = await usersService.getUsers();
      users.value = response;
    } catch (err: any) {
      error.value = err.message || '获取用户列表失败';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const createUser = async (userRequest: UserRequest) => {
    loading.value = true;
    error.value = null;
    try {
      const response = await usersService.createUser(userRequest);
      users.value.push(response);
    } catch (err: any) {
      error.value = err.message || '创建用户失败';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const updateUser = async (userId: number, userRequest: UserRequest) => {
    loading.value = true;
    error.value = null;
    try {
      const response = await usersService.updateUser(userId, userRequest);
      const index = users.value.findIndex(user => user.userId === userId);
      if (index !== -1) {
        users.value[index] = response;
      }
    } catch (err: any) {
      error.value = err.message || '更新用户失败';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const deleteUser = async (userId: number) => {
    loading.value = true;
    error.value = null;
    try {
      await usersService.deleteUser(userId);
      users.value = users.value.filter(user => user.userId !== userId);
    } catch (err: any) {
      error.value = err.message || '删除用户失败';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  return {
    users,
    loading,
    error,
    fetchUsers,
    createUser,
    updateUser,
    deleteUser
  };
}); 