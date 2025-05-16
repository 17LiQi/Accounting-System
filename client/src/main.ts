import { createApp } from 'vue';
import App from './App.vue';

const app = createApp(App);
if (import.meta.env.VITE_MOCK_ENABLED === 'true') {
    import('@/mocks/browser').then(({ worker }) => worker.start());
}
app.mount('#app');

import router from './router';
app.use(router);

import { pinia } from './store';
app.use(pinia);