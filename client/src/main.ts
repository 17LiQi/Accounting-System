import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import './assets/main.css';

const app = createApp(App);
app.use(createPinia());
app.use(router);

// 在开发环境下启动 mock 服务
if (import.meta.env.DEV) {
  import('./mocks/browser').then(({ startMockService }) => {
    startMockService();
  });
}

app.mount('#app');