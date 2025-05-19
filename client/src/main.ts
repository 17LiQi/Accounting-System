import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import { startMockService } from './mocks/browser';
import { isUsingMock } from './api/client';

// 如果启用了mock模式，启动mock服务
if (isUsingMock) {
  console.log('Starting mock service...');
  startMockService().catch(console.error);
}

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.mount('#app');