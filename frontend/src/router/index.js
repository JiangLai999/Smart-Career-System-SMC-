import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'
import Layout from '@/components/Layout.vue'
import CompanyLayout from '@/components/company/CompanyLayout.vue'
import AdminLayout from '@/components/admin/AdminLayout.vue'

const routes = [
  // 根路径重定向到登录页
  {
    path: '/',
    redirect: '/login'
  },
  // 求职者端路由
  {
    path: '/app',
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: '/app/home'
      },
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue')
      },
      {
        path: 'jobs',
        name: 'JobList',
        component: () => import('@/views/JobList.vue')
      },
      {
        path: 'job/:id',
        name: 'JobDetail',
        component: () => import('@/views/JobDetail.vue')
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'resume',
        name: 'Resume',
        component: () => import('@/views/Resume.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'assessment',
        name: 'Assessment',
        component: () => import('@/views/Assessment.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'recommendation',
        name: 'Recommendation',
        component: () => import('@/views/Recommendation.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'career-advice',
        name: 'CareerAdvice',
        component: () => import('@/views/CareerAdvice.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'applications',
        name: 'Applications',
        component: () => import('@/views/Applications.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'interviews',
        name: 'Interviews',
        component: () => import('@/views/Interviews.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'notifications',
        name: 'Notifications',
        component: () => import('@/views/Notifications.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/forgot-password',
    name: 'ForgotPassword',
    component: () => import('@/views/ForgotPassword.vue')
  },
  
  // 企业HR端路由
  {
    path: '/company',
    component: CompanyLayout,
    meta: { requiresCompanyAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'CompanyDashboard',
        component: () => import('@/views/company/CompanyDashboard.vue')
      },
      {
        path: 'jobs',
        name: 'CompanyJobs',
        component: () => import('@/views/company/CompanyJobs.vue')
      },
      {
        path: 'jobs/create',
        name: 'CreateJob',
        component: () => import('@/views/company/CreateJob.vue')
      },
      {
        path: 'jobs/:id/edit',
        name: 'EditJob',
        component: () => import('@/views/company/CreateJob.vue')
      },
      {
        path: 'resumes',
        name: 'CompanyResumes',
        component: () => import('@/views/company/CompanyResumes.vue')
      },
      {
        path: 'resumes/:id',
        name: 'ResumeDetail',
        component: () => import('@/views/company/ResumeDetail.vue')
      },
      {
        path: 'applications',
        name: 'CompanyApplications',
        component: () => import('@/views/company/CompanyApplications.vue')
      },
      {
        path: 'profile',
        name: 'CompanyProfile',
        component: () => import('@/views/company/CompanyProfile.vue')
      },
      {
        path: 'interviews',
        name: 'InterviewSchedule',
        component: () => import('@/views/company/InterviewSchedule.vue')
      },
      {
        path: 'interview/feedback/:id',
        name: 'InterviewFeedback',
        component: () => import('@/views/company/InterviewFeedback.vue')
      },
      {
        path: 'audit-status',
        name: 'AuditStatus',
        component: () => import('@/views/company/AuditStatus.vue')
      },
      {
        path: 'notifications',
        name: 'CompanyNotifications',
        component: () => import('@/views/company/CompanyNotifications.vue')
      }
    ]
  },
  {
    path: '/company/login',
    name: 'CompanyLogin',
    component: () => import('@/views/company/CompanyLogin.vue')
  },
  {
    path: '/company/register',
    name: 'CompanyRegister',
    component: () => import('@/views/company/CompanyRegister.vue')
  },
  {
    path: '/company/forgot-password',
    name: 'CompanyForgotPassword',
    component: () => import('@/views/company/CompanyForgotPassword.vue')
  },
  
  // 系统管理员端路由
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAdminAuth: true },
    children: [
      {
        path: '',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/AdminDashboard.vue')
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/AdminUsers.vue')
      },
      {
        path: 'audits',
        name: 'AdminAudits',
        component: () => import('@/views/admin/AdminAudits.vue')
      },
      {
        path: 'settings',
        name: 'AdminSettings',
        component: () => import('@/views/admin/AdminSettings.vue')
      },
      {
        path: 'logs',
        name: 'AdminLogs',
        component: () => import('@/views/admin/AdminLogs.vue')
      },
      {
        path: 'permissions',
        name: 'AdminPermissions',
        component: () => import('@/views/admin/AdminPermissions.vue')
      },
      {
        path: 'announcements',
        name: 'AdminAnnouncements',
        component: () => import('@/views/admin/AdminAnnouncements.vue')
      }
    ]
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('@/views/admin/AdminLogin.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  const token = localStorage.getItem('token')
  const companyToken = localStorage.getItem('companyToken')
  const adminToken = localStorage.getItem('adminToken')
  
  // 根路径强制重定向到登录页
  if (to.path === '/') {
    next('/login')
    return
  }
  
  // 如果访问登录页且已登录，重定向到首页
  if (to.path === '/login') {
    if (token && userStore.userInfo) {
      next('/app/home')
      return
    }
    next()
    return
  }
  
  // 验证token有效性（仅在有token且需要验证时）
  if (token && !userStore.userInfo && to.matched.some(record => record.meta.requiresAuth)) {
    try {
      userStore.setToken(token)
      await userStore.fetchUserInfo()
    } catch (error) {
      // Token无效，清除并重定向到登录页
      console.log('Token验证失败，跳转到登录页')
      userStore.logout()
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }
  }
  
  // 管理员路由守卫
  if (to.matched.some(record => record.meta.requiresAdminAuth)) {
    if (!adminToken) {
      next({ name: 'AdminLogin', query: { redirect: to.fullPath } })
      return
    }
  }
  
  // 企业HR路由守卫
  if (to.matched.some(record => record.meta.requiresCompanyAuth)) {
    if (!companyToken) {
      next({ name: 'CompanyLogin', query: { redirect: to.fullPath } })
      return
    }
  }
  
  // 求职者端路由守卫
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token || !userStore.userInfo) {
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }
  }
  
  next()
})

export default router
