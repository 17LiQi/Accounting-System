import '@testing-library/jest-dom';
import { config } from '@vue/test-utils';
import { vi } from 'vitest';

// 全局配置
config.global.mocks = {
  $t: (key: string) => key,
  $route: { params: {} },
  $router: { push: vi.fn() }
};

// Mock matchMedia
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation(query => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(),
    removeListener: vi.fn(),
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
});

// 模拟 window.confirm
window.confirm = vi.fn(() => true);
// 模拟 window.alert
window.alert = vi.fn();

// 模拟 localStorage
const localStorageMock = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  removeItem: vi.fn(),
  clear: vi.fn()
};
Object.defineProperty(window, 'localStorage', { value: localStorageMock });

// Mock axios 和 apiClient
vi.mock('axios', async () => {
  const actual = await vi.importActual<any>('axios');
  const mockAxios = {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
    interceptors: {
      request: { use: vi.fn(), eject: vi.fn() },
      response: { use: vi.fn(), eject: vi.fn() }
    }
  };
  return {
    ...actual,
    default: { create: () => mockAxios },
    AxiosError: actual.AxiosError
  };
});

vi.mock('@/api/client', () => {
  const apiClient = {
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
    interceptors: {
      request: { use: vi.fn(), eject: vi.fn() },
      response: { use: vi.fn(), eject: vi.fn() }
    }
  };
  return { apiClient, isUsingMock: true };
});

// 模拟 window.location
Object.defineProperty(window, 'location', {
  value: {
    href: '',
    pathname: '',
    search: '',
    hash: '',
    assign: vi.fn(),
    replace: vi.fn(),
    reload: vi.fn()
  },
  writable: true
});

// 导出 mock 对象供测试使用
// 注意：mockAxios、apiClient 需要在 mock 里定义并导出
export { localStorageMock };