import { AuthApi } from '../apis';
import type { LoginRequest, LoginResponse } from '../models/auth';
import { mockAuthApi } from '@/mocks/auth';
import { MOCK_ENABLED } from '../client';
import { AxiosError } from 'axios';
import { apiClient } from '../client';

class AuthService {
  private api: AuthApi;

  constructor() {
    this.api = new AuthApi();
    console.log('AuthService initialized, mock mode:', MOCK_ENABLED);
  }

  async login(request: LoginRequest): Promise<LoginResponse> {
    console.log('Login attempt:', { 
      username: request.username, 
      passwordLength: request.password?.length,
      isUsingMock: MOCK_ENABLED 
    });

    try {
      let response;
      if (MOCK_ENABLED) {
        console.log('Using mock authentication');
        response = await mockAuthApi.authLogin(request);
        console.log('Mock login response:', response);
      } else {
        console.log('Using real API authentication');
        response = await this.api.authLogin(request);
        console.log('API login response:', response);
      }
      
      if (!response || !response.data) {
        console.error('Invalid response:', response);
        throw new Error('无效的响应数据');
      }

      if (!response.data.token) {
        console.error('Response missing token:', response.data);
        throw new Error('响应数据缺少token');
      }

      console.log('Login successful, saving token and role');
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('userRole', response.data.role);
      return response.data;
    } catch (error: unknown) {
      console.error('Login error details:', {
        error,
        message: error instanceof Error ? error.message : 'Unknown error',
        response: error instanceof AxiosError ? error.response?.data : undefined
      });
      throw error;
    }
  }

  async logout(): Promise<void> {
    console.log('Logging out user');
    await apiClient.post('/auth/logout');
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
  }

  isAuthenticated(): boolean {
    const hasToken = !!localStorage.getItem('token');
    console.log('Checking authentication status:', { hasToken });
    return hasToken;
  }

  getToken(): string | null {
    const token = localStorage.getItem('token');
    console.log('Getting token:', { hasToken: !!token });
    return token;
  }

  getUserRole(): string | null {
    const role = localStorage.getItem('userRole');
    console.log('Getting user role:', { role });
    return role;
  }

  setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  clearToken(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
  }
}

export const authService = new AuthService();

export const login = async (data: LoginRequest): Promise<LoginResponse> => {
  const response = await apiClient.post<LoginResponse>('/auth/login', data);
  return response.data;
};

export const logout = async (): Promise<void> => {
  await apiClient.post('/auth/logout');
  localStorage.removeItem('token');
}; 