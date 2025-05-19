// src/mocks/browser.ts
import { setupWorker } from 'msw';
import { handlers } from './handlers';

export const worker = setupWorker(...handlers);

// 启动 worker
if (process.env.NODE_ENV === 'development') {
  worker.start({
    onUnhandledRequest: 'bypass'
  });
}
