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
    port: 5173,
    host: '0.0.0.0',
    proxy: {
      '/api': {
        target: 'http://localhost:3000',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
        configure: (proxy, _options) => {
          proxy.on('error', (err, _req, _res) => {
            console.log('proxy error', err);
          });
          proxy.on('proxyReq', (proxyReq, req, _res) => {
            console.log('Sending Request to the Target:', req.method, req.url);
          });
          proxy.on('proxyRes', (proxyRes, req, _res) => {
            console.log('Received Response from the Target:', proxyRes.statusCode, req.url);
          });
        },
      }
    }
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
});