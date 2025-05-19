import { Router } from 'vue-router';
import { authService } from '@/api/services/auth';

export function setupAuthGuard(router: Router) {
  router.beforeEach((to, from, next) => {
    const isAuthenticated = authService.isAuthenticated();
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

    if (requiresAuth && !isAuthenticated) {
      next({ name: 'login', query: { redirect: to.fullPath } });
    } else if (to.name === 'login' && isAuthenticated) {
      next({ name: 'home' });
    } else {
      next();
    }
  });
} 