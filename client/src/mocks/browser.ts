// src/mocks/browser.ts
import { setupWorker } from 'msw';
import { handlers } from './handlers';
import { MOCK_ENABLED } from '@/api/client';

let worker: ReturnType<typeof setupWorker>;

// 启动mock服务
export async function startMockService() {
  if (!MOCK_ENABLED) {
    console.log('Mock service is disabled');
    return;
  }

  if (!worker) {
    worker = setupWorker(...handlers);
  }
  
  try {
    await worker.start({
      onUnhandledRequest: 'bypass',
      serviceWorker: {
        url: '/mockServiceWorker.js',
        options: {
          scope: '/',
        },
      },
    });
    console.log('Mock service started successfully');
  } catch (error) {
    console.error('Failed to start mock service:', error);
    // 如果启动失败，禁用 mock 服务
    if (import.meta.env.DEV) {
      console.warn('Mock service failed to start, continuing without mocks');
    }
  }
}
