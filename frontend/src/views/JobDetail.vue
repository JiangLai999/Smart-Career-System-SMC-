<template>
  <div>
    <div v-if="job" class="flex flex-col lg:flex-row gap-6">
      <!-- Main Content -->
      <div class="flex-1">
        <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 md:p-8">
          <!-- Header -->
          <div class="flex items-start gap-4 mb-6">
            <div class="size-16 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white text-xl font-bold shrink-0">
              {{ job.enterpriseName?.charAt(0) || 'B' }}
            </div>
            <div class="flex-1">
              <div class="flex justify-between items-start">
                <div>
                  <h1 class="text-2xl font-bold text-slate-900 dark:text-white mb-2">{{ job.title }}</h1>
                  <p class="text-slate-600 dark:text-slate-400">{{ job.enterpriseName }}</p>
                </div>
                <div class="text-right">
                  <p class="text-2xl font-bold text-primary">{{ Math.round(job.salaryMin/1000) }}-{{ Math.round(job.salaryMax/1000) }}k</p>
                  <p class="text-sm text-slate-500">/{{ job.workType || '月' }}</p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Tags -->
          <div class="flex flex-wrap gap-3 mb-8">
            <span class="px-4 py-2 bg-slate-100 dark:bg-slate-800 rounded-full text-sm font-medium text-slate-700 dark:text-slate-300 flex items-center gap-1">
              <span class="material-symbols-outlined text-base">location_on</span> {{ job.location }}
            </span>
            <span class="px-4 py-2 bg-slate-100 dark:bg-slate-800 rounded-full text-sm font-medium text-slate-700 dark:text-slate-300">{{ job.education }}</span>
            <span class="px-4 py-2 bg-slate-100 dark:bg-slate-800 rounded-full text-sm font-medium text-slate-700 dark:text-slate-300">{{ job.experience }}</span>
            <span class="px-4 py-2 bg-slate-100 dark:bg-slate-800 rounded-full text-sm font-medium text-slate-700 dark:text-slate-300">{{ job.workType }}</span>
          </div>
          
          <!-- Description -->
          <div class="mb-8">
            <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-4">职位描述</h3>
            <div class="prose prose-sm max-w-none text-slate-600 dark:text-slate-400 whitespace-pre-line">
              {{ job.description }}
            </div>
          </div>
          
          <!-- Requirements -->
          <div class="mb-8">
            <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-4">任职要求</h3>
            <div class="prose prose-sm max-w-none text-slate-600 dark:text-slate-400 whitespace-pre-line">
              {{ job.requirement }}
            </div>
          </div>
          
          <!-- Responsibilities -->
          <div class="mb-8" v-if="job.responsibility">
            <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-4">岗位职责</h3>
            <div class="prose prose-sm max-w-none text-slate-600 dark:text-slate-400 whitespace-pre-line">
              {{ job.responsibility }}
            </div>
          </div>
          
          <!-- Welfare -->
          <div v-if="job.welfare">
            <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-4">福利待遇</h3>
            <div class="flex flex-wrap gap-2">
              <span 
                v-for="(welfare, index) in parseWelfare(job.welfare)" 
                :key="index"
                class="px-4 py-2 bg-primary/10 text-primary rounded-full text-sm font-medium"
              >
                {{ welfare }}
              </span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Sidebar -->
      <div class="w-full lg:w-80 shrink-0">
        <!-- Apply Card -->
        <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 sticky top-24">
          <button 
            v-if="!hasApplied"
            class="w-full bg-primary hover:bg-primary/90 text-white py-4 rounded-xl font-bold text-lg transition-all shadow-xl shadow-primary/20 flex items-center justify-center gap-2"
            :disabled="applying"
            @click="handleApply"
          >
            <span class="material-symbols-outlined">send</span>
            {{ applying ? '投递中...' : '立即申请' }}
          </button>
          <div v-else class="w-full py-4 rounded-xl font-bold text-lg flex items-center justify-center gap-2" :class="getStatusClass(applicationStatus)">
            <span class="material-symbols-outlined">{{ getStatusIcon(applicationStatus) }}</span>
            {{ getStatusText(applicationStatus) }}
          </div>
          <p class="text-center text-[10px] text-slate-400 mt-3">通常在 24 小时内回复</p>
          
          <!-- Company Info -->
          <div class="mt-8 pt-6 border-t border-slate-200 dark:border-slate-800">
            <h4 class="font-bold text-slate-900 dark:text-white mb-4">公司信息</h4>
            <div class="space-y-3 text-sm">
              <div class="flex items-center gap-2 text-slate-600 dark:text-slate-400">
                <span class="material-symbols-outlined text-base">business</span>
                <span>{{ job.enterpriseName }}</span>
              </div>
              <div class="flex items-center gap-2 text-slate-600 dark:text-slate-400">
                <span class="material-symbols-outlined text-base">location_on</span>
                <span>北京</span>
              </div>
              <div class="flex items-center gap-2 text-slate-600 dark:text-slate-400">
                <span class="material-symbols-outlined text-base">groups</span>
                <span>1000-9999人</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Loading -->
    <div v-else class="flex items-center justify-center py-20">
      <div class="animate-spin rounded-full h-12 w-12 border-4 border-primary border-t-transparent"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import api from '@/api'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const job = ref(null)
const applying = ref(false)
const hasApplied = ref(false)
const applicationStatus = ref('')

const isLoggedIn = computed(() => userStore.isLoggedIn)

const statusTextMap = {
  'APPLIED': '待处理',
  'VIEWED': '已查看',
  'SCREENING': '筛选中',
  'INTERVIEW': '面试中',
  'OFFERED': '已录用',
  'HIRED': '已入职',
  'REJECTED': '已拒绝'
}

const statusClassMap = {
  'APPLIED': 'bg-blue-100 text-blue-700 border-blue-200',
  'VIEWED': 'bg-amber-100 text-amber-700 border-amber-200',
  'SCREENING': 'bg-purple-100 text-purple-700 border-purple-200',
  'INTERVIEW': 'bg-green-100 text-green-700 border-green-200',
  'OFFERED': 'bg-emerald-100 text-emerald-700 border-emerald-200',
  'HIRED': 'bg-cyan-100 text-cyan-700 border-cyan-200',
  'REJECTED': 'bg-red-100 text-red-700 border-red-200'
}

const getStatusText = (status) => statusTextMap[status] || status || '已投递'
const getStatusClass = (status) => statusClassMap[status] || 'bg-emerald-100 text-emerald-700 border-emerald-200'
const getStatusIcon = (status) => {
  const icons = {
    'APPLIED': 'hourglass_empty',
    'VIEWED': 'visibility',
    'SCREENING': 'filter_list',
    'INTERVIEW': 'event',
    'OFFERED': 'celebration',
    'HIRED': 'work',
    'REJECTED': 'cancel'
  }
  return icons[status] || 'check_circle'
}

const checkApplicationStatus = async () => {
  if (!isLoggedIn.value || !job.value?.id) return
  
  try {
    const result = await api.checkApplicationStatus(job.value.id)
    hasApplied.value = result && (result.hasApplied !== false)
    applicationStatus.value = result?.status || (hasApplied.value ? 'APPLIED' : '')
  } catch (error) {
    console.error('检查申请状态失败:', error)
  }
}

const fetchJobDetail = async () => {
  try {
    job.value = await api.getJobById(route.params.id)
    await checkApplicationStatus()
  } catch (error) {
    console.error('获取职位详情失败', error)
  }
}

const handleApply = async () => {
  if (!isLoggedIn.value) {
    router.push({ path: '/login', query: { redirect: route.fullPath } })
    return
  }
  
  if (!job.value || !job.value.id) {
    alert('职位信息加载失败，请刷新页面重试')
    return
  }
  
  applying.value = true
  try {
    await api.applyForJob({ jobId: job.value.id })
    alert('简历投递成功！')
    hasApplied.value = true
  } catch (error) {
    console.error('投递简历失败', error)
    
    let errorMessage = '投递失败，请稍后重试'
    
    if (error.response) {
      const status = error.response.status
      const data = error.response.data
      
      if (status === 400) {
        errorMessage = data?.message || '请求参数错误'
      } else if (status === 401) {
        errorMessage = '请先登录'
        router.push({ path: '/login', query: { redirect: route.fullPath } })
        return
      } else if (status === 409 || (data?.message && data.message.includes('已申请'))) {
        errorMessage = '您已经投递过该职位了'
        hasApplied.value = true
      } else if (status === 500) {
        errorMessage = '服务器暂忙，请稍后重试'
      } else {
        errorMessage = data?.message || `投递失败 (${status})`
      }
    } else if (error.message) {
      errorMessage = error.message
    }
    
    alert(errorMessage)
  } finally {
    applying.value = false
  }
}

const parseWelfare = (welfare) => {
  if (!welfare) return []
  return welfare.split(/[,，、]/).filter(w => w.trim())
}

onMounted(() => {
  fetchJobDetail()
})
</script>
