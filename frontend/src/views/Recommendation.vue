<template>
  <div>
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
      <div>
        <h2 class="text-3xl font-black text-slate-900 dark:text-slate-100 tracking-tight">智能推荐</h2>
        <p class="text-slate-500 dark:text-slate-400 mt-1">基于AI算法，为您推荐最适合的岗位</p>
      </div>
    </div>
    
    <!-- Recommended Jobs -->
    <div v-if="loading" class="text-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-4 border-primary border-t-transparent mx-auto"></div>
      <p class="text-slate-500 dark:text-slate-400 mt-4">加载中...</p>
    </div>
    
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <div 
        v-for="job in recommendedJobs" 
        :key="job.id"
        class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-5 hover:border-primary/50 transition-all cursor-pointer"
        @click="$router.push(`/app/job/${job.id}`)"
      >
        <div class="flex justify-between items-start mb-3">
          <div class="flex gap-3">
            <div class="w-12 h-12 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white font-bold">
              {{ (job.enterpriseName || job.company || 'B')?.charAt(0) }}
            </div>
            <div>
              <h4 class="font-bold text-slate-900 dark:text-white tracking-tight">{{ job.title }}</h4>
              <p class="text-sm text-slate-600 dark:text-slate-400 mt-0.5">{{ job.enterpriseName || job.company }}</p>
            </div>
          </div>
          <span v-if="job.match" class="px-2 py-1 bg-green-100 text-green-700 text-xs font-bold rounded-full">匹配度 {{ job.match }}%</span>
        </div>
        
        <p class="text-xl font-bold text-primary mb-3 tabular-nums">{{ Math.round(job.salaryMin/1000) }}-{{ Math.round(job.salaryMax/1000) }}k</p>
        
        <div class="flex flex-wrap gap-2 mb-4">
          <span class="px-3 py-1 bg-slate-100 dark:bg-slate-800 rounded-full text-xs font-bold text-slate-600 dark:text-slate-400 flex items-center gap-1 tracking-wide">
            <span class="material-symbols-outlined text-xs">location_on</span> {{ job.location }}
          </span>
          <span class="px-3 py-1 bg-slate-100 dark:bg-slate-800 rounded-full text-xs font-bold text-slate-600 dark:text-slate-400 tracking-wide">{{ job.workType || '全职' }}</span>
        </div>
        
        <button 
          class="w-full py-2 bg-primary/10 text-primary rounded-xl font-bold text-sm hover:bg-primary hover:text-white transition-colors tracking-wide"
          @click.stop="handleApply(job)"
        >
          投递简历
        </button>
      </div>
    </div>
    
    <!-- Empty State -->
    <div v-if="!loading && recommendedJobs.length === 0" class="text-center py-12">
      <span class="material-symbols-outlined text-5xl text-slate-300 dark:text-slate-600 mb-4">recommend</span>
      <p class="text-slate-500 dark:text-slate-400 mb-4">暂无推荐职位</p>
      <button class="px-6 py-2 bg-primary text-white rounded-xl font-bold text-sm" @click="$router.push('/app/jobs')">
        浏览更多职位
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import api from '@/api'

const router = useRouter()
const userStore = useUserStore()

const recommendedJobs = ref([])
const loading = ref(false)

const fetchRecommendations = async () => {
  loading.value = true
  try {
    const data = await api.getRecommendedJobs(20)
    recommendedJobs.value = data || []
  } catch (error) {
    console.error('获取推荐职位失败', error)
  } finally {
    loading.value = false
  }
}

const handleApply = async (job) => {
  if (!userStore.isLoggedIn) {
    router.push({ path: '/login', query: { redirect: '/recommendation' } })
    return
  }
  
  try {
    await api.applyForJob({ jobId: job.id })
    alert('简历投递成功')
  } catch (error) {
    console.error('投递失败', error)
  }
}

onMounted(() => {
  fetchRecommendations()
})
</script>
