import { httpClient } from '../http-client';
import type { UserDTO } from '../models/user/user-dto';
import type { UserRequest } from '../models/user/user-request';
import { useAuthStore } from '@/store/modules/auth';

export const usersService = {
  async getUsers(): Promise<UserDTO[]> {
    const response = await httpClient.get<UserDTO[]>('/users');
    return response.data;
  },

  async getUser(userId: number): Promise<UserDTO> {
    const response = await httpClient.get<UserDTO>(`/users/${userId}`);
    return response.data;
  },

  // 改造后的 getCurrentUser 方法
  async getCurrentUser(): Promise<UserDTO> {
    const auth = useAuthStore();
    const userId = auth.currentUser?.userId;
    if (!userId) {
      throw new Error('当前用户未登录');
    }
    const response = await httpClient.get<UserDTO>(`/users/${userId}`);
    return response.data;
  },

  async createUser(request: UserRequest): Promise<UserDTO> {
    const response = await httpClient.post<UserDTO>('/users', request);
    return response.data;
  },

  async updateUser(userId: number, request: UserRequest): Promise<UserDTO> {
    const response = await httpClient.put<UserDTO>(`/users/${userId}`, request);
    return response.data;
  },

  async updatePassword(userId: number, oldPassword: string, newPassword: string): Promise<void> {
    await httpClient.patch(`/users/${userId}/password`, {
      oldPassword,
      newPassword
    });
  },

  async deleteUser(userId: number): Promise<void> {
    await httpClient.delete(`/users/${userId}`);
  }
};
