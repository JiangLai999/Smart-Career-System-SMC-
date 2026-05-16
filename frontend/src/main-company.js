import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import { createPinia } from 'pinia'
import App from './App.vue'
import './style.css'

// 企业端路由
const routes = [
  {
    path: '/',
    redirect: '/company/dashboard'
  },
  {
    path: '/company',
    component: () => import('./components/company/CompanyLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/company/dashboard' },
      { path: 'dashboard', name: 'CompanyDashboard', component: () => import('./views/company/CompanyDashboard.vue') },
      { path: 'jobs', name: 'CompanyJobs', component: () => import('./views/company/CompanyJobs.vue') },
      { path: 'jobs/create', name: 'CreateJob', component: () => import('./views/company/CreateJob.vue') },
      { path: 'jobs/:id/edit', name: 'EditJob', component: () => import('./views/company/CreateJob.vue') },
      { path: 'resumes', name: 'CompanyResumes', component: () => import('./views/company/CompanyResumes.vue') },
      { path: 'resumes/:id', name: 'ResumeDetail', component: () => import('./views/company/ResumeDetail.vue') },

      { path: 'applications', name: 'CompanyApplications', component: () => import('./views/company/CompanyApplications.vue') },
      { path: 'profile', name: 'CompanyProfile', component: () => import('./views/company/CompanyProfile.vue') },
      { path: 'interviews', name: 'InterviewSchedule', component: () => import('./views/company/InterviewSchedule.vue') },
      { path: 'interview/feedback/:id', name: 'InterviewFeedback', component: () => import('./views/company/InterviewFeedback.vue') },
      { path: 'audit-status', name: 'AuditStatus', component: () => import('./views/company/AuditStatus.vue') },
      { path: 'notifications', name: 'CompanyNotifications', component: () => import('./views/company/CompanyNotifications.vue') }
    ]
  },
  {
    path: '/company/login',
    name: 'CompanyLogin',
    component: () => import('./views/company/CompanyLogin.vue'),
    meta: { guest: true }
  },
  {
    path: '/company/register',
    name: 'CompanyRegister',
    component: () => import('./views/company/CompanyRegister.vue'),
    meta: { guest: true }
  },
  {
    path: '/company/forgot-password',
    name: 'CompanyForgotPassword',
    component: () => import('./views/company/CompanyForgotPassword.vue'),
    meta: { guest: true }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/company/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const companyToken = localStorage.getItem('companyToken')
  if (to.meta.requiresAuth && !companyToken) {
    next({ name: 'CompanyLogin', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.mount('#app')
