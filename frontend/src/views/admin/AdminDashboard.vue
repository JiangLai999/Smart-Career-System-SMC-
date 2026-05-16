<template>
  <div>
    <!-- Welcome Section -->
    <div class="mb-8">
      <h1 class="text-3xl font-extrabold text-slate-900 tracking-tight">
        欢迎回来，{{ adminInfo?.username || '管理员' }}! 👋
      </h1>
      <p class="text-slate-500 mt-2">以下是系统运营的最新数据和概览。</p>
    </div>
    
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6 mb-8">
      <div class="group bg-white rounded-2xl p-6 shadow-sm border border-slate-200 hover:shadow-xl hover:border-violet-200 transition-all duration-300 cursor-pointer">
        <div class="flex items-start justify-between mb-4">
          <div class="p-3 bg-gradient-to-br from-violet-500 to-purple-600 rounded-xl shadow-lg shadow-violet-500/30">
            <span class="material-symbols-outlined text-white text-2xl">people</span>
          </div>
          <span class="text-xs font-semibold text-emerald-600 bg-emerald-50 px-2 py-1 rounded-full">
            +12%
          </span>
        </div>
        <div>
          <p class="text-sm font-medium text-slate-500 mb-1">总用户数</p>
          <p class="text-3xl font-bold text-slate-900">{{ stats.totalUsers || 0 }}</p>
          <div class="mt-3 flex items-center gap-2 text-xs text-slate-600">
            <span class="flex items-center gap-1">
              <span class="w-2 h-2 bg-blue-500 rounded-full"></span>
              求职者 {{ stats.totalJobSeekers || 0 }}
            </span>
            <span class="flex items-center gap-1">
              <span class="w-2 h-2 bg-emerald-500 rounded-full"></span>
              企业 {{ stats.totalEnterprises || 0 }}
            </span>
          </div>
        </div>
      </div>
      
      <div class="group bg-white rounded-2xl p-6 shadow-sm border border-slate-200 hover:shadow-xl hover:border-blue-200 transition-all duration-300 cursor-pointer">
        <div class="flex items-start justify-between mb-4">
          <div class="p-3 bg-gradient-to-br from-blue-500 to-cyan-500 rounded-xl shadow-lg shadow-blue-500/30">
            <span class="material-symbols-outlined text-white text-2xl">business</span>
          </div>
          <span class="text-xs font-semibold text-emerald-600 bg-emerald-50 px-2 py-1 rounded-full">
            +8%
          </span>
        </div>
        <div>
          <p class="text-sm font-medium text-slate-500 mb-1">企业总数</p>
          <p class="text-3xl font-bold text-slate-900">{{ stats.totalEnterprises || 0 }}</p>
          <div class="mt-3 flex items-center gap-2 text-xs text-slate-600">
            <span class="flex items-center gap-1">
              <span class="w-2 h-2 bg-emerald-500 rounded-full"></span>
              已认证 {{ stats.approvedEnterprises || 0 }}
            </span>
            <span class="flex items-center gap-1">
              <span class="w-2 h-2 bg-amber-500 rounded-full"></span>
              待审核 {{ stats.pendingAudits || 0 }}
            </span>
          </div>
        </div>
      </div>
      
      <div class="group bg-white rounded-2xl p-6 shadow-sm border border-slate-200 hover:shadow-xl hover:border-amber-200 transition-all duration-300 cursor-pointer">
        <div class="flex items-start justify-between mb-4">
          <div class="p-3 bg-gradient-to-br from-amber-500 to-orange-500 rounded-xl shadow-lg shadow-amber-500/30">
            <span class="material-symbols-outlined text-white text-2xl">pending_actions</span>
          </div>
          <span class="text-xs font-semibold text-amber-600 bg-amber-50 px-2 py-1 rounded-full">
            待处理
          </span>
        </div>
        <div>
          <p class="text-sm font-medium text-slate-500 mb-1">待审核企业</p>
          <p class="text-3xl font-bold text-slate-900">{{ stats.pendingAudits || 0 }}</p>
          <button 
            @click="$router.push('/admin/audits')"
            class="mt-3 text-xs font-semibold text-amber-600 hover:text-amber-700 flex items-center gap-1"
          >
            <span>立即处理</span>
            <span class="material-symbols-outlined text-sm">arrow_forward</span>
          </button>
        </div>
      </div>
      
      <div class="group bg-white rounded-2xl p-6 shadow-sm border border-slate-200 hover:shadow-xl hover:border-emerald-200 transition-all duration-300 cursor-pointer">
        <div class="flex items-start justify-between mb-4">
          <div class="p-3 bg-gradient-to-br from-emerald-500 to-teal-500 rounded-xl shadow-lg shadow-emerald-500/30">
            <span class="material-symbols-outlined text-white text-2xl">trending_up</span>
          </div>
          <span class="text-xs font-semibold text-emerald-600 bg-emerald-50 px-2 py-1 rounded-full">
            活跃
          </span>
        </div>
        <div>
          <p class="text-sm font-medium text-slate-500 mb-1">今日活跃</p>
          <p class="text-3xl font-bold text-slate-900">{{ stats.activeUsers || 0 }}</p>
          <p class="mt-3 text-xs text-slate-600">
            较昨日 <span class="text-emerald-600 font-semibold">+15%</span>
          </p>
        </div>
      </div>
    </div>
    
    <!-- Charts & Activity -->
    <div class="grid grid-cols-1 xl:grid-cols-3 gap-6 mb-8">
      <!-- Quick Actions -->
      <div class="bg-white rounded-2xl p-6 shadow-sm border border-slate-200">
        <h3 class="text-lg font-bold text-slate-900 mb-6 flex items-center gap-2">
          <span class="material-symbols-outlined text-violet-500">bolt</span>
          快速操作
        </h3>
        <div class="grid grid-cols-2 gap-4">
          <button 
            @click="$router.push('/admin/users')"
            class="group p-6 rounded-xl bg-gradient-to-br from-violet-50 to-purple-50 border border-violet-100 hover:border-violet-200 hover:shadow-lg transition-all"
          >
            <span class="material-symbols-outlined text-4xl text-violet-500 group-hover:scale-110 transition-transform">person_add</span>
            <p class="mt-3 text-sm font-semibold text-slate-900">添加用户</p>
          </button>
          
          <button 
            @click="$router.push('/admin/audits')"
            class="group p-6 rounded-xl bg-gradient-to-br from-blue-50 to-cyan-50 border border-blue-100 hover:border-blue-200 hover:shadow-lg transition-all"
          >
            <span class="material-symbols-outlined text-4xl text-blue-500 group-hover:scale-110 transition-transform">fact_check</span>
            <p class="mt-3 text-sm font-semibold text-slate-900">企业审核</p>
          </button>
          
          <button 
            @click="$router.push('/admin/settings')"
            class="group p-6 rounded-xl bg-gradient-to-br from-amber-50 to-orange-50 border border-amber-100 hover:border-amber-200 hover:shadow-lg transition-all"
          >
            <span class="material-symbols-outlined text-4xl text-amber-500 group-hover:scale-110 transition-transform">settings</span>
            <p class="mt-3 text-sm font-semibold text-slate-900">系统设置</p>
          </button>
          
          <button 
            @click="$router.push('/admin/logs')"
            class="group p-6 rounded-xl bg-gradient-to-br from-emerald-50 to-teal-50 border border-emerald-100 hover:border-emerald-200 hover:shadow-lg transition-all"
          >
            <span class="material-symbols-outlined text-4xl text-emerald-500 group-hover:scale-110 transition-transform">description</span>
            <p class="mt-3 text-sm font-semibold text-slate-900">查看日志</p>
          </button>
        </div>
      </div>
      
      <!-- Recent Activity -->
      <div class="bg-white rounded-2xl p-6 shadow-sm border border-slate-200 xl:col-span-2">
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-lg font-bold text-slate-900 flex items-center gap-2">
            <span class="material-symbols-outlined text-blue-500">history</span>
            最近活动
          </h3>
          <button @click="$router.push('/admin/logs')" class="text-sm font-semibold text-violet-600 hover:text-violet-700 flex items-center gap-1">
            查看全部
            <span class="material-symbols-outlined text-sm">arrow_forward</span>
          </button>
        </div>
        
        <div v-if="recentActivities.length > 0" class="space-y-4">
          <div 
            v-for="activity in recentActivities" 
            :key="activity.id"
            class="flex items-start gap-4 p-4 rounded-xl bg-slate-50 hover:bg-slate-100 transition-colors cursor-pointer"
            @click="goToActivityDetail(activity)"
          >
            <div :class="[
              'p-2 rounded-lg',
              activity.type === 'user' ? 'bg-violet-100 text-violet-600' :
              activity.type === 'enterprise' ? 'bg-blue-100 text-blue-600' :
              activity.type === 'audit' ? 'bg-amber-100 text-amber-600' :
              'bg-emerald-100 text-emerald-600'
            ]">
              <span class="material-symbols-outlined">{{ activity.icon }}</span>
            </div>
            <div class="flex-1 min-w-0">
              <p class="text-sm font-semibold text-slate-900">{{ activity.title }}</p>
              <p class="text-xs text-slate-500 mt-1">{{ activity.description }}</p>
            </div>
            <span class="text-xs text-slate-400 whitespace-nowrap">{{ activity.time }}</span>
          </div>
        </div>
        
        <div v-else class="text-center py-8">
          <span class="material-symbols-outlined text-4xl text-slate-300 mb-2">history</span>
          <p class="text-sm text-slate-500">暂无最近活动</p>
        </div>
      </div>
    </div>
    
    <!-- System Status & Info -->
    <div class="grid grid-cols-1 xl:grid-cols-2 gap-6">
      <!-- System Status -->
      <div class="bg-white rounded-2xl p-6 shadow-sm border border-slate-200">
        <h3 class="text-lg font-bold text-slate-900 mb-6 flex items-center gap-2">
          <span class="material-symbols-outlined text-emerald-500">check_circle</span>
          系统状态
        </h3>
        
        <div class="space-y-4">
          <div class="flex items-center justify-between p-4 bg-slate-50 rounded-xl">
            <div class="flex items-center gap-3">
              <div :class="['w-3 h-3 rounded-full animate-pulse', systemStatus.database === '正常' ? 'bg-emerald-500' : 'bg-red-500']"></div>
              <span class="text-sm font-medium text-slate-700">数据库连接</span>
            </div>
            <span :class="['text-sm font-semibold', systemStatus.database === '正常' ? 'text-emerald-600' : 'text-red-600']">{{ systemStatus.database }}</span>
          </div>
          
          <div class="flex items-center justify-between p-4 bg-slate-50 rounded-xl">
            <div class="flex items-center gap-3">
              <div :class="['w-3 h-3 rounded-full animate-pulse', systemStatus.api === '正常' ? 'bg-emerald-500' : 'bg-amber-500']"></div>
              <span class="text-sm font-medium text-slate-700">API服务</span>
            </div>
            <span :class="['text-sm font-semibold', systemStatus.api === '正常' ? 'text-emerald-600' : 'text-amber-600']">{{ systemStatus.api }}</span>
          </div>
          
          <div class="flex items-center justify-between p-4 bg-slate-50 rounded-xl">
            <div class="flex items-center gap-3">
              <div :class="['w-3 h-3 rounded-full animate-pulse', systemStatus.email === '正常' ? 'bg-emerald-500' : 'bg-amber-500']"></div>
              <span class="text-sm font-medium text-slate-700">邮件服务</span>
            </div>
            <span :class="['text-sm font-semibold', systemStatus.email === '正常' ? 'text-emerald-600' : 'text-amber-600']">{{ systemStatus.email }}</span>
          </div>
          
          <div class="flex items-center justify-between p-4 bg-slate-50 rounded-xl">
            <div class="flex items-center gap-3">
              <div :class="['w-3 h-3 rounded-full animate-pulse', systemStatus.storage === '正常' ? 'bg-emerald-500' : 'bg-red-500']"></div>
              <span class="text-sm font-medium text-slate-700">文件存储</span>
            </div>
            <span :class="['text-sm font-semibold', systemStatus.storage === '正常' ? 'text-emerald-600' : 'text-red-600']">{{ systemStatus.storage }}</span>
          </div>
        </div>
      </div>
      
      <!-- System Info -->
      <div class="bg-white rounded-2xl p-6 shadow-sm border border-slate-200">
        <h3 class="text-lg font-bold text-slate-900 mb-6 flex items-center gap-2">
          <span class="material-symbols-outlined text-blue-500">info</span>
          系统信息
        </h3>
        
        <div class="space-y-4">
          <div class="flex items-center justify-between p-4 bg-gradient-to-r from-violet-50 to-purple-50 rounded-xl border border-violet-100">
            <span class="text-sm font-medium text-slate-700">系统版本</span>
            <span class="text-sm font-bold text-violet-600">{{ systemInfo.version }}</span>
          </div>
          
          <div class="flex items-center justify-between p-4 bg-slate-50 rounded-xl">
            <span class="text-sm font-medium text-slate-700">运行时间</span>
            <span class="text-sm font-bold text-slate-900">{{ systemInfo.uptime }}</span>
          </div>
          
          <div class="flex items-center justify-between p-4 bg-slate-50 rounded-xl">
            <span class="text-sm font-medium text-slate-700">数据库版本</span>
            <span class="text-sm font-bold text-slate-900">{{ systemInfo.databaseVersion }}</span>
          </div>
          
          <div class="flex items-center justify-between p-4 bg-slate-50 rounded-xl">
            <span class="text-sm font-medium text-slate-700">Java版本</span>
            <span class="text-sm font-bold text-slate-900">{{ systemInfo.javaVersion }}</span>
          </div>
          
          <div class="flex items-center justify-between p-4 bg-slate-50 rounded-xl">
            <span class="text-sm font-medium text-slate-700">操作系统</span>
            <span class="text-sm font-bold text-slate-900">{{ systemInfo.osName }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import api from '@/api'

const router = useRouter()
const userStore = useUserStore()
const adminInfo = computed(() => userStore.userInfo)

const stats = ref({
  totalUsers: 0,
  totalJobSeekers: 0,
  totalEnterprises: 0,
  approvedEnterprises: 0,
  pendingAudits: 0,
  activeUsers: 0,
  recentLogs: 0
})

const systemStatus = ref({
  database: '检测中...',
  api: '正常',
  email: '未配置',
  storage: '正常'
})

const systemInfo = ref({
  version: '-',
  startTime: null,
  uptime: '-',
  databaseVersion: '-',
  environment: 'Production',
  lastUpdate: '-',
  javaVersion: '-',
  osName: '-'
})

const recentActivities = ref([])

const getOperationIcon = (type) => {
  const icons = {
    'LOGIN': 'login',
    'REGISTER': 'person_add',
    'JOB_POST': 'work',
    'JOB_APPLY': 'send',
    'USER_DISABLE': 'block',
    'USER_ENABLE': 'check_circle',
    'USER_DELETE': 'delete',
    'AUDIT_APPROVE': 'fact_check',
    'AUDIT_REJECT': 'cancel',
    'SETTING_CHANGE': 'settings'
  }
  return icons[type] || 'info'
}

const getOperationType = (type) => {
  const types = {
    'LOGIN': 'user',
    'REGISTER': 'user',
    'JOB_POST': 'enterprise',
    'JOB_APPLY': 'user',
    'USER_DISABLE': 'user',
    'USER_ENABLE': 'user',
    'USER_DELETE': 'user',
    'AUDIT_APPROVE': 'audit',
    'AUDIT_REJECT': 'audit',
    'SETTING_CHANGE': 'system'
  }
  return types[type] || 'system'
}

const formatTimeAgo = (time) => {
  if (!time) return ''
  const now = new Date()
  const date = new Date(time)
  const diff = now - date
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  return `${days}天前`
}

let uptimeTimer = null

const calculateUptime = () => {
  if (!systemInfo.value.startTime) return
  const now = Date.now()
  const diff = now - systemInfo.value.startTime
  const days = Math.floor(diff / (24 * 60 * 60 * 1000))
  const hours = Math.floor((diff % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000))
  const minutes = Math.floor((diff % (60 * 60 * 1000)) / (60 * 1000))
  const seconds = Math.floor((diff % (60 * 1000)) / 1000)
  systemInfo.value.uptime = `${days}天 ${hours}小时 ${minutes}分 ${seconds}秒`
}

const startUptimeTimer = () => {
  uptimeTimer = setInterval(calculateUptime, 1000)
}

const goToActivityDetail = (activity) => {
  if (activity.type === 'audit') {
    router.push('/admin/audits')
  } else if (activity.type === 'user') {
    router.push('/admin/users')
  } else {
    router.push('/admin/logs')
  }
}

onMounted(async () => {
  try {
    const response = await api.getAdminDashboard()
    if (response && response.data) {
      stats.value = { ...stats.value, ...response.data }
      if (response.data.systemStatus) {
        systemStatus.value = { ...systemStatus.value, ...response.data.systemStatus }
      }
      if (response.data.systemInfo) {
        systemInfo.value = { ...systemInfo.value, ...response.data.systemInfo }
        calculateUptime()
        startUptimeTimer()
      }
    } else if (response) {
      stats.value = { ...stats.value, ...response }
      if (response.systemStatus) {
        systemStatus.value = { ...systemStatus.value, ...response.systemStatus }
      }
      if (response.systemInfo) {
        systemInfo.value = { ...systemInfo.value, ...response.systemInfo }
        calculateUptime()
        startUptimeTimer()
      }
    }
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
  
  try {
    const logsResponse = await api.getAdminLogs({ size: 5 })
    const logsData = logsResponse?.data?.content || logsResponse?.content || []
    if (logsData.length > 0) {
      recentActivities.value = logsData.map(log => ({
        id: log.id,
        type: getOperationType(log.operationType),
        icon: getOperationIcon(log.operationType),
        title: log.operationDetail || log.operationType,
        description: `用户: ${log.username || '系统'}`,
        time: formatTimeAgo(log.operationTime)
      }))
    }
  } catch (error) {
    console.error('获取活动日志失败', error)
  }
})

onUnmounted(() => {
  if (uptimeTimer) {
    clearInterval(uptimeTimer)
    uptimeTimer = null
  }
})
</script>
