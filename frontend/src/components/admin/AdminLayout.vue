<template>
  <div class="flex min-h-screen bg-slate-50">
    <!-- Sidebar -->
    <aside class="fixed left-0 top-0 bottom-0 w-64 bg-gradient-to-b from-slate-900 via-slate-800 to-slate-900 text-white z-50 flex flex-col">
      <!-- Logo -->
      <div class="p-6 border-b border-slate-700/50">
        <div class="flex items-center gap-3">
          <div class="size-12 bg-gradient-to-br from-violet-500 to-purple-600 rounded-xl flex items-center justify-center shadow-lg shadow-purple-500/30">
            <span class="material-symbols-outlined text-white text-2xl">admin_panel_settings</span>
          </div>
          <div>
            <h1 class="text-lg font-bold tracking-tight">管理中心</h1>
            <p class="text-xs text-slate-400">Smart Career Admin</p>
          </div>
        </div>
      </div>
      
      <!-- Navigation -->
      <nav class="flex-1 p-4 space-y-1 overflow-y-auto">
        <div class="mb-6">
          <p class="text-xs font-semibold text-slate-500 uppercase tracking-wider px-3 mb-3">概览</p>
          <router-link 
            to="/admin" 
            :class="[
              'flex items-center gap-3 px-3 py-3 rounded-xl transition-all group',
              route.path === '/admin' 
                ? 'bg-gradient-to-r from-violet-500 to-purple-600 text-white shadow-lg shadow-purple-500/30' 
                : 'text-slate-400 hover:bg-slate-700/50 hover:text-white'
            ]"
          >
            <span class="material-symbols-outlined text-xl group-hover:scale-110 transition-transform">dashboard</span>
            <span class="text-sm font-medium">仪表盘</span>
          </router-link>
        </div>
        
        <div class="mb-6">
          <p class="text-xs font-semibold text-slate-500 uppercase tracking-wider px-3 mb-3">管理</p>
          <router-link 
            v-for="item in managementItems" 
            :key="item.path"
            :to="item.path"
            :class="[
              'flex items-center gap-3 px-3 py-3 rounded-xl transition-all group mb-1',
              route.path === item.path 
                ? 'bg-gradient-to-r from-violet-500 to-purple-600 text-white shadow-lg shadow-purple-500/30' 
                : 'text-slate-400 hover:bg-slate-700/50 hover:text-white'
            ]"
          >
            <span class="material-symbols-outlined text-xl group-hover:scale-110 transition-transform">{{ item.icon }}</span>
            <span class="text-sm font-medium">{{ item.label }}</span>
            <span 
              v-if="item.badge" 
              class="ml-auto px-2 py-0.5 text-xs font-bold bg-red-500 text-white rounded-full"
            >
              {{ item.badge }}
            </span>
          </router-link>
        </div>
        
        <div class="mb-6">
          <p class="text-xs font-semibold text-slate-500 uppercase tracking-wider px-3 mb-3">系统</p>
          <router-link 
            v-for="item in systemItems" 
            :key="item.path"
            :to="item.path"
            :class="[
              'flex items-center gap-3 px-3 py-3 rounded-xl transition-all group mb-1',
              route.path === item.path 
                ? 'bg-gradient-to-r from-violet-500 to-purple-600 text-white shadow-lg shadow-purple-500/30' 
                : 'text-slate-400 hover:bg-slate-700/50 hover:text-white'
            ]"
          >
            <span class="material-symbols-outlined text-xl group-hover:scale-110 transition-transform">{{ item.icon }}</span>
            <span class="text-sm font-medium">{{ item.label }}</span>
          </router-link>
        </div>
      </nav>
      
      <!-- User Profile -->
      <div class="p-4 border-t border-slate-700/50">
        <div class="flex items-center gap-3 p-3 rounded-xl bg-slate-700/30">
          <div class="size-10 bg-gradient-to-br from-violet-400 to-purple-500 rounded-full flex items-center justify-center text-white font-bold">
            {{ adminInfo?.username?.charAt(0)?.toUpperCase() || 'A' }}
          </div>
          <div class="flex-1 min-w-0">
            <p class="text-sm font-semibold truncate">{{ adminInfo?.username || '管理员' }}</p>
            <p class="text-xs text-slate-400">系统管理员</p>
          </div>
          <button 
            @click="handleLogout"
            class="text-slate-400 hover:text-white transition-colors"
            title="退出登录"
          >
            <span class="material-symbols-outlined text-xl">logout</span>
          </button>
        </div>
      </div>
    </aside>
    
    <!-- Main Content -->
    <main class="flex-1 ml-64">
      <!-- Top Bar -->
      <header class="sticky top-0 z-40 bg-white/80 backdrop-blur-xl border-b border-slate-200">
        <div class="flex items-center justify-between px-8 py-4">
          <div>
            <h2 class="text-xl font-bold text-slate-900">{{ pageTitle }}</h2>
            <p class="text-sm text-slate-500">{{ pageDescription }}</p>
          </div>
          
          <div class="flex items-center gap-4">
            <!-- Admin info display only -->
            <div class="flex items-center gap-2 text-sm text-slate-500">
              <span>当前管理员:</span>
              <span class="font-semibold text-slate-900">{{ adminInfo?.username || 'admin' }}</span>
            </div>
          </div>
        </div>
      </header>
      
      <!-- Page Content -->
      <div class="p-8">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import api from '@/api'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const adminInfo = computed(() => userStore.userInfo)
const pendingAuditCount = ref(0)

const fetchPendingAudits = async () => {
  try {
    const response = await api.getPendingAudits({ size: 1 })
    const total = response?.data?.totalElements || response?.totalElements || 0
    pendingAuditCount.value = total > 99 ? '99+' : total
  } catch (error) {
    console.error('获取待审核数量失败', error)
  }
}

const managementItems = computed(() => [
  { path: '/admin/users', label: '用户管理', icon: 'people', badge: null },
  { path: '/admin/audits', label: '企业审核', icon: 'fact_check', badge: pendingAuditCount.value > 0 ? pendingAuditCount.value : null },
  { path: '/admin/announcements', label: '公告管理', icon: 'campaign', badge: null },
  { path: '/admin/permissions', label: '权限管理', icon: 'admin_panel_settings', badge: null },
])

const systemItems = [
  { path: '/admin/settings', label: '系统设置', icon: 'settings' },
  { path: '/admin/logs', label: '系统日志', icon: 'description' },
]

const pageTitle = computed(() => {
  const titles = {
    '/admin': '仪表盘概览',
    '/admin/users': '用户管理',
    '/admin/audits': '企业审核',
    '/admin/announcements': '公告管理',
    '/admin/permissions': '权限管理',
    '/admin/settings': '系统设置',
    '/admin/logs': '系统日志'
  }
  return titles[route.path] || '管理中心'
})

const pageDescription = computed(() => {
  const descriptions = {
    '/admin': '系统运营数据与关键指标',
    '/admin/users': '管理所有用户账户和权限',
    '/admin/audits': '审核企业资质和信息',
    '/admin/announcements': '发布和管理系统公告',
    '/admin/permissions': '配置角色和访问权限',
    '/admin/settings': '系统配置和参数设置',
    '/admin/logs': '查看系统操作日志'
  }
  return descriptions[route.path] || ''
})

const handleLogout = () => {
  userStore.logout()
  router.push('/admin/login')
}

onMounted(() => {
  fetchPendingAudits()
})
</script>
