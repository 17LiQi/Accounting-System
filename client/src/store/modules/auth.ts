import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { UserDTO } from '@/api/models/user/user-dto';
import { apiClient, isUsingMock } from '@/api/client';

export const useAuthStore = defineStore('auth', () => {
  const currentUser = ref<UserDTO | null>(null);
  const token = ref<string | null>(null);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const login = async (username: string, password: string) => {
    loading.value = true;
    error.value = null;
    try {
      const loginData = {
        username,
        password
      };
      console.log('发送登录请求:', loginData);
      const response = await apiClient.post<{ token: string; role: string }>('/login', loginData, {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      });
      console.log('登录响应:', response.data);
      
      // 从响应中获取token和role
      const { token: responseToken, role } = response.data;
      
      if (!responseToken) {
        throw new Error('登录响应中缺少token');
      }
      
      // 创建用户对象
      const user: UserDTO = {
        userId: 0, // 这里需要从后端获取用户ID
        username,
        role,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      };
      
      token.value = responseToken;
      currentUser.value = user;
      
      // 存储到 localStorage
      localStorage.setItem('token', responseToken);
      localStorage.setItem('user', JSON.stringify(user));
      
      return { token: responseToken, user };
    } catch (err: any) {
      console.error('登录请求错误:', err.response?.data || err.message);
      error.value = err.response?.data?.message || err.message || '登录失败';
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const logout = async () => {
    try {
      if (token.value) {
        await apiClient.post('/logout', {}, {
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          }
        });
      }
    } catch (err) {
      console.error('登出请求失败:', err);
    } finally {
      currentUser.value = null;
      token.value = null;
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
  };

  const checkAuth = () => {
    const savedToken = localStorage.getItem('token');
    const savedUser = localStorage.getItem('user');
    
    console.log('认证检查 - 本地存储:', { savedToken, savedUser });
    
    if (!savedToken || !savedUser) {
      currentUser.value = null;
      token.value = null;
      return false;
    }

    try {
      const parsedUser = JSON.parse(savedUser);
      if (!parsedUser || typeof parsedUser !== 'object') {
        throw new Error('无效的用户信息格式');
      }
      
      token.value = savedToken;
      currentUser.value = parsedUser;
      
      console.log('认证检查 - 解析结果:', {
        token: savedToken,
        user: parsedUser
      });
      
      return true;
    } catch (err) {
      console.error('认证检查失败:', err);
      currentUser.value = null;
      token.value = null;
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      return false;
    }
  };

  return {
    currentUser,
    token,
    loading,
    error,
    login,
    logout,
    checkAuth
  };
}); 