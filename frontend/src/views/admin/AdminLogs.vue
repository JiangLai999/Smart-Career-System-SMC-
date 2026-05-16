<template>
  <div>
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6">
      <div>
        <h1 class="text-3xl font-black text-slate-900 dark:text-white">系统日志</h1>
        <p class="text-slate-500 dark:text-slate-400">查看系统操作记录</p>
      </div>
      <div class="flex gap-3">
        <input 
          v-model="searchKeyword"
          class="px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-sm"
          placeholder="搜索用户名..."
          @keyup.enter="fetchLogs"
        />
        <select v-model="operationType" class="px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-900 text-sm" @change="fetchLogs">
          <option value="">全部操作</option>
          <option value="LOGIN">登录</option>
          <option value="REGISTER">注册</option>
          <option value="JOB_POST">发布职位</option>
          <option value="JOB_APPLY">投递简历</option>
        </select>
      </div>
    </div>
    
    <!-- Logs Table -->
    <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 overflow-hidden">
      <table class="w-full">
        <thead class="bg-slate-50 dark:bg-slate-800">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase">时间</th>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase">用户</th>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase">类型</th>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase">操作详情</th>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase">IP地址</th>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 dark:text-slate-400 uppercase">状态</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100 dark:divide-slate-800">
          <tr v-for="log in logs" :key="log.id" class="hover:bg-slate-50 dark:hover:bg-slate-800">
            <td class="px-6 py-4 text-sm text-slate-600 dark:text-slate-400 whitespace-nowrap">
              {{ formatTime(log.operationTime) }}
            </td>
            <td class="px-6 py-4">
              <div>
                <p class="font-medium text-slate-900 dark:text-white">{{ log.username || '-' }}</p>
                <p class="text-xs text-slate-500">{{ log.userType }}</p>
              </div>
            </td>
            <td class="px-6 py-4">
              <span :class="getOperationTypeClass(log.operationType)" class="px-2 py-1 rounded-full text-xs font-bold">
                {{ getOperationTypeText(log.operationType) }}
              </span>
            </td>
            <td class="px-6 py-4 text-sm text-slate-600 dark:text-slate-400 max-w-xs truncate">
              {{ log.operationDetail || '-' }}
            </td>
            <td class="px-6 py-4 text-sm text-slate-600 dark:text-slate-400 font-mono">
              {{ log.ipAddress || '-' }}
            </td>
            <td class="px-6 py-4">
              <span v-if="log.responseStatus === 200 || log.responseStatus === null" class="text-green-500 flex items-center gap-1">
                <span class="material-symbols-outlined text-sm">check_circle</span> 成功
              </span>
              <span v-else class="text-red-500 flex items-center gap-1">
                <span class="material-symbols-outlined text-sm">error</span> 失败
              </span>
            </td>
          </tr>
        </tbody>
      </table>
      
      <!-- Empty State -->
      <div v-if="logs.length === 0" class="text-center py-12">
        <span class="material-symbols-outlined text-5xl text-slate-300 dark:text-slate-600 mb-4">article</span>
        <p class="text-slate-500 dark:text-slate-400">暂无日志记录</p>
      </div>
    </div>
    
    <!-- Pagination -->
    <div class="flex justify-center mt-6">
      <div class="flex items-center gap-2">
        <button 
          class="p-2 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400"
          :disabled="currentPage === 0"
          @click="changePage(currentPage - 1)"
        >
          <span class="material-symbols-outlined">chevron_left</span>
        </button>
        <span class="px-4 text-sm text-slate-600 dark:text-slate-400">
          第 {{ currentPage + 1 }} 页
        </span>
        <button 
          class="p-2 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400"
          :disabled="logs.length < pageSize"
          @click="changePage(currentPage + 1)"
        >
          <span class="material-symbols-outlined">chevron_right</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const logs = ref([])
const currentPage = ref(0)
const pageSize = ref(20)
const searchKeyword = ref('')
const operationType = ref('')

const fetchLogs = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      username: searchKeyword.value,
      operationType: operationType.value
    }
    const response = await api.getAdminLogs(params)
    const data = response?.data?.content || response?.content || []
    if (data) {
      logs.value = data
    }
  } catch (error) {
    console.error('获取日志失败', error)
  }
}

const changePage = (page) => {
  currentPage.value = page
  fetchLogs()
}

const getOperationTypeClass = (type) => {
  const classes = {
    'LOGIN': 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-400',
    'REGISTER': 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
    'JOB_POST': 'bg-purple-100 text-purple-700 dark:bg-purple-900/30 dark:text-purple-400',
    'JOB_APPLY': 'bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400'
  }
  return classes[type] || 'bg-slate-100 text-slate-700'
}

const getOperationTypeText = (type) => {
  const texts = {
    'LOGIN': '登录',
    'REGISTER': '注册',
    'JOB_POST': '发布职位',
    'JOB_APPLY': '投递简历'
  }
  return texts[type] || type || '其他'
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString()
}

onMounted(() => {
  fetchLogs()
})
</script>