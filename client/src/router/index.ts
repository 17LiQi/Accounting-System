// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router';
import { authService } from '@/api/services/auth';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/login',
            name: 'Login',
            component: () => import('@/views/Login.vue'),
            meta: { requiresAuth: false }
        },
        {
            path: '/',
            name: 'Home',
            component: () => import('@/views/Home.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: '/accounts',
            name: 'Accounts',
            component: () => import('@/views/Accounts.vue'),
            meta: { requiresAuth: true }
        },
        {
            path: '/accounts/:id',
            name: 'account-detail',
            component: () => import('@/views/accounts/AccountDetail.vue'),
            meta: { requiresAuth: true }
        },
    ],
});

// 恢复路由守卫
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const isAuthenticated = authService.isAuthenticated();

  if (requiresAuth && !isAuthenticated) {
    next('/login');
  } else {
    next();
  }
});

export default router;