import type { LoginRequest } from '@/api/models/auth/login-request';
import type { LoginResponse } from '@/api/models/auth/login-response';

// 模拟用户数据
const mockUsers = [
  {
    username: 'admin',
    password: 'password',
    role: 'ADMIN'
  }
];

console.log('Mock users initialized:', mockUsers.map(u => ({ ...u, password: '***' })));

// 模拟API响应
export const mockAuthApi = {
  // 用户登录
  login: (request: LoginRequest) => {
    console.log('Mock login attempt:', { 
      username: request.username, 
      passwordLength: request.password?.length,
      requestData: request
    });

    if (!request.username || !request.password) {
      console.error('Mock login failed: Missing username or password');
      return Promise.reject({
        status: 400,
        statusText: 'Bad Request',
        data: { message: '用户名和密码不能为空' }
      });
    }

    const user = mockUsers.find(u => {
      const usernameMatch = u.username === request.username;
      const passwordMatch = u.password === request.password;
      console.log('User check:', { 
        usernameMatch, 
        passwordMatch,
        expectedUsername: u.username,
        expectedPassword: '***',
        receivedUsername: request.username,
        receivedPasswordLength: request.password.length
      });
      return usernameMatch && passwordMatch;
    });

    if (user) {
      console.log('Mock login successful for user:', user.username);
      const response = {
        data: {
          token: 'mock-token-' + Date.now(),
          role: user.role
        } as LoginResponse,
        status: 200,
        statusText: 'OK'
      };
      console.log('Mock login response:', response);
      return Promise.resolve(response);
    }

    console.log('Mock login failed: Invalid credentials');
    return Promise.reject({
      status: 401,
      statusText: 'Unauthorized',
      data: { message: '无效的用户名或密码' }
    });
  }
}; 