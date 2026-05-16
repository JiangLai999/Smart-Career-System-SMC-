<template>
  <div class="flex min-h-screen">
    <!-- Top Navigation Bar -->
    <header class="fixed top-0 left-0 right-0 z-50 flex items-center justify-between whitespace-nowrap border-b border-slate-200 dark:border-slate-800 bg-gradient-to-r from-slate-900 to-slate-800 px-4 md:px-10 py-3 text-white">
      <div class="flex items-center gap-8">
        <div class="flex items-center gap-4 cursor-pointer" @click="$router.push('/')">
          <div class="size-8 bg-primary rounded-lg flex items-center justify-center">
            <span class="material-symbols-outlined text-white text-lg">work</span>
          </div>
          <div>
            <h2 class="text-white text-lg font-bold leading-tight tracking-tight hidden sm:block">求职者平台</h2>
            <span class="text-[10px] text-blue-400 font-medium tracking-wider hidden sm:block">JOB SEEKER PORTAL</span>
          </div>
        </div>
      </div>
      
      <div class="flex items-center gap-2 md:gap-6">
        <!-- Notifications -->
        <NotificationBell />
        
        <!-- User Info -->
        <template v-if="isLoggedIn">
          <div class="flex items-center gap-3">
            <div class="hidden md:flex flex-col items-end">
              <span class="text-xs font-semibold tracking-wide">{{ userInfo?.username }}</span>
              <span class="text-[10px] text-slate-400 font-medium tracking-wide">标准会员</span>
            </div>
            <div 
              class="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-10 border-2 border-slate-700 cursor-pointer"
              @click="$router.push('/app/profile')"
            >
              <img v-if="userInfo?.avatar" :src="userInfo.avatar" class="w-full h-full rounded-full object-cover" />
              <div v-else class="w-full h-full rounded-full bg-primary/20 flex items-center justify-center text-primary font-bold text-sm">
                {{ userInfo?.username?.charAt(0)?.toUpperCase() }}
              </div>
            </div>
          </div>
          <button 
            class="hidden md:flex min-w-[84px] cursor-pointer items-center justify-center rounded-lg h-10 px-4 bg-primary text-white text-sm font-bold tracking-wide transition-all hover:bg-primary/90"
            @click="handleLogout"
          >
            <span class="truncate">退出登录</span>
          </button>
        </template>
        <template v-else>
          <button 
            class="text-sm font-semibold text-slate-300 hover:text-white transition-colors tracking-wide"
            @click="$router.push('/login')"
          >
            登录
          </button>
          <button 
            class="min-w-[84px] cursor-pointer items-center justify-center rounded-lg h-10 px-4 bg-primary text-white text-sm font-bold tracking-wide transition-all hover:bg-primary/90"
            @click="$router.push('/register')"
          >
            注册
          </button>
        </template>
      </div>
    </header>
    
    <!-- Sidebar -->
    <aside class="hidden lg:flex w-64 flex-col bg-slate-900 dark:bg-black p-4 gap-2 border-r border-slate-800 fixed left-0 top-[65px] bottom-0 z-40">
      <div 
        v-for="item in menuItems" 
        :key="item.path"
        :class="[
          'flex items-center gap-3 px-3 py-3 rounded-xl cursor-pointer transition-all',
          isActive(item.path) 
            ? 'bg-primary text-white shadow-lg shadow-primary/20' 
            : 'text-slate-400 hover:bg-slate-800 hover:text-white'
        ]"
        @click="handleMenuClick(item.path)"
      >
        <span class="material-symbols-outlined text-xl">{{ item.icon }}</span>
        <p class="text-sm font-semibold tracking-wide">{{ item.label }}</p>
      </div>
      
      <div class="mt-auto flex items-center gap-3 px-3 py-3 rounded-xl text-slate-400 hover:bg-slate-800 hover:text-white transition-all cursor-pointer" @click="$router.push('/app/profile')">
        <span class="material-symbols-outlined text-xl">settings</span>
        <p class="text-sm font-semibold tracking-wide">设置</p>
      </div>
    </aside>
    
    <!-- Main Content -->
    <main class="flex-1 lg:ml-64 mt-[65px] p-4 md:p-8 overflow-y-auto min-h-[calc(100vh-65px)]">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import NotificationBell from './NotificationBell.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const menuItems = [
  { path: '/app/home', label: '首页概览', icon: 'dashboard' },
  { path: '/app/jobs', label: '职位查找', icon: 'search_check' },
  { path: '/app/resume', label: '我的简历', icon: 'description' },
  { path: '/app/applications', label: '申请记录', icon: 'history' },
  { path: '/app/interviews', label: '面试管理', icon: 'event' },
  { path: '/app/notifications', label: '消息通知', icon: 'notifications' },
  { path: '/app/assessment', label: '职业评估', icon: 'school' },
  { path: '/app/recommendation', label: '智能推荐', icon: 'recommend' },
  { path: '/app/career-advice', label: '职业规划', icon: 'timeline' },
]

const isLoggedIn = computed(() => userStore.isLoggedIn)
const userInfo = computed(() => userStore.userInfo)

const isActive = (path) => {
  return route.path === path || route.path.startsWith(path + '/')
}

const handleMenuClick = (path) => {
  router.push(path)
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>
