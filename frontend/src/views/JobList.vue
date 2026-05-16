<template>
  <div>
    <!-- Search Header Bar -->
    <div class="p-6 bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 mb-6">
      <div class="flex flex-col md:flex-row gap-4">
        <div class="flex-1 flex items-center bg-slate-100 dark:bg-slate-800 rounded-xl px-4 py-2.5 border border-transparent focus-within:border-primary/50 transition-all">
          <span class="material-symbols-outlined text-slate-400 mr-2 text-xl">search</span>
          <div class="flex-1">
            <p class="text-[10px] text-slate-500 font-bold uppercase tracking-wider">职位标题、关键词</p>
            <input 
              v-model="searchForm.keyword"
              class="w-full bg-transparent border-none p-0 focus:ring-0 text-sm text-slate-900 dark:text-white placeholder:text-slate-400 font-medium" 
              placeholder="例如：前端工程师" 
              type="text"
            />
          </div>
        </div>
        <div class="flex-1 flex items-center bg-slate-100 dark:bg-slate-800 rounded-xl px-4 py-2.5 border border-transparent focus-within:border-primary/50 transition-all">
          <span class="material-symbols-outlined text-slate-400 mr-2 text-xl">location_on</span>
          <div class="flex-1">
            <p class="text-[10px] text-slate-500 font-bold uppercase tracking-wider">地点</p>
            <input 
              v-model="searchForm.location"
              class="w-full bg-transparent border-none p-0 focus:ring-0 text-sm text-slate-900 dark:text-white placeholder:text-slate-400 font-medium" 
              placeholder="例如：北京" 
              type="text"
            />
          </div>
        </div>
        <button 
          class="bg-primary hover:bg-primary/90 text-white px-8 rounded-xl font-bold transition-all shadow-lg shadow-primary/20 flex items-center gap-2"
          @click="handleSearch"
        >
          <span class="material-symbols-outlined text-xl">search</span>
          搜索
        </button>
      </div>
    </div>
    
    <div class="flex gap-6">
      <!-- Filters Sidebar -->
      <div class="w-72 shrink-0 hidden lg:block">
        <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 sticky top-24">
          <div class="flex items-center justify-between mb-6">
            <h3 class="font-bold text-slate-900 dark:text-white">筛选条件</h3>
            <button class="text-primary text-xs font-bold hover:underline" @click="resetFilters">清除全部</button>
          </div>
          
          <!-- Salary Filter -->
          <div class="mb-8">
            <p class="text-sm font-bold text-slate-800 dark:text-slate-200 mb-4">薪资范围</p>
            <div class="px-2 space-y-4">
              <div class="flex items-center gap-3">
                <div class="flex-1">
                  <label class="text-xs text-slate-500 mb-1 block">最低薪资</label>
                  <select 
                    v-model.number="salaryRange[0]" 
                    class="w-full px-3 py-2 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg text-sm"
                    @change="handleSearch"
                  >
                    <option v-for="s in [5, 8, 10, 12, 15, 18, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 100]" :key="s" :value="s">{{ s }}k</option>
                  </select>
                </div>
                <span class="text-slate-400 mt-5">-</span>
                <div class="flex-1">
                  <label class="text-xs text-slate-500 mb-1 block">最高薪资</label>
                  <select 
                    v-model.number="salaryRange[1]" 
                    class="w-full px-3 py-2 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg text-sm"
                    @change="handleSearch"
                  >
                    <option v-for="s in [10, 15, 18, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 100, 150]" :key="s" :value="s">{{ s }}k</option>
                  </select>
                </div>
              </div>
              <div class="text-center text-xs text-slate-500">
                当前筛选: {{ salaryRange[0] }}k - {{ salaryRange[1] }}k
              </div>
            </div>
          </div>
          
          <!-- Job Type Filter -->
          <div class="mb-8">
            <p class="text-sm font-bold text-slate-800 dark:text-slate-200 mb-4">工作类型</p>
            <div class="space-y-3">
              <label v-for="type in jobTypes" :key="type.value" class="flex items-center gap-3 cursor-pointer group">
                <input 
                  v-model="searchForm.jobTypes" 
                  :value="type.value"
                  class="rounded border-slate-300 text-primary focus:ring-primary" 
                  type="checkbox"
                />
                <span class="text-sm text-slate-600 dark:text-slate-400 group-hover:text-primary transition-colors">{{ type.label }}</span>
              </label>
            </div>
          </div>
          
          <!-- Experience Level -->
          <div class="mb-8">
            <p class="text-sm font-bold text-slate-800 dark:text-slate-200 mb-4">经验要求</p>
            <div class="space-y-3">
              <label v-for="exp in experiences" :key="exp.value" class="flex items-center gap-3 cursor-pointer group">
                <input 
                  v-model="searchForm.experience" 
                  :value="exp.value"
                  class="border-slate-300 text-primary focus:ring-primary" 
                  name="exp" 
                  type="radio"
                />
                <span class="text-sm text-slate-600 dark:text-slate-400">{{ exp.label }}</span>
              </label>
            </div>
          </div>
          
          <!-- Education -->
          <div class="mb-8">
            <p class="text-sm font-bold text-slate-800 dark:text-slate-200 mb-4">学历要求</p>
            <div class="space-y-3">
              <label v-for="edu in educations" :key="edu.value" class="flex items-center gap-3 cursor-pointer group">
                <input 
                  v-model="searchForm.education" 
                  :value="edu.value"
                  class="border-slate-300 text-primary focus:ring-primary" 
                  name="edu" 
                  type="radio"
                />
                <span class="text-sm text-slate-600 dark:text-slate-400">{{ edu.label }}</span>
              </label>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Job Listings -->
      <div class="flex-1 overflow-y-auto">
        <div class="flex items-center justify-between mb-4">
          <p class="text-sm text-slate-500">找到 <span class="text-slate-900 dark:text-white font-bold">{{ total }}</span> 个职位</p>
          <div class="flex items-center gap-2 text-sm text-slate-600 dark:text-slate-400">
            <span>排序方式:</span>
            <select v-model="searchForm.sortBy" class="bg-transparent border-none text-primary font-bold focus:ring-0 p-0 cursor-pointer" @change="handleSearch">
              <option value="publishTime">最新发布</option>
              <option value="salary">薪资最高</option>
            </select>
          </div>
        </div>
        
        <!-- Job Card 1 (Active) -->
        <div 
          v-for="job in jobs" 
          :key="job.id"
          class="group relative bg-white dark:bg-slate-900 p-5 rounded-2xl border-2 border-primary shadow-sm cursor-pointer transition-all mb-4 hover:border-primary/50"
           @click="$router.push(`/app/job/${job.id}`)"
        >
          <div class="flex gap-4">
            <div class="size-14 rounded-xl bg-slate-100 dark:bg-slate-800 flex items-center justify-center overflow-hidden shrink-0 border border-slate-100 dark:border-slate-700">
              <div class="w-10 h-10 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-lg flex items-center justify-center text-white text-sm font-bold">
                {{ job.company?.charAt(0) || 'B' }}
              </div>
            </div>
            <div class="flex-1">
              <div class="flex justify-between items-start">
                <div>
                  <h3 class="text-lg font-bold text-slate-900 dark:text-white group-hover:text-primary transition-colors tracking-tight">{{ job.title }}</h3>
                  <p class="text-sm text-slate-600 dark:text-slate-400 mt-0.5 font-medium">{{ job.enterpriseName || '公司名称' }}</p>
                </div>
                <span class="text-lg font-bold text-primary tabular-nums">{{ Math.round(job.salaryMin/1000) }}-{{ Math.round(job.salaryMax/1000) }}k</span>
              </div>
              <div class="mt-4 flex flex-wrap gap-2 items-center">
                <span class="px-3 py-1 bg-slate-100 dark:bg-slate-800 rounded-full text-[10px] font-bold text-slate-600 dark:text-slate-400 flex items-center gap-1 tracking-wide">
                  <span class="material-symbols-outlined text-xs">location_on</span> {{ job.location }}
                </span>
                <span class="px-3 py-1 bg-slate-100 dark:bg-slate-800 rounded-full text-[10px] font-bold text-slate-600 dark:text-slate-400 tracking-wide">{{ job.education }}</span>
                <span class="px-3 py-1 bg-slate-100 dark:bg-slate-800 rounded-full text-[10px] font-bold text-slate-600 dark:text-slate-400 tracking-wide">{{ job.experience }}</span>
                <span class="ml-auto text-[10px] text-slate-400 font-semibold tracking-wide">{{ formatTime(job.publishTime) }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <!-- Pagination -->
        <div class="flex justify-center mt-8">
          <div class="flex items-center gap-2">
            <button 
              class="p-2 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 hover:bg-primary hover:text-white transition-colors"
              :disabled="currentPage === 0"
              @click="handlePageChange(currentPage - 1)"
            >
              <span class="material-symbols-outlined">chevron_left</span>
            </button>
            <button 
              v-for="page in visiblePages" 
              :key="page"
              :class="[
                'w-10 h-10 rounded-lg font-bold transition-colors',
                page === currentPage + 1 
                  ? 'bg-primary text-white' 
                  : 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 hover:bg-primary hover:text-white'
              ]"
              @click="handlePageChange(page - 1)"
            >
              {{ page }}
            </button>
            <button 
              class="p-2 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 hover:bg-primary hover:text-white transition-colors"
              :disabled="currentPage >= totalPages - 1"
              @click="handlePageChange(currentPage + 1)"
            >
              <span class="material-symbols-outlined">chevron_right</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api'

const route = useRoute()
const router = useRouter()

const jobs = ref([])
const loading = ref(false)
const currentPage = ref(0)
const pageSize = ref(10)
const total = ref(0)

const searchForm = ref({
  keyword: route.query.keyword || '',
  location: '',
  jobTypes: [],
  experience: '',
  education: '',
  sortBy: 'publishTime'
})

const salaryRange = ref([5, 150])

const jobTypes = [
  { label: '全职', value: '全职' },
  { label: '兼职', value: '兼职' },
  { label: '实习', value: '实习' },
  { label: '远程', value: '远程' }
]

const experiences = [
  { label: '不限', value: '' },
  { label: '应届毕业生', value: '应届毕业生' },
  { label: '1年以下', value: '1年以下' },
  { label: '1-3年', value: '1-3年' },
  { label: '3-5年', value: '3-5年' },
  { label: '5-10年', value: '5-10年' },
  { label: '10年以上', value: '10年以上' }
]

const educations = [
  { label: '不限', value: '' },
  { label: '初中及以下', value: '初中及以下' },
  { label: '高中/中专', value: '高中/中专' },
  { label: '大专', value: '大专' },
  { label: '本科', value: '本科' },
  { label: '硕士', value: '硕士' },
  { label: '博士', value: '博士' }
]

const totalPages = computed(() => Math.ceil(total.value / pageSize.value))

const visiblePages = computed(() => {
  const pages = []
  const start = Math.max(1, currentPage.value - 1)
  const end = Math.min(totalPages.value, currentPage.value + 3)
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

const fetchJobs = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      sortBy: searchForm.value.sortBy,
      sortOrder: 'desc'
    }
    
    if (searchForm.value.keyword) {
      params.keyword = searchForm.value.keyword
    }
    if (searchForm.value.location) {
      params.location = searchForm.value.location
    }
    if (searchForm.value.experience) {
      params.experience = searchForm.value.experience
    }
    if (searchForm.value.education) {
      params.education = searchForm.value.education
    }
    if (searchForm.value.jobTypes && searchForm.value.jobTypes.length > 0) {
      params.jobType = searchForm.value.jobTypes.join(',')
    }
    params.salaryMin = salaryRange.value[0] * 1000
    params.salaryMax = salaryRange.value[1] * 1000
    
    const data = await api.filterJobs(params)
    jobs.value = data.content || []
    total.value = data.totalElements || 0
  } catch (error) {
    console.error('获取职位列表失败', error)
    jobs.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 监听筛选条件变化，自动搜索
watch([
  () => searchForm.value.experience,
  () => searchForm.value.education,
  () => searchForm.value.jobTypes,
  salaryRange
], () => {
  currentPage.value = 0
  fetchJobs()
}, { deep: true })

const handleSearch = () => {
  currentPage.value = 0
  fetchJobs()
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchJobs()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const resetFilters = () => {
  searchForm.value = {
    keyword: '',
    location: '',
    jobTypes: ['全职'],
    experience: '',
    education: '',
    sortBy: 'publishTime'
  }
  salaryRange.value = [15, 30]
  handleSearch()
}

const formatTime = (time) => {
  if (!time) return '刚刚'
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  
  if (days === 0) return '今天'
  if (days === 1) return '昨天'
  if (days < 7) return `${days}天前`
  return `${Math.floor(days / 7)}周前`
}

onMounted(() => {
  if (route.query.keyword) {
    searchForm.value.keyword = route.query.keyword
  }
  fetchJobs()
})
</script>
