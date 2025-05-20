import type { LoginRequest, LoginResponse } from '@/api/models/auth';
import type { AxiosResponse } from 'axios';
import { LoginResponseRoleEnum } from '@/api/models/auth/login-response';

// 模拟用户数据
const mockUsers = [
  {
    username: 'admin',
    password: 'admin123',
    role: LoginResponseRoleEnum.Admin
  }
];

console.log('Mock users initialized:', mockUsers.map(u => ({ ...u, password: '***' })));

// 模拟API响应
export const mockAuthApi = {
  authLogin: async (request: LoginRequest): Promise<AxiosResponse<LoginResponse>> => {
    const user = mockUsers.find(u => u.username === request.username && u.password === request.password);
    
    if (user) {
      return {
        data: {
          token: 'test-token',
          role: user.role
        },
        status: 200,
        statusText: 'OK',
        headers: {},
        config: {} as any
      };
    }
    throw new Error('Invalid credentials');
  }
}; 