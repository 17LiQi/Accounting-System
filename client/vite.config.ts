// vite.config.ts
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  server: {
    port: 3000
  },
  envDir: '.',
  build: {
    sourcemap: false,
    lib: {
      entry: path.resolve(__dirname, './src/index.ts'),
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
  define: {
    'process.env.NODE_ENV': JSON.stringify(process.env.NODE_ENV)
  }
});