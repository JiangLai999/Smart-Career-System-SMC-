import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [
    vue(),
    {
      name: 'html-entry-rewrite',
      configureServer(server) {
        return () => {
          server.middlewares.use((req, res, next) => {
            const url = req.url
            if (url === '/' || url === '/index.html' || url.startsWith('/company')) {
              req.url = '/index-company.html'
            }
            next()
          })
        }
      }
    }
  ],
  root: '.',
  publicDir: 'public',
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3001,
    host: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path
      }
    }
  },
  build: {
    outDir: 'dist-company',
    rollupOptions: {
      input: resolve(__dirname, 'index-company.html')
    }
  },
  optimizeDeps: {
    include: ['vue', 'vue-router', 'pinia', 'axios']
  }
})
