// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router';
import type { RouteLocationNormalized, NavigationGuardNext, RouteRecordRaw } from 'vue-router';
import { authService } from '@/api/services/auth';
import { useAuthStore } from '@/store/modules/auth';
import { useAccountsStore } from '@/store/modules/accounts';
import { useAccountTypesStore } from '@/store/modules/accountTypes';
import { useStatisticsStore } from '@/store/modules/statistics';
import { useTransactionStore } from '@/store/modules/transactions';
import { useUsersStore } from '@/store/modules/users';
import HomeView from '@/views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';
import AccountsView from '@/views/accounts/AccountsView.vue';
import SubAccountView from '@/views/sub-accounts/SubAccountView.vue';
import TransactionsView from '@/views/transactions/TransactionsView.vue';

const routes: RouteRecordRaw[] = [
    {
        path: '/',
        name: 'home',
        component: HomeView,
        meta: { requiresAuth: true }
    },
    {
        path: '/login',
        name: 'login',
        component: LoginView,
        meta: { requiresAuth: false }
    },
    {
        path: '/accounts',
        name: 'accounts',
        component: AccountsView,
        meta: { requiresAuth: true },
        children: [
            {
                path: 'sub-accounts',
                name: 'sub-accounts',
                component: () => import('@/views/accounts/SubAccountsView.vue'),
                meta: { requiresAuth: true }
            }
        ]
    },
    {
        path: '/accounts/:id',
        name: 'account-detail',
        component: () => import('@/views/accounts/AccountDetailView.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/transactions',
        name: 'transactions',
        component: TransactionsView,
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
        component: () => import('@/views/users/UsersView.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/sub-accounts/:id',
        name: 'sub-account-details',
        component: SubAccountView
    }
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
});

router.beforeEach((to: RouteLocationNormalized, from: RouteLocationNormalized, next: NavigationGuardNext) => {
    const authStore = useAuthStore();
    const isAuthenticated = authStore.checkAuth();
    
    if (to.meta.requiresAuth && !isAuthenticated) {
        next({ name: 'login' });
    } else {
        next();
    }
});

// 修改数据获取逻辑
router.afterEach(async (to) => {
    const authStore = useAuthStore();
    const isAuthenticated = authStore.checkAuth();
    
    if (!isAuthenticated) {
        return;
    }

    const userId = authStore.currentUser?.userId;
    if (!userId) {
        return;
    }

    try {
        switch (to.name) {
            case 'accounts':
            case 'account-detail':
            case 'sub-accounts':
                const accountsStore = useAccountsStore();
                await accountsStore.fetchAccounts();
                break;
            case 'transactions':
                const transactionStore = useTransactionStore();
                await transactionStore.fetchTransactions();
                break;
            case 'statistics':
                const statisticsStore = useStatisticsStore();
                const today = new Date();
                const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
                await statisticsStore.fetchStatistics(
                    firstDayOfMonth.toISOString().split('T')[0],
                    today.toISOString().split('T')[0]
                );
                break;
            case 'users':
                if (authStore.currentUser?.role === 'ADMIN') {
                    const usersStore = useUsersStore();
                    await usersStore.fetchUsers();
                }
                break;
        }
    } catch (error) {
        console.error('数据获取失败:', error);
    }
});

export default router;