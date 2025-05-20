import { apiClient } from '../client';
import type { UserDTO } from '../models/user/user-dto';
import type { UserRequest } from '../models/user/user-request';

class UsersService {
  async getUsers(): Promise<UserDTO[]> {
    const response = await apiClient.get('/users');
    return response.data;
  }

  async getUser(id: number): Promise<UserDTO> {
    const response = await apiClient.get(`/users/${id}`);
    return response.data;
  }

  async createUser(request: UserRequest): Promise<UserDTO> {
    const response = await apiClient.post('/users', request);
    return response.data;
  }

  async updateUser(id: number, request: UserRequest): Promise<UserDTO> {
    const response = await apiClient.put(`/users/${id}`, request);
    return response.data;
  }

  async deleteUser(id: number): Promise<void> {
    await apiClient.delete(`/users/${id}`);
  }
}

export const usersService = new UsersService(); 