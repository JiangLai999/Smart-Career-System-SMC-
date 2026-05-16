<template>
  <div>
    <!-- Header -->
    <div class="p-6 border-b border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900/50 mb-6 rounded-t-2xl">
      <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 class="text-3xl font-black text-slate-900 dark:text-slate-100 tracking-tight">通知中心</h2>
          <p class="text-slate-500 dark:text-slate-400 mt-1">查看所有通知消息</p>
        </div>
        <div class="flex items-center gap-3">
          <button
            v-if="unreadCount > 0"
            @click="markAllAsRead"
            class="px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary/90 transition-colors text-sm font-medium"
          >
            全部标记已读
          </button>
        </div>
      </div>
    </div>

    <!-- Filter Tabs -->
    <div class="flex gap-2 mb-6 overflow-x-auto pb-2">
      <button 
        v-for="tab in filterTabs" 
        :key="tab.value"
        :class="[
          'px-4 py-2 rounded-lg font-medium text-sm whitespace-nowrap transition-colors',
          activeFilter === tab.value 
            ? 'bg-primary text-white' 
            : 'bg-white dark:bg-slate-900 text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'
        ]"
        @click="activeFilter = tab.value"
      >
        {{ tab.label }}
      </button>
    </div>

    <!-- Notifications List -->
    <div class="space-y-4">
      <div v-if="loading" class="text-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-4 border-primary border-t-transparent mx-auto"></div>
        <p class="text-slate-500 dark:text-slate-400 mt-4">加载中...</p>
      </div>

      <div v-else-if="filteredNotifications.length === 0" class="text-center py-12">
        <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">notifications_off</span>
        <p class="text-slate-500 dark:text-slate-400 mt-2">暂无通知</p>
      </div>

      <div v-else class="bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-slate-200 dark:border-slate-800 divide-y divide-slate-100 dark:divide-slate-800">
        <div
          v-for="notification in filteredNotifications"
          :key="notification.id"
          @click="handleNotificationClick(notification)"
          :class="[
            'p-4 cursor-pointer transition-colors hover:bg-slate-50 dark:hover:bg-slate-800',
            notification.isRead === 0 ? 'bg-blue-50/50 dark:bg-blue-900/10' : ''
          ]"
        >
          <div class="flex gap-3">
            <div
              :class="[
                'flex-shrink-0 w-10 h-10 rounded-full flex items-center justify-center',
                getNotificationIcon(notification.type).bgColor
              ]"
            >
              <span class="material-symbols-outlined text-xl" :class="getNotificationIcon(notification.type).textColor">
                {{ getNotificationIcon(notification.type).icon }}
              </span>
            </div>
            <div class="flex-1 min-w-0">
              <div class="flex items-start justify-between gap-2">
                <h4 class="text-sm font-semibold text-slate-900 dark:text-white">
                  {{ notification.title }}
                </h4>
                <span class="text-xs text-slate-500 dark:text-slate-400 whitespace-nowrap">
                  {{ formatTime(notification.createTime) }}
                </span>
              </div>
              <p class="text-sm text-slate-600 dark:text-slate-400 mt-1">
                {{ notification.content }}
              </p>
              <div v-if="notification.isRead === 0" class="mt-2">
                <span class="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200">
                  未读
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Pagination -->
      <div v-if="totalPages > 1" class="flex items-center justify-center gap-2 mt-6">
        <button
          @click="currentPage = Math.max(0, currentPage - 1)"
          :disabled="currentPage === 0"
          :class="[
            'px-4 py-2 rounded-lg font-medium text-sm transition-colors',
            currentPage === 0
              ? 'bg-slate-100 text-slate-400 cursor-not-allowed'
              : 'bg-white dark:bg-slate-900 text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'
          ]"
        >
          上一页
        </button>
        <span class="text-sm text-slate-600 dark:text-slate-400">
          {{ currentPage + 1 }} / {{ totalPages }}
        </span>
        <button
          @click="currentPage = Math.min(totalPages - 1, currentPage + 1)"
          :disabled="currentPage === totalPages - 1"
          :class="[
            'px-4 py-2 rounded-lg font-medium text-sm transition-colors',
            currentPage === totalPages - 1
              ? 'bg-slate-100 text-slate-400 cursor-not-allowed'
              : 'bg-white dark:bg-slate-900 text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'
          ]"
        >
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'

const router = useRouter()
const loading = ref(false)
const notifications = ref([])
const unreadCount = ref(0)
const activeFilter = ref('all')
const currentPage = ref(0)
const pageSize = 10
const totalPages = ref(1)

const filterTabs = [
  { label: '全部', value: 'all' },
  { label: '未读', value: 'unread' },
  { label: '已读', value: 'read' }
]

const getNotificationIcon = (type) => {
  const icons = {
    WARNING: {
      icon: 'warning',
      bgColor: 'bg-amber-100 dark:bg-amber-900/30',
      textColor: 'text-amber-600 dark:text-amber-400'
    },
    HIRE: {
      icon: 'check_circle',
      bgColor: 'bg-green-100 dark:bg-green-900/30',
      textColor: 'text-green-600 dark:text-green-400'
    },
    INTERVIEW: {
      icon: 'event',
      bgColor: 'bg-blue-100 dark:bg-blue-900/30',
      textColor: 'text-blue-600 dark:text-blue-400'
    },
    REJECT: {
      icon: 'cancel',
      bgColor: 'bg-red-100 dark:bg-red-900/30',
      textColor: 'text-red-600 dark:text-red-400'
    },
    ANNOUNCEMENT: {
      icon: 'campaign',
      bgColor: 'bg-purple-100 dark:bg-purple-900/30',
      textColor: 'text-purple-600 dark:text-purple-400'
    },
    SYSTEM: {
      icon: 'info',
      bgColor: 'bg-slate-100 dark:bg-slate-700',
      textColor: 'text-slate-600 dark:text-slate-400'
    }
  }
  return icons[type] || icons.SYSTEM
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

const filteredNotifications = computed(() => {
  if (activeFilter.value === 'unread') {
    return notifications.value.filter(n => n.isRead === 0)
  } else if (activeFilter.value === 'read') {
    return notifications.value.filter(n => n.isRead === 1)
  }
  return notifications.value
})

const fetchNotifications = async () => {
  loading.value = true
  try {
    const data = await api.getNotificationsPage({ page: currentPage.value, size: pageSize })
    notifications.value = data.content || []
    totalPages.value = data.totalPages || 1
    await fetchUnreadCount()
  } catch (error) {
    console.error('获取通知列表失败:', error)
  } finally {
    loading.value = false
  }
}

const fetchUnreadCount = async () => {
  try {
    const count = await api.getUnreadCount()
    unreadCount.value = count
  } catch (error) {
    console.error('获取未读数量失败:', error)
  }
}

const handleNotificationClick = async (notification) => {
  if (notification.isRead === 0) {
    try {
      await api.markNotificationAsRead(notification.id)
      notification.isRead = 1
      unreadCount.value = Math.max(0, unreadCount.value - 1)
    } catch (error) {
      console.error('标记已读失败:', error)
    }
  }

  if (notification.relatedType === 'APPLICATION') {
    router.push('/app/applications')
  } else if (notification.relatedType === 'INTERVIEW') {
    router.push('/app/interviews')
  }
}

const markAllAsRead = async () => {
  try {
    await api.markAllNotificationsAsRead()
    notifications.value.forEach(n => n.isRead = 1)
    unreadCount.value = 0
  } catch (error) {
    console.error('全部标记已读失败:', error)
  }
}

watch(currentPage, () => {
  fetchNotifications()
})

onMounted(() => {
  fetchNotifications()
})
</script>
