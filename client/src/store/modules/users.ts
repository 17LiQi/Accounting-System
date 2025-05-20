import { defineStore } from 'pinia';
import { usersService } from '@/api/services/users';
import type { UserDTO } from '@/api/models/user/user-dto';
import type { UserRequest } from '@/api/models/user/user-request';

export const useUsersStore = defineStore('users', {
  state: () => ({
    users: [] as UserDTO[],
    loading: false,
    error: null as string | null
  }),

  actions: {
    async fetchUsers() {
      this.loading = true;
      this.error = null;
      try {
        this.users = await usersService.getUsers();
        return this.users;
      } catch (error: any) {
        this.error = error.message || '获取用户列表失败';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async createUser(request: UserRequest) {
      this.loading = true;
      this.error = null;
      try {
        const user = await usersService.createUser(request);
        this.users.push(user);
        return user;
      } catch (error: any) {
        this.error = error.message || '创建用户失败';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async updateUser(id: number, request: UserRequest) {
      this.loading = true;
      this.error = null;
      try {
        const user = await usersService.updateUser(id, request);
        const index = this.users.findIndex(u => u.userId === id);
        if (index !== -1) {
          this.users[index] = user;
        }
        return user;
      } catch (error: any) {
        this.error = error.message || '更新用户失败';
        throw error;
      } finally {
        this.loading = false;
      }
    },

    async deleteUser(id: number) {
      this.loading = true;
      this.error = null;
      try {
        await usersService.deleteUser(id);
        this.users = this.users.filter(u => u.userId !== id);
      } catch (error: any) {
        this.error = error.message || '删除用户失败';
        throw error;
      } finally {
        this.loading = false;
      }
    }
  }
}); 