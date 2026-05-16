<template>
  <div class="flex min-h-screen bg-slate-50">
    <!-- Sidebar -->
    <aside class="w-64 bg-gradient-to-b from-slate-900 to-slate-800 flex flex-col fixed left-0 top-0 bottom-0 z-40">
      <!-- Logo -->
      <div class="flex items-center gap-3 px-6 py-5 border-b border-slate-700">
        <div class="size-10 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-lg flex items-center justify-center">
          <span class="material-symbols-outlined text-white">business</span>
        </div>
        <div>
          <h2 class="text-white font-bold text-sm">企业招聘系统</h2>
          <p class="text-slate-400 text-xs">HR管理后台</p>
        </div>
      </div>
      
      <!-- Menu -->
      <nav class="flex-1 py-4 px-3 overflow-y-auto">
        <div class="space-y-1">
          <router-link 
            v-for="item in menuItems" 
            :key="item.path"
            :to="item.path"
            :class="[
              'flex items-center gap-3 px-3 py-2.5 rounded-lg transition-all',
              isActive(item.path) 
                ? 'bg-blue-500/20 text-blue-400' 
                : 'text-slate-400 hover:bg-slate-700/50 hover:text-white'
            ]"
          >
            <span class="material-symbols-outlined text-xl">{{ item.icon }}</span>
            <span class="text-sm font-medium">{{ item.label }}</span>
          </router-link>
        </div>
      </nav>
      
      <!-- Company Info -->
      <div class="p-4 border-t border-slate-700">
        <div class="bg-slate-800/50 rounded-lg p-3">
          <div class="flex items-center gap-2 mb-2">
            <span class="material-symbols-outlined text-blue-400 text-lg">business</span>
            <span class="text-white text-sm font-medium truncate">{{ companyInfo?.companyName || '企业名称' }}</span>
          </div>
          <div class="flex items-center justify-between">
            <span :class="['text-xs px-2 py-0.5 rounded-full', statusClass]">{{ companyStatusText }}</span>
            <button @click="handleLogout" class="text-xs text-slate-400 hover:text-white transition-colors">退出</button>
          </div>
        </div>
      </div>
    </aside>
    
    <!-- Main Content -->
    <main class="flex-1 ml-64">
      <!-- Top Header -->
      <header class="bg-white border-b border-slate-200 px-6 py-4 flex items-center justify-between sticky top-0 z-30">
        <div class="flex items-center gap-4">
          <h1 class="text-xl font-bold text-slate-800">{{ currentPageTitle }}</h1>
        </div>
        
<div class="flex items-center gap-4">
      <!-- Notifications -->
      <button @click="showNotifications" class="relative p-2 rounded-lg hover:bg-slate-100 transition-colors">
        <span class="material-symbols-outlined text-slate-600">notifications</span>
        <span v-if="unreadCount > 0" class="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full"></span>
      </button>

      <!-- User Menu -->
          <div class="flex items-center gap-3 pl-4 border-l border-slate-200">
            <div class="w-9 h-9 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-full flex items-center justify-center text-white font-bold text-sm">
              {{ companyInfo?.contactName?.charAt(0)?.toUpperCase() || 'H' }}
            </div>
            <div class="hidden sm:block">
              <p class="text-sm font-medium text-slate-800">{{ companyInfo?.contactName || 'HR' }}</p>
              <p class="text-xs text-slate-500">{{ companyInfo?.contactPosition || '企业用户' }}</p>
            </div>
          </div>
        </div>
      </header>
      
      <!-- Page Content -->
      <div class="p-6">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import api from '@/api'

const router = useRouter()
const route = useRoute()

const companyInfo = ref(JSON.parse(localStorage.getItem('companyInfo') || '{}'))

const menuItems = [
  { path: '/company/dashboard', label: '仪表盘', icon: 'dashboard' },
  { path: '/company/jobs', label: '职位管理', icon: 'work' },
  { path: '/company/jobs/create', label: '发布职位', icon: 'add_circle' },
  { path: '/company/resumes', label: '简历筛选', icon: 'person_search' },
  { path: '/company/interviews', label: '面试管理', icon: 'calendar_month' },
  { path: '/company/notifications', label: '通知中心', icon: 'notifications' },
  { path: '/company/audit-status', label: '审核进度', icon: 'pending_actions' },
  { path: '/company/profile', label: '企业资料', icon: 'business' }
]

const pageTitles = {
  '/company/dashboard': '仪表盘',
  '/company/jobs': '职位管理',
  '/company/jobs/create': '发布职位',
  '/company/resumes': '简历筛选',
  '/company/interviews': '面试管理',
  '/company/notifications': '通知中心',
  '/company/audit-status': '审核进度',
  '/company/profile': '企业资料'
}

const currentPageTitle = computed(() => {
  const current = menuItems.find(item => isActive(item.path))
  return current?.label || pageTitles[route.path] || '企业招聘系统'
})

const companyStatus = computed(() => companyInfo.value?.status || 'PENDING')
const companyStatusText = computed(() => {
  const statusMap = {
    'PENDING': '待审核',
    'APPROVED': '已认证',
    'REJECTED': '已拒绝'
  }
  return statusMap[companyStatus.value] || '未知'
})
const statusClass = computed(() => {
  const classMap = {
    'PENDING': 'bg-yellow-500/20 text-yellow-400',
    'APPROVED': 'bg-green-500/20 text-green-400',
    'REJECTED': 'bg-red-500/20 text-red-400'
  }
  return classMap[companyStatus.value] || 'bg-slate-500/20 text-slate-400'
})

const isActive = (path) => {
  if (path === '/company/dashboard') {
    return route.path === path
  }
  return route.path.startsWith(path)
}

const unreadCount = ref(0)

const showNotifications = () => {
  router.push('/company/notifications')
}

const fetchNotifications = async () => {
  const token = localStorage.getItem('companyToken')
  if (!token) {
    unreadCount.value = 0
    return
  }
  try {
    const data = await api.getCompanyUnreadCount()
    unreadCount.value = data || 0
  } catch (error) {
    console.error('获取通知失败', error)
    unreadCount.value = 0
  }
}

onMounted(async () => {
  await fetchNotifications()
})

const handleLogout = () => {
  localStorage.removeItem('companyToken')
  localStorage.removeItem('companyInfo')
  router.push('/company/login')
}
</script>
