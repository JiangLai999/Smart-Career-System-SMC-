<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-3xl font-black tracking-tight text-slate-900 dark:text-white">通知中心</h2>
        <p class="text-slate-500 dark:text-slate-400 mt-1">查看系统通知和消息</p>
      </div>
      <button 
        @click="markAllAsRead"
        :disabled="unreadNotifications.length === 0"
        class="px-4 py-2 text-sm font-medium text-blue-600 hover:text-blue-700 border border-blue-200 rounded-lg hover:bg-blue-50 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
      >
        全部标记已读
      </button>
    </div>
    
    <div class="flex gap-4 border-b border-slate-200 dark:border-slate-800 pb-4">
      <button
        v-for="tab in tabs"
        :key="tab.value"
        @click="activeTab = tab.value"
        :class="['px-4 py-2 text-sm font-medium rounded-lg transition-colors', activeTab === tab.value ? 'bg-blue-100 text-blue-700' : 'text-slate-500 hover:text-slate-700 hover:bg-slate-100']"
      >
        {{ tab.label }}
        <span v-if="tab.count > 0" :class="['ml-2 px-2 py-0.5 text-xs rounded-full', activeTab === tab.value ? 'bg-blue-200 text-blue-800' : 'bg-slate-200 text-slate-600']">
          {{ tab.count }}
        </span>
      </button>
    </div>
    
    <div v-if="loading" class="text-center py-12">
      <span class="material-symbols-outlined text-4xl text-slate-300 animate-spin">sync</span>
      <p class="text-slate-500 mt-2">加载中...</p>
    </div>
    
    <div v-else-if="filteredNotifications.length === 0" class="text-center py-12">
      <span class="material-symbols-outlined text-5xl text-slate-300 mb-4">notifications_none</span>
      <p class="text-slate-500">暂无通知</p>
    </div>
    
    <div v-else class="space-y-3">
        <div
          v-for="notification in filteredNotifications"
          :key="notification.id"
          :class="['bg-white dark:bg-slate-900 rounded-xl border p-5 cursor-pointer transition-all hover:shadow-md', notification.isRead ? 'border-slate-200 dark:border-slate-800' : 'border-blue-200 dark:border-blue-800 bg-blue-50/50 dark:bg-blue-900/10']"
          @click="handleNotificationClick(notification)"
        >
          <div class="flex items-start gap-4">
            <div :class="['w-10 h-10 rounded-full flex items-center justify-center flex-shrink-0', getTypeBgClass(notification.type)]">
              <span class="material-symbols-outlined text-lg" :class="getTypeTextClass(notification.type)">{{ getTypeIcon(notification.type) }}</span>
            </div>
            <div class="flex-1 min-w-0">
              <div class="flex items-center justify-between gap-2 flex-wrap">
                <div class="flex items-center gap-2">
                  <h3 :class="['font-semibold', notification.isRead ? 'text-slate-700 dark:text-slate-300' : 'text-slate-900 dark:text-white']">
                    {{ notification.title }}
                  </h3>
                  <span v-if="notification.source === 'ADMIN'" class="text-xs px-2 py-0.5 rounded-full bg-purple-100 text-purple-700 font-medium">管理端</span>
                  <span v-else-if="notification.source === 'SYSTEM'" class="text-xs px-2 py-0.5 rounded-full bg-slate-100 text-slate-600 font-medium">系统</span>
                  <span v-else class="text-xs px-2 py-0.5 rounded-full bg-blue-100 text-blue-700 font-medium">用户</span>
                </div>
                <span class="text-xs text-slate-500 whitespace-nowrap">{{ formatTime(notification.createTime) }}</span>
              </div>
              <p class="text-sm text-slate-500 dark:text-slate-400 mt-1 line-clamp-2">{{ notification.content }}</p>
            </div>
            <span v-if="!notification.isRead" class="w-2 h-2 bg-blue-500 rounded-full flex-shrink-0 mt-2"></span>
          </div>
        </div>
    </div>
    
    <div v-if="hasMore" class="text-center pt-4">
      <button 
        @click="loadMore"
        :disabled="loadingMore"
        class="px-6 py-2 text-sm font-medium text-slate-600 hover:text-slate-800 border border-slate-200 rounded-lg hover:bg-slate-50 disabled:opacity-50 transition-colors"
      >
        {{ loadingMore ? '加载中...' : '加载更多' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'

const router = useRouter()

const loading = ref(false)
const loadingMore = ref(false)
const notifications = ref([])
const activeTab = ref('all')
const page = ref(0)
const pageSize = 20
const hasMore = ref(true)

const tabs = computed(() => [
  { label: '全部', value: 'all', count: notifications.value.length },
  { label: '未读', value: 'unread', count: unreadNotifications.value.length },
  { label: '用户消息', value: 'user', count: notifications.value.filter(n => n.source === 'USER').length },
  { label: '系统公告', value: 'admin', count: notifications.value.filter(n => n.source === 'ADMIN' || n.source === 'SYSTEM').length },
  { label: '已读', value: 'read', count: readNotifications.value.length }
])

const unreadNotifications = computed(() => notifications.value.filter(n => !n.isRead))
const readNotifications = computed(() => notifications.value.filter(n => n.isRead))

const filteredNotifications = computed(() => {
  if (activeTab.value === 'unread') return unreadNotifications.value
  if (activeTab.value === 'read') return readNotifications.value
  if (activeTab.value === 'user') return notifications.value.filter(n => n.source === 'USER')
  if (activeTab.value === 'admin') return notifications.value.filter(n => n.source === 'ADMIN' || n.source === 'SYSTEM')
  return notifications.value
})

onMounted(() => {
  fetchNotifications()
})

const fetchNotifications = async () => {
  loading.value = true
  try {
    const data = await api.getCompanyNotifications({ page: page.value, size: pageSize })
    notifications.value = data.content || data || []
    hasMore.value = notifications.value.length >= pageSize
  } catch (error) {
    console.error('获取通知失败', error)
    notifications.value = []
  } finally {
    loading.value = false
  }
}

const loadMore = async () => {
  loadingMore.value = true
  try {
    page.value++
    const data = await api.getCompanyNotifications({ page: page.value, size: pageSize })
    const newNotifications = data.content || data || []
    notifications.value = [...notifications.value, ...newNotifications]
    hasMore.value = newNotifications.length >= pageSize
  } catch (error) {
    console.error('加载更多失败', error)
  } finally {
    loadingMore.value = false
  }
}

const markAllAsRead = async () => {
  try {
    await api.markAllCompanyNotificationsAsRead()
    notifications.value = notifications.value.map(n => ({ ...n, isRead: true }))
  } catch (error) {
    console.error('标记已读失败', error)
  }
}

const handleNotificationClick = async (notification) => {
  if (!notification.isRead) {
    try {
      await api.markCompanyNotificationAsRead(notification.id)
      notification.isRead = true
    } catch (error) {
      console.error('标记已读失败', error)
    }
  }

  if (notification.relatedType === 'APPLICATION') {
    router.push(`/company/resumes/${notification.relatedId}`)
  } else if (notification.relatedType === 'INTERVIEW') {
    router.push(`/company/interviews`)
  } else if (notification.relatedType === 'ANNOUNCEMENT') {
    // 公告直接展开内容，不跳转
  }
}

const getTypeIcon = (type) => {
  const map = {
    'APPLICATION': 'description',
    'HIRE': 'check_circle',
    'INTERVIEW': 'calendar_month',
    'REJECT': 'cancel',
    'WARNING': 'warning',
    'ANNOUNCEMENT': 'campaign',
    'SYSTEM': 'info'
  }
  return map[type] || 'notifications'
}

const getTypeBgClass = (type) => {
  const map = {
    'APPLICATION': 'bg-blue-100',
    'HIRE': 'bg-green-100',
    'INTERVIEW': 'bg-indigo-100',
    'REJECT': 'bg-red-100',
    'WARNING': 'bg-yellow-100',
    'ANNOUNCEMENT': 'bg-purple-100',
    'SYSTEM': 'bg-slate-100'
  }
  return map[type] || 'bg-slate-100'
}

const getTypeTextClass = (type) => {
  const map = {
    'APPLICATION': 'text-blue-600',
    'HIRE': 'text-green-600',
    'INTERVIEW': 'text-indigo-600',
    'REJECT': 'text-red-600',
    'WARNING': 'text-yellow-600',
    'ANNOUNCEMENT': 'text-purple-600',
    'SYSTEM': 'text-slate-600'
  }
  return map[type] || 'text-slate-600'
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  return date.toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
