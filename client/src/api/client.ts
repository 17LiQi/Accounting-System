import axios, { AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import { Configuration } from './configuration';

// 获取环境变量，支持Vite和Jest环境
const getEnvVar = (key: string, defaultValue: string): string => {
  // 在Jest环境中使用process.env
  if (typeof process !== 'undefined' && process.env) {
    return process.env[key] || defaultValue;
  }
  // 在Vite环境中使用import.meta.env
  if (typeof import.meta !== 'undefined' && import.meta.env) {
    return import.meta.env[key] || defaultValue;
  }
  return defaultValue;
};

// 修改baseURL为相对路径，让Vite代理处理
const baseURL = '/api';
// 从环境变量获取mock模式配置
const useMock = getEnvVar('VITE_MOCK_ENABLED', 'true') === 'true';

console.log('API Client initialized:', { baseURL, useMock });

// 创建axios实例
const axiosInstance: AxiosInstance = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 添加请求拦截器
axiosInstance.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    console.log('API Request:', {
      url: config.url,
      method: config.method,
      headers: config.headers,
      data: config.data
    });
    const token = localStorage.getItem('token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error: unknown) => {
    console.error('API Request Error:', error);
    return Promise.reject(error);
  }
);

// 添加响应拦截器
axiosInstance.interceptors.response.use(
  (response) => {
    console.log('API Response:', {
      status: response.status,
      data: response.data
    });
    return response;
  },
  (error) => {
    console.error('API Response Error:', {
      status: error.response?.status,
      data: error.response?.data,
      message: error.message
    });
    if (error.response?.status === 401) {
      // 清除token并跳转到登录页
      localStorage.removeItem('token');
      localStorage.removeItem('userRole');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// 创建配置实例
export const apiClient = new Configuration({
  baseOptions: {
    baseURL,
    headers: {
      'Content-Type': 'application/json'
    }
  }
});

// 将axios实例添加到配置中
(apiClient as any).axios = axiosInstance;

// 导出是否使用mock的标志
export const isUsingMock = useMock; 