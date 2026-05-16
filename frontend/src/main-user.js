import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import { createPinia } from 'pinia'
import App from './App.vue'
import './style.css'

// 用户端路由
const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/app',
    component: () => import('./components/Layout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/app/home' },
      { path: 'home', name: 'Home', component: () => import('./views/Home.vue') },
      { path: 'jobs', name: 'JobList', component: () => import('./views/JobList.vue') },
      { path: 'job/:id', name: 'JobDetail', component: () => import('./views/JobDetail.vue') },
      { path: 'resume', name: 'Resume', component: () => import('./views/Resume.vue') },
      { path: 'profile', name: 'Profile', component: () => import('./views/Profile.vue') },
      { path: 'assessment', name: 'Assessment', component: () => import('./views/Assessment.vue') },
      { path: 'recommendation', name: 'Recommendation', component: () => import('./views/Recommendation.vue') },
      { path: 'career-advice', name: 'CareerAdvice', component: () => import('./views/CareerAdvice.vue') },
      { path: 'applications', name: 'Applications', component: () => import('./views/Applications.vue') },
      { path: 'interviews', name: 'Interviews', component: () => import('./views/Interviews.vue') },
      { path: 'notifications', name: 'Notifications', component: () => import('./views/Notifications.vue') }
    ]
  },
  { path: '/login', name: 'Login', component: () => import('./views/Login.vue'), meta: { guest: true } },
  { path: '/register', name: 'Register', component: () => import('./views/Register.vue'), meta: { guest: true } },
  { path: '/forgot-password', name: 'ForgotPassword', component: () => import('./views/ForgotPassword.vue'), meta: { guest: true } },
  { path: '/:pathMatch(.*)*', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const isAuthenticated = !!token
  
  // 已登录用户访问登录/注册页，重定向到首页
  if (to.meta.guest && isAuthenticated) {
    next('/app/home')
    return
  }
  
  // 需要认证的页面，未登录则重定向到登录页
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }
  }
  
  // 所有 /app 路径都需要认证
  if (to.path.startsWith('/app') && !isAuthenticated) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }
  
  next()
})

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.mount('#app')
