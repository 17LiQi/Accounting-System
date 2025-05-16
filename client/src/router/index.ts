import { createRouter, createWebHistory } from 'vue-router';
const routes = [
    { path: '/', name: 'Home', component: () => import('@/views/Home.vue') },
    { path: '/accounts', name: 'Accounts', component: () => import('@/views/Accounts.vue') },
];
const router = createRouter({
    history: createWebHistory(),
    routes,
});
export default router;

const routes = [
    { path: '/accounts', name: 'Accounts', component: () => import('@/views/Accounts.vue') },
];