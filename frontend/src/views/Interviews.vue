<template>
  <div>
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
      <div>
        <h2 class="text-3xl font-black text-slate-900 dark:text-slate-100 tracking-tight">面试管理</h2>
        <p class="text-slate-500 dark:text-slate-400 mt-1">查看和管理您的面试邀请</p>
      </div>
    </div>

    <div class="flex gap-2 mb-6 overflow-x-auto pb-2">
      <button 
        v-for="tab in statusTabs" 
        :key="tab.value"
        :class="[
          'px-4 py-2 rounded-lg font-medium text-sm whitespace-nowrap transition-colors',
          activeTab === tab.value 
            ? 'bg-primary text-white' 
            : 'bg-white dark:bg-slate-900 text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'
        ]"
        @click="activeTab = tab.value"
      >
        {{ tab.label }}
        <span class="ml-1 text-xs opacity-70">({{ tab.count }})</span>
      </button>
    </div>

    <div v-if="loading" class="text-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-4 border-primary border-t-transparent mx-auto"></div>
      <p class="text-slate-500 dark:text-slate-400 mt-4">加载中...</p>
    </div>

    <div v-else-if="filteredInterviews.length === 0" class="text-center py-12">
      <span class="material-symbols-outlined text-6xl text-slate-300 dark:text-slate-600">event_busy</span>
      <p class="text-slate-500 dark:text-slate-400 mt-2">暂无面试安排</p>
    </div>

    <div v-else class="space-y-4">
      <div 
        v-for="interview in filteredInterviews" 
        :key="interview.id"
        class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-5 hover:border-primary/50 transition-colors"
      >
        <div class="flex flex-col md:flex-row md:items-start justify-between gap-4">
          <div class="flex gap-4">
            <div class="w-12 h-12 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white font-bold shrink-0">
              {{ (interview.companyName || interview.enterpriseName || 'B')?.charAt(0) }}
            </div>
            <div>
              <h3 class="font-bold text-slate-900 dark:text-white">{{ interview.position || interview.jobTitle }}</h3>
              <p class="text-sm text-slate-600 dark:text-slate-400 mt-0.5">{{ interview.companyName || interview.enterpriseName }}</p>
              <div class="flex flex-wrap gap-3 mt-3 text-sm text-slate-500">
                <span class="flex items-center gap-1">
                  <span class="material-symbols-outlined text-base">event</span>
                  {{ formatDate(interview.interviewTime) }}
                </span>
                <span class="flex items-center gap-1">
                  <span class="material-symbols-outlined text-base">schedule</span>
                  {{ formatTime(interview.interviewTime) }}
                </span>
                <span v-if="interview.interviewType" class="flex items-center gap-1">
                  <span class="material-symbols-outlined text-base">videocam</span>
                  {{ interview.interviewType }}
                </span>
              </div>
            </div>
          </div>
          
          <div class="flex flex-col items-end gap-2">
            <span :class="['px-3 py-1 rounded-full text-xs font-bold', getStatusClass(interview.status)]">
              {{ getStatusText(interview.status) }}
            </span>
            <div class="flex gap-2">
              <button 
                v-if="interview.status === 'PENDING'"
                class="px-3 py-1.5 bg-emerald-500 text-white rounded-lg text-sm font-medium hover:bg-emerald-600 transition-colors"
                @click="handleConfirm(interview)"
              >
                确认参加
              </button>
              <button 
                v-if="interview.status === 'PENDING'"
                class="px-3 py-1.5 bg-red-100 text-red-600 rounded-lg text-sm font-medium hover:bg-red-200 transition-colors"
                @click="handleReject(interview)"
              >
                拒绝
              </button>
              <button 
                class="px-3 py-1.5 bg-slate-100 text-slate-600 rounded-lg text-sm font-medium hover:bg-slate-200 transition-colors"
                @click="viewDetail(interview)"
              >
                查看详情
              </button>
            </div>
          </div>
        </div>
        
        <div v-if="interview.location || interview.interviewLink" class="mt-4 p-3 bg-slate-50 dark:bg-slate-800 rounded-xl">
          <p v-if="interview.location" class="text-sm text-slate-600 dark:text-slate-400">
            <span class="font-medium">地点：</span>{{ interview.location }}
          </p>
          <p v-if="interview.interviewLink" class="text-sm text-slate-600 dark:text-slate-400 mt-1">
            <span class="font-medium">链接：</span>
            <a :href="interview.interviewLink" target="_blank" class="text-primary hover:underline">{{ interview.interviewLink }}</a>
          </p>
        </div>
        
        <p v-if="interview.notes" class="mt-3 text-sm text-slate-500 dark:text-slate-400">
          <span class="font-medium">备注：</span>{{ interview.notes }}
        </p>
      </div>
    </div>

    <div v-if="showRejectModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-white dark:bg-slate-900 rounded-2xl w-full max-w-md p-6">
        <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-4">拒绝面试</h3>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">拒绝原因</label>
            <textarea 
              v-model="rejectReason" 
              class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-2 focus:ring-primary text-sm"
              rows="3"
              placeholder="请输入拒绝原因（可选）"
            ></textarea>
          </div>
        </div>
        <div class="flex gap-3 mt-6">
          <button 
            class="flex-1 py-2 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 rounded-xl font-bold"
            @click="showRejectModal = false"
          >
            取消
          </button>
          <button 
            class="flex-1 py-2 bg-red-500 text-white rounded-xl font-bold hover:bg-red-600"
            @click="confirmReject"
          >
            确认拒绝
          </button>
        </div>
      </div>
    </div>

    <div v-if="showDetailModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-white dark:bg-slate-900 rounded-2xl w-full max-w-lg p-6">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-bold text-slate-900 dark:text-white">面试详情</h3>
          <button class="text-slate-400 hover:text-slate-600" @click="showDetailModal = false">
            <span class="material-symbols-outlined">close</span>
          </button>
        </div>
        
        <div v-if="selectedInterview" class="space-y-4">
          <div class="flex items-center gap-3">
            <div class="w-12 h-12 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white font-bold">
              {{ (selectedInterview.companyName || selectedInterview.enterpriseName || 'B')?.charAt(0) }}
            </div>
            <div>
              <h4 class="font-bold text-slate-900 dark:text-white">{{ selectedInterview.position || selectedInterview.jobTitle }}</h4>
              <p class="text-sm text-slate-600 dark:text-slate-400">{{ selectedInterview.companyName || selectedInterview.enterpriseName }}</p>
            </div>
          </div>
          
          <div class="grid grid-cols-2 gap-4">
            <div class="p-3 bg-slate-50 dark:bg-slate-800 rounded-xl">
              <p class="text-xs text-slate-500 mb-1">面试时间</p>
              <p class="font-medium text-slate-900 dark:text-white">{{ formatDate(selectedInterview.interviewTime) }}</p>
              <p class="text-sm text-slate-600 dark:text-slate-400">{{ formatTime(selectedInterview.interviewTime) }}</p>
            </div>
            <div class="p-3 bg-slate-50 dark:bg-slate-800 rounded-xl">
              <p class="text-xs text-slate-500 mb-1">面试状态</p>
              <span :class="['px-2 py-1 rounded-full text-xs font-bold', getStatusClass(selectedInterview.status)]">
                {{ getStatusText(selectedInterview.status) }}
              </span>
            </div>
          </div>
          
          <div v-if="selectedInterview.location" class="p-3 bg-slate-50 dark:bg-slate-800 rounded-xl">
            <p class="text-xs text-slate-500 mb-1">面试地点</p>
            <p class="font-medium text-slate-900 dark:text-white">{{ selectedInterview.location }}</p>
          </div>
          
          <div v-if="selectedInterview.interviewLink" class="p-3 bg-slate-50 dark:bg-slate-800 rounded-xl">
            <p class="text-xs text-slate-500 mb-1">面试链接</p>
            <a :href="selectedInterview.interviewLink" target="_blank" class="text-primary hover:underline break-all">{{ selectedInterview.interviewLink }}</a>
          </div>
          
          <div v-if="selectedInterview.notes" class="p-3 bg-slate-50 dark:bg-slate-800 rounded-xl">
            <p class="text-xs text-slate-500 mb-1">备注</p>
            <p class="text-slate-700 dark:text-slate-300">{{ selectedInterview.notes }}</p>
          </div>
        </div>
        
        <div class="flex gap-3 mt-6">
          <button 
            class="flex-1 py-2 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 rounded-xl font-bold"
            @click="showDetailModal = false"
          >
            关闭
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '@/api'
import { useToast } from '@/composables/useToast'

const toast = useToast()

const loading = ref(false)
const interviews = ref([])
const activeTab = ref('')
const showRejectModal = ref(false)
const showDetailModal = ref(false)
const selectedInterview = ref(null)
const rejectReason = ref('')

const statusTabs = ref([
  { label: '全部', value: '', count: 0 },
  { label: '待确认', value: 'PENDING', count: 0 },
  { label: '已确认', value: 'CONFIRMED', count: 0 },
  { label: '已完成', value: 'COMPLETED', count: 0 },
  { label: '已拒绝', value: 'REJECTED', count: 0 },
  { label: '已取消', value: 'CANCELLED', count: 0 }
])

const filteredInterviews = computed(() => {
  if (!activeTab.value) return interviews.value
  return interviews.value.filter(i => i.status === activeTab.value)
})

const fetchInterviews = async () => {
  loading.value = true
  try {
    const data = await api.getJobSeekerInterviews({ page: 0, size: 100 })
    interviews.value = data.content || data || []
    updateCounts()
  } catch (error) {
    console.error('获取面试列表失败', error)
    toast.error('获取面试列表失败，请刷新页面重试')
    interviews.value = []
  } finally {
    loading.value = false
  }
}

const updateCounts = () => {
  const counts = { '': interviews.value.length }
  statusTabs.value.forEach(tab => {
    if (tab.value) {
      counts[tab.value] = interviews.value.filter(i => i.status === tab.value).length
    }
  })
  statusTabs.value.forEach(tab => {
    tab.count = counts[tab.value] || 0
  })
}

const getStatusClass = (status) => {
  const classes = {
    'PENDING': 'bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400',
    'CONFIRMED': 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-400',
    'COMPLETED': 'bg-emerald-100 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-400',
    'REJECTED': 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400',
    'CANCELLED': 'bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-400'
  }
  return classes[status] || 'bg-slate-100 text-slate-700'
}

const getStatusText = (status) => {
  const texts = {
    'PENDING': '待确认',
    'CONFIRMED': '已确认',
    'COMPLETED': '已完成',
    'REJECTED': '已拒绝',
    'CANCELLED': '已取消'
  }
  return texts[status] || status
}

const formatDate = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

const handleConfirm = async (interview) => {
  try {
    await api.confirmInterview(interview.id)
    toast.success('已确认参加面试')
    interview.status = 'CONFIRMED'
    updateCounts()
  } catch (error) {
    console.error('确认面试失败', error)
    toast.error('确认失败，请稍后重试')
  }
}

const handleReject = (interview) => {
  selectedInterview.value = interview
  rejectReason.value = ''
  showRejectModal.value = true
}

const confirmReject = async () => {
  if (!selectedInterview.value) return
  
  try {
    await api.rejectInterview(selectedInterview.value.id, rejectReason.value)
    toast.success('已拒绝面试邀请')
    selectedInterview.value.status = 'REJECTED'
    showRejectModal.value = false
    updateCounts()
  } catch (error) {
    console.error('拒绝面试失败', error)
    toast.error('操作失败，请稍后重试')
  }
}

const viewDetail = (interview) => {
  selectedInterview.value = interview
  showDetailModal.value = true
}

onMounted(() => {
  fetchInterviews()
})
</script>

<style scoped>
.material-symbols-outlined {
  font-variation-settings: 'FILL' 0, 'wght' 400, 'GRAD' 0, 'opsz' 24;
}
</style>
