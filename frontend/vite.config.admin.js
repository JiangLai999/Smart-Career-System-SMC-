import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [
    vue(),
    {
      name: 'html-entry-rewrite',
      configureServer(server) {
        server.middlewares.use((req, res, next) => {
          const url = req.url
          if (url === '/' || url === '/index.html' || url.startsWith('/admin')) {
            req.url = '/index-admin.html'
          }
          next()
        })
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
    port: 3002,
    host: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path
      }
    },
    middlewareMode: false,
    fs: {
      allow: ['.']
    }
  },
  preview: {
    fallback: true
  },
  build: {
    outDir: 'dist-admin',
    rollupOptions: {
      input: resolve(__dirname, 'index-admin.html')
    }
  },
  optimizeDeps: {
    include: ['vue', 'vue-router', 'pinia', 'axios']
  }
})
