// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'home',
            component: () => import('@/views/Home.vue'),
        },
        {
            path: '/accounts',
            name: 'accounts',
            component: () => import('@/views/accounts/AccountList.vue'),
        },
        {
            path: '/accounts/:id',
            name: 'account-detail',
            component: () => import('@/views/accounts/AccountDetail.vue'),
        },
    ],
});

export default router;