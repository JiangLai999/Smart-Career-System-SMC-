<template>
  <div class="relative" ref="dropdownRef">
    <button
      @click="toggleDropdown"
      class="relative flex items-center justify-center rounded-full h-10 w-10 bg-slate-800 text-slate-300 hover:text-white transition-colors"
    >
      <span class="material-symbols-outlined text-xl">notifications</span>
      <span
        v-if="unreadCount > 0"
        class="absolute -top-1 -right-1 flex h-5 w-5 items-center justify-center rounded-full bg-red-500 text-white text-xs font-bold"
      >
        {{ unreadCount > 99 ? '99+' : unreadCount }}
      </span>
    </button>

    <div
      v-if="isOpen"
      class="absolute right-0 mt-2 w-96 bg-white dark:bg-slate-800 rounded-xl shadow-2xl border border-slate-200 dark:border-slate-700 z-50 overflow-hidden"
    >
      <div class="flex items-center justify-between p-4 border-b border-slate-200 dark:border-slate-700">
        <h3 class="text-lg font-bold text-slate-900 dark:text-white">通知中心</h3>
        <button
          v-if="unreadCount > 0"
          @click="markAllAsRead"
          class="text-sm text-primary hover:text-primary/80 font-medium"
        >
          全部已读
        </button>
      </div>

      <div class="max-h-96 overflow-y-auto">
        <div v-if="loading" class="p-8 text-center">
          <span class="material-symbols-outlined animate-spin text-4xl text-primary">progress_activity</span>
          <p class="text-slate-500 dark:text-slate-400 mt-2">加载中...</p>
        </div>

        <div v-else-if="notifications.length === 0" class="p-8 text-center">
          <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">notifications_off</span>
          <p class="text-slate-500 dark:text-slate-400 mt-2">暂无通知</p>
        </div>

        <div v-else>
          <div
            v-for="notification in notifications"
            :key="notification.id"
            @click="handleNotificationClick(notification)"
            :class="[
              'p-4 border-b border-slate-100 dark:border-slate-700 cursor-pointer transition-colors hover:bg-slate-50 dark:hover:bg-slate-700',
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
                  <h4 class="text-sm font-semibold text-slate-900 dark:text-white truncate">
                    {{ notification.title }}
                  </h4>
                  <span class="text-xs text-slate-500 dark:text-slate-400 whitespace-nowrap">
                    {{ formatTime(notification.createTime) }}
                  </span>
                </div>
                <p class="text-sm text-slate-600 dark:text-slate-400 mt-1 line-clamp-2">
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
      </div>

      <div class="p-3 border-t border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-900">
        <button
          @click="viewAllNotifications"
          class="w-full text-center text-sm font-medium text-primary hover:text-primary/80"
        >
          查看全部通知
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'

const router = useRouter()
const dropdownRef = ref(null)
const isOpen = ref(false)
const loading = ref(false)
const notifications = ref([])
const unreadCount = ref(0)

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

const toggleDropdown = () => {
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    fetchNotifications()
  }
}

const fetchNotifications = async () => {
  loading.value = true
  try {
    const data = await api.getUnreadNotifications()
    notifications.value = data.slice(0, 10)
  } catch (error) {
    console.error('获取通知失败:', error)
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

  isOpen.value = false
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

const viewAllNotifications = () => {
  router.push('/app/notifications')
  isOpen.value = false
}

const handleClickOutside = (event) => {
  if (dropdownRef.value && !dropdownRef.value.contains(event.target)) {
    isOpen.value = false
  }
}

let refreshInterval = null

onMounted(() => {
  fetchUnreadCount()
  document.addEventListener('click', handleClickOutside)
  refreshInterval = setInterval(fetchUnreadCount, 30000)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
  if (refreshInterval) {
    clearInterval(refreshInterval)
  }
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>