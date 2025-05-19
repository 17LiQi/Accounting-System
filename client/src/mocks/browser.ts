// src/mocks/browser.ts
import { setupWorker } from 'msw';
import { handlers } from './handlers';
import { isUsingMock } from '@/api/client';

// 创建mock服务
export const worker = setupWorker(...handlers);

// 只在开发环境且启用mock时启动worker
if (import.meta.env.DEV && isUsingMock) {
  worker.start({
    onUnhandledRequest: 'bypass'
  });
}

// 启动mock服务
export async function startMockService() {
  try {
    await worker.start({
      onUnhandledRequest: 'bypass', // 对于未处理的请求，直接放行
    });
    console.log('Mock service started successfully');
  } catch (error) {
    console.error('Failed to start mock service:', error);
  }
}
