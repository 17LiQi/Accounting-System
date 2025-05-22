import { apiClient } from '../client';
import type { UserDTO } from '../models/user/user-dto';
import type { UserRequest } from '../models/user/user-request';

class UsersService {
  async getUsers(): Promise<UserDTO[]> {
    const response = await apiClient.get<UserDTO[]>('/users');
    return response.data;
  }

  async getUser(userId: number): Promise<UserDTO> {
    const response = await apiClient.get<UserDTO>(`/users/${userId}`);
    return response.data;
  }

  async getCurrentUser(): Promise<UserDTO> {
    const response = await apiClient.get<UserDTO>('/users/current');
    return response.data;
  }

  async createUser(request: UserRequest): Promise<UserDTO> {
    const response = await apiClient.post<UserDTO>('/users', request);
    return response.data;
  }

  async updateUser(userId: number, request: UserRequest): Promise<UserDTO> {
    const response = await apiClient.put<UserDTO>(`/users/${userId}`, request);
    return response.data;
  }

  async deleteUser(userId: number): Promise<void> {
    await apiClient.delete(`/users/${userId}`);
  }
}

export const usersService = new UsersService();