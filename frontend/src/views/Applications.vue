<template>
  <div>
    <!-- Header -->
    <div class="p-6 border-b border-slate-200 dark:border-slate-800 bg-white dark:bg-slate-900/50 mb-6 rounded-t-2xl">
      <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 class="text-3xl font-black text-slate-900 dark:text-slate-100 tracking-tight">我的申请</h2>
          <p class="text-slate-500 dark:text-slate-400 mt-1">追踪您的求职进度</p>
        </div>
        <div class="flex items-center gap-3">
          <div class="flex items-center gap-2 bg-slate-100 dark:bg-slate-800 rounded-lg px-3 py-2">
            <span class="material-symbols-outlined text-slate-400 text-xl">search</span>
            <input 
              v-model="searchKeyword"
              class="bg-transparent border-none focus:ring-0 text-sm placeholder:text-slate-400" 
              placeholder="搜索申请..."
            />
          </div>
        </div>
      </div>
    </div>
    
    <!-- Filter Tabs -->
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
    
    <!-- Applications List -->
    <div class="space-y-4">
      <div v-if="loading" class="text-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-4 border-primary border-t-transparent mx-auto"></div>
        <p class="text-slate-500 dark:text-slate-400 mt-4">加载中...</p>
      </div>
      
      <template v-else>
        <div 
          v-for="app in filteredApplications" 
          :key="app.id"
          class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-5 hover:border-primary/50 transition-colors cursor-pointer"
          @click="goToJobDetail(app)"
        >
          <div class="flex gap-4">
            <div class="size-14 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white text-lg font-bold shrink-0">
              {{ (app.enterpriseName || app.company || 'B')?.charAt(0) }}
            </div>
            <div class="flex-1">
              <div class="flex justify-between items-start">
                <div>
                  <h3 class="font-bold text-slate-900 dark:text-white tracking-tight">{{ app.position || app.job?.title }}</h3>
                  <p class="text-sm text-slate-600 dark:text-slate-400 mt-0.5">{{ app.enterpriseName || app.company }}</p>
                </div>
                <span 
                  :class="[
                    'px-3 py-1 rounded-full text-xs font-bold',
                    getStatusClass(app.status)
                  ]"
                >
                  {{ getStatusText(app.status) }}
                </span>
              </div>
              <div class="mt-3 flex items-center gap-4 text-xs text-slate-500">
                <span class="flex items-center gap-1">
                  <span class="material-symbols-outlined text-xs">schedule</span>
                  {{ formatTime(app.applyTime || app.createdAt) }}
                </span>
                <span v-if="app.interviewTime" class="flex items-center gap-1 text-primary">
                  <span class="material-symbols-outlined text-xs">event</span>
                  面试: {{ formatTime(app.interviewTime) }}
                </span>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Empty State -->
        <div v-if="filteredApplications.length === 0" class="text-center py-12">
          <span class="material-symbols-outlined text-5xl text-slate-300 dark:text-slate-600 mb-4">inbox</span>
          <p class="text-slate-500 dark:text-slate-400">暂无申请记录</p>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'
import { useToast } from '@/composables/useToast'

const router = useRouter()
const toast = useToast()

const searchKeyword = ref('')
const activeTab = ref('')
const loading = ref(false)

const statusTabs = ref([
  { label: '全部', value: '', count: 0 },
  { label: '待处理', value: 'APPLIED', count: 0 },
  { label: '已查看', value: 'VIEWED', count: 0 },
  { label: '筛选中', value: 'SCREENING', count: 0 },
  { label: '面试中', value: 'INTERVIEW', count: 0 },
  { label: '已录用', value: 'OFFERED', count: 0 },
  { label: '已入职', value: 'HIRED', count: 0 },
  { label: '已拒绝', value: 'REJECTED', count: 0 }
])

const applications = ref([])

const fetchApplications = async () => {
  loading.value = true
  try {
    const data = await api.getMyApplications({ page: 0, size: 100 })
    applications.value = data.content || []
    updateCounts()
  } catch (error) {
    console.error('获取申请列表失败', error)
  } finally {
    loading.value = false
  }
}

const updateCounts = () => {
  const counts = { '': applications.value.length }
  statusTabs.value.forEach(tab => {
    if (tab.value) {
      counts[tab.value] = applications.value.filter(app => app.status === tab.value).length
    }
  })
  statusTabs.value.forEach(tab => {
    tab.count = counts[tab.value] || 0
  })
}

const filteredApplications = computed(() => {
  let result = applications.value
  
  if (activeTab.value) {
    result = result.filter(app => app.status === activeTab.value)
  }
  
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(app => 
      app.position?.toLowerCase().includes(keyword) || 
      app.company?.toLowerCase().includes(keyword) ||
      app.job?.title?.toLowerCase().includes(keyword) ||
      app.enterpriseName?.toLowerCase().includes(keyword)
    )
  }
  
  return result
})

const getStatusClass = (status) => {
  const classes = {
    'APPLIED': 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-400',
    'VIEWED': 'bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400',
    'SCREENING': 'bg-purple-100 text-purple-700 dark:bg-purple-900/30 dark:text-purple-400',
    'INTERVIEW': 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
    'OFFERED': 'bg-emerald-100 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-400',
    'HIRED': 'bg-cyan-100 text-cyan-700 dark:bg-cyan-900/30 dark:text-cyan-400',
    'REJECTED': 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400'
  }
  return classes[status] || 'bg-slate-100 text-slate-700'
}

const getStatusText = (status) => {
  const texts = {
    'APPLIED': '待处理',
    'VIEWED': '已查看',
    'SCREENING': '筛选中',
    'INTERVIEW': '面试中',
    'OFFERED': '已录用',
    'HIRED': '已入职',
    'REJECTED': '已拒绝'
  }
  return texts[status] || status
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleDateString('zh-CN')
}

const goToJobDetail = async (app) => {
  const jobId = app.jobId || app.job?.id
  if (!jobId) {
    toast.warning('职位信息不存在')
    return
  }
  
  try {
    const job = await api.getJobById(jobId)
    if (job && job.id) {
      router.push(`/app/job/${jobId}`)
    } else {
      toast.warning('该职位已下架')
    }
  } catch (error) {
    toast.warning('该职位已下架或不存在')
  }
}

onMounted(() => {
  fetchApplications()
})
</script>
