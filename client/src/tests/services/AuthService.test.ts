import { authService } from '@/api/services/auth';
import { rest } from 'msw';
import { setupServer } from 'msw/node';
import { vi } from 'vitest';

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

// 使用与client.ts相同的baseURL
const baseURL = 'http://localhost:3000/api';

const server = setupServer(
  rest.post(`${baseURL}/login`, async (req, res, ctx) => {
    const { username, password } = await req.json();
    
    if (username === 'admin' && password === 'admin123') {
      return res(
        ctx.status(200),
        ctx.json({
          token: 'test-token',
          role: 'ADMIN'
        })
      );
    }
    
    return res(
      ctx.status(401),
      ctx.json({ message: '无效的用户名或密码' })
    );
  })
);

beforeAll(() => {
  server.listen();
  // 清除localStorage
  localStorage.clear();
});

afterEach(() => {
  server.resetHandlers();
  localStorage.clear();
});

afterAll(() => {
  server.close();
  localStorage.clear();
});

describe('AuthService', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    localStorage.clear();
  });

  it('登录成功时应该返回token和角色', async () => {
    const response = await authService.login({
      username: 'admin',
      password: 'admin123'
    });
    
    expect(response.token).toBe('test-token');
    expect(response.role).toBe('ADMIN');
    
    // 验证localStorage
    expect(localStorage.getItem('token')).toBe('test-token');
    expect(localStorage.getItem('userRole')).toBe('ADMIN');
  });

  it('登录失败时应该抛出错误', async () => {
    await expect(
      authService.login({
        username: 'wrong',
        password: 'wrong'
      })
    ).rejects.toThrow();
  });

  it('登出时应该清除token和角色', async () => {
    // 先设置token
    localStorage.setItem('token', 'test-token');
    localStorage.setItem('userRole', 'ADMIN');
    
    await authService.logout();
    
    expect(localStorage.getItem('token')).toBeNull();
    expect(localStorage.getItem('userRole')).toBeNull();
  });

  it('isAuthenticated应该正确反映认证状态', () => {
    expect(authService.isAuthenticated()).toBe(false);
    
    localStorage.setItem('token', 'test-token');
    expect(authService.isAuthenticated()).toBe(true);
  });
}); 