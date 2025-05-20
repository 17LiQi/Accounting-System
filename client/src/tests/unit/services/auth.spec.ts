import { describe, it, expect, beforeEach, vi } from 'vitest';
import { authService } from '@/api/services/auth';
import type { LoginRequest } from '@/api/models/auth';
import { apiClient } from '@/api/client';
import { AxiosError } from 'axios';

// Mock localStorage
const localStorageMock = (() => {
  let store: Record<string, string> = {};
  return {
    getItem: vi.fn((key: string) => store[key] || null),
    setItem: vi.fn((key: string, value: string) => {
      store[key] = value;
    }),
    removeItem: vi.fn((key: string) => {
      delete store[key];
    }),
    clear: vi.fn(() => {
      store = {};
    })
  };
})();

Object.defineProperty(window, 'localStorage', {
  value: localStorageMock,
  writable: true
});

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
  },
  AxiosError: class extends Error {
    constructor(message: string, public response?: any) {
      super(message);
      this.name = 'AxiosError';
    }
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

describe('AuthService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  describe('login', () => {
    it('应该成功登录并返回 token', async () => {
      const mockResponse = {
        data: {
          token: 'test-token',
          role: 'ADMIN'
        }
      };

      vi.mocked(apiClient.post).mockResolvedValueOnce(mockResponse);

      const loginRequest: LoginRequest = {
        username: 'admin',
        password: 'admin123'
      };

      const result = await authService.login(loginRequest);
      expect(result).toEqual(mockResponse.data);
      expect(localStorage.getItem('token')).toBe('test-token');
      expect(localStorage.getItem('userRole')).toBe('ADMIN');
    });

    it('应该处理登录失败', async () => {
      const error = new AxiosError('Invalid credentials');
      vi.mocked(apiClient.post).mockRejectedValueOnce(error);

      const loginRequest: LoginRequest = {
        username: 'wrong',
        password: 'wrong'
      };

      await expect(authService.login(loginRequest)).rejects.toThrow('Invalid credentials');
    });
  });

  describe('logout', () => {
    it('应该清除本地存储的认证信息', () => {
      localStorage.setItem('token', 'test-token');
      localStorage.setItem('userRole', 'ADMIN');

      authService.logout();
      expect(localStorage.getItem('token')).toBeNull();
      expect(localStorage.getItem('userRole')).toBeNull();
    });
  });

  describe('isAuthenticated', () => {
    it('当存在 token 时应该返回 true', () => {
      localStorage.setItem('token', 'test-token');
      expect(authService.isAuthenticated()).toBe(true);
    });

    it('当不存在 token 时应该返回 false', () => {
      localStorage.removeItem('token');
      expect(authService.isAuthenticated()).toBe(false);
    });
  });
}); 