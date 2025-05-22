import { Configuration } from '../configuration';
import { AuthApi } from '../apis/auth-api';
import { LoginRequest, LoginResponse } from '../models';
import { LogoutResponse } from '../models/auth';
import { AxiosError } from 'axios';
import { apiClient, isUsingMock } from '../client';

const config = new Configuration({
    basePath: 'http://localhost:8080'
});

const authApi = new AuthApi(config);

class AuthService {
    /**
     * 用户登录
     * @param data 登录请求数据
     */
    async login(data: LoginRequest): Promise<LoginResponse> {
        try {
            const response = await apiClient.post<LoginResponse>('/login', data, {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            if (error instanceof AxiosError) {
                console.error('登录请求错误:', error.response?.data);
                throw new Error(error.response?.data?.message || '登录失败');
            }
            throw error;
        }
    }

    /**
     * 用户登出
     */
    async logout(): Promise<void> {
        try {
            await authApi.authLogout();
            this.clearToken();
        } catch (error) {
            if (error instanceof AxiosError) {
                throw new Error(error.response?.data?.message || '登出失败');
            }
            throw error;
        }
    }

    /**
     * 检查用户是否已认证
     */
    isAuthenticated(): boolean {
        return !!localStorage.getItem('token');
    }

    /**
     * 获取用户角色
     */
    getUserRole(): string | null {
        return localStorage.getItem('userRole');
    }

    /**
     * 设置认证令牌
     */
    setToken(token: string): void {
        localStorage.setItem('token', token);
    }

    /**
     * 清除认证信息
     */
    clearToken(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('userRole');
    }
}

export const authService = new AuthService();

export const login = async (data: LoginRequest): Promise<LoginResponse> => {
  const response = await apiClient.post<LoginResponse>('/login', data, {
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  });
  return response.data;
};

export const logout = async (): Promise<void> => {
  if (!isUsingMock) {
    await apiClient.post('/logout', {}, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    });
  }
  localStorage.removeItem('token');
  localStorage.removeItem('userRole');
}; 