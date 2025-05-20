// src/mocks/browser.ts
import { setupWorker } from 'msw';
import { handlers } from './handlers';
import { MOCK_ENABLED } from '@/api/client';

let worker: ReturnType<typeof setupWorker>;

if (MOCK_ENABLED) {
  worker = setupWorker(...handlers);
  worker.start();
}

// 启动mock服务
export async function startMockService() {
  if (!worker) {
    worker = setupWorker(...handlers);
  }
  
  try {
    await worker.start({
      onUnhandledRequest: 'bypass', // 对于未处理的请求，直接放行
    });
    console.log('Mock service started successfully');
  } catch (error) {
    console.error('Failed to start mock service:', error);
  }
}
