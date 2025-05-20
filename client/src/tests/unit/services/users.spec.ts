import { describe, it, expect, beforeEach, vi } from 'vitest';
import { usersService } from '@/api/services/users';
import type { UserDTO } from '@/api/models/user/user-dto';
import type { UserRequest } from '@/api/models/user/user-request';
import { apiClient } from '@/api/client';

vi.mock('axios', () => ({
  default: {
    create: () => ({
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      delete: vi.fn(),
      interceptors: {
        request: { use: vi.fn(), eject: vi.fn() },
        response: { use: vi.fn(), eject: vi.fn() }
      }
    })
  }
}));

vi.mock('@/api/client', () => ({
  apiClient: {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
    interceptors: {
      request: { use: vi.fn(), eject: vi.fn() },
      response: { use: vi.fn(), eject: vi.fn() }
    }
  },
  isUsingMock: true
}));

describe('UsersService', () => {
  const mockUser: UserDTO = {
    userId: 1,
    username: 'testuser',
    role: 'USER'
  };

  const mockUserRequest: UserRequest = {
    username: 'testuser',
    password: 'password123',
    role: 'USER'
  };

  beforeEach(() => {
    vi.clearAllMocks();
  });

  describe('getUsers', () => {
    it('应该返回用户列表', async () => {
      vi.mocked(apiClient.get).mockResolvedValueOnce({
        data: [mockUser]
      });

      const result = await usersService.getUsers();
      expect(result).toEqual([mockUser]);
      expect(apiClient.get).toHaveBeenCalledWith('/users');
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.get).mockRejectedValueOnce(new Error('API Error'));
      await expect(usersService.getUsers()).rejects.toThrow('API Error');
    });
  });

  describe('getUser', () => {
    it('应该返回单个用户', async () => {
      const userId = 1;
      vi.mocked(apiClient.get).mockResolvedValueOnce({
        data: mockUser
      });

      const result = await usersService.getUser(userId);
      expect(result).toEqual(mockUser);
      expect(apiClient.get).toHaveBeenCalledWith(`/users/${userId}`);
    });

    it('应该处理错误', async () => {
      const userId = 1;
      vi.mocked(apiClient.get).mockRejectedValueOnce(new Error('API Error'));
      await expect(usersService.getUser(userId)).rejects.toThrow('API Error');
    });
  });

  describe('createUser', () => {
    it('应该创建用户', async () => {
      vi.mocked(apiClient.post).mockResolvedValueOnce({
        data: mockUser
      });

      const result = await usersService.createUser(mockUserRequest);
      expect(result).toEqual(mockUser);
      expect(apiClient.post).toHaveBeenCalledWith('/users', mockUserRequest);
    });

    it('应该处理错误', async () => {
      vi.mocked(apiClient.post).mockRejectedValueOnce(new Error('API Error'));
      await expect(usersService.createUser(mockUserRequest)).rejects.toThrow('API Error');
    });
  });

  describe('updateUser', () => {
    it('应该更新用户', async () => {
      const userId = 1;
      vi.mocked(apiClient.put).mockResolvedValueOnce({
        data: mockUser
      });

      const result = await usersService.updateUser(userId, mockUserRequest);
      expect(result).toEqual(mockUser);
      expect(apiClient.put).toHaveBeenCalledWith(`/users/${userId}`, mockUserRequest);
    });

    it('应该处理错误', async () => {
      const userId = 1;
      vi.mocked(apiClient.put).mockRejectedValueOnce(new Error('API Error'));
      await expect(usersService.updateUser(userId, mockUserRequest)).rejects.toThrow('API Error');
    });
  });

  describe('deleteUser', () => {
    it('应该删除用户', async () => {
      const userId = 1;
      vi.mocked(apiClient.delete).mockResolvedValueOnce({
        data: { message: '删除成功' }
      });

      await usersService.deleteUser(userId);
      expect(apiClient.delete).toHaveBeenCalledWith(`/users/${userId}`);
    });

    it('应该处理错误', async () => {
      const userId = 1;
      vi.mocked(apiClient.delete).mockRejectedValueOnce(new Error('API Error'));
      await expect(usersService.deleteUser(userId)).rejects.toThrow('API Error');
    });
  });
}); 