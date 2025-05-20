// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router';
import type { RouteLocationNormalized, NavigationGuardNext, RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        name: 'Home',
        component: () => import('@/views/Home.vue')
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/accounts',
        name: 'Accounts',
        component: () => import('@/views/accounts/AccountList.vue')
    },
    {
        path: '/accounts/:id',
        name: 'AccountDetail',
        component: () => import('@/views/accounts/AccountDetail.vue')
    },
    {
        path: '/sub-accounts',
        name: 'subAccounts',
        component: () => import('@/views/SubAccountsView.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/transactions',
        name: 'transactions',
        component: () => import('@/views/transactions/TransactionsView.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/statistics',
        name: 'statistics',
        component: () => import('@/views/statistics/StatisticsView.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/users',
        name: 'users',
        component: () => import('@/views/UsersView.vue'),
        meta: { requiresAuth: true, requiresAdmin: true }
    }
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
});

// 路由守卫
router.beforeEach((to: RouteLocationNormalized, _: RouteLocationNormalized, next: NavigationGuardNext) => {
    const token = localStorage.getItem('token');
    if (to.name !== 'login' && !token) {
        next({ name: 'login' });
    } else {
        next();
    }
});

export default router;