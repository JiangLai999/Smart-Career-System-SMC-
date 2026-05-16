import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import { createPinia } from 'pinia'
import App from './App.vue'
import './style.css'

// 管理端路由
const routes = [
  {
    path: '/',
    redirect: '/admin'
  },
  { path: '/admin/login', name: 'AdminLogin', component: () => import('./views/admin/AdminLogin.vue') },
  {
    path: '/admin',
    component: () => import('./components/admin/AdminLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', name: 'AdminDashboard', component: () => import('./views/admin/AdminDashboard.vue') },
      { path: 'users', name: 'AdminUsers', component: () => import('./views/admin/AdminUsers.vue') },
      { path: 'permissions', name: 'AdminPermissions', component: () => import('./views/admin/AdminPermissions.vue') },
      { path: 'audits', name: 'AdminAudits', component: () => import('./views/admin/AdminAudits.vue') },
      { path: 'settings', name: 'AdminSettings', component: () => import('./views/admin/AdminSettings.vue') },
      { path: 'logs', name: 'AdminLogs', component: () => import('./views/admin/AdminLogs.vue') },
      { path: 'announcements', name: 'AdminAnnouncements', component: () => import('./views/admin/AdminAnnouncements.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const adminToken = localStorage.getItem('adminToken')
  if (to.meta.requiresAuth && !adminToken) {
    next({ name: 'AdminLogin', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.mount('#app')
