// vite.config.ts
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import { resolve } from 'path';

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000,
  },
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
      '@/models/accounts': resolve(__dirname, 'src/models/accounts/index.ts'),
      '@/api/services': resolve(__dirname, 'src/api/services'),
      '@/views': resolve(__dirname, 'src/views'),
      '@/components': resolve(__dirname, 'src/components'),
    },
  },
  envDir: '.',
  build: {
    lib: {
      entry: resolve(__dirname, 'src/index.ts'),
      name: 'AccountingSystem',
      fileName: (format) => `accounting-system.${format}.js`,
    },
    rollupOptions: {
      external: ['vue', 'axios', 'pinia', 'vue-router'],
      output: {
        globals: {
          vue: 'Vue',
          axios: 'axios',
          pinia: 'pinia',
          'vue-router': 'VueRouter',
        },
      },
    },
  },
});