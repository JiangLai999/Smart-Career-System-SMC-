<template>
  <div>
    <!-- Header Section -->
    <header class="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
      <div>
        <h2 class="text-3xl font-black tracking-tight text-slate-900 dark:text-white">职位管理</h2>
        <p class="text-slate-500 dark:text-slate-400 mt-1">管理您企业中所有的招聘岗位和应聘流程</p>
      </div>
      <router-link to="/company/jobs/create" class="bg-primary hover:bg-primary/90 text-white px-6 py-2.5 rounded-lg font-bold text-sm flex items-center gap-2 transition-all shadow-lg shadow-primary/20">
        <span class="material-symbols-outlined text-lg">add</span>
        发布新职位
      </router-link>
    </header>
    
    <!-- Stats Overview -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
      <div class="bg-white dark:bg-slate-900 p-4 rounded-xl border border-slate-200 dark:border-slate-800">
        <p class="text-xs text-slate-500 font-bold uppercase tracking-wider mb-1">正在招聘</p>
        <p class="text-2xl font-black text-slate-900 dark:text-white">{{ stats.active }} <span class="text-sm font-normal text-green-500">+{{ stats.newThisWeek }}</span></p>
      </div>
      <div class="bg-white dark:bg-slate-900 p-4 rounded-xl border border-slate-200 dark:border-slate-800">
        <p class="text-xs text-slate-500 font-bold uppercase tracking-wider mb-1">收到简历</p>
        <p class="text-2xl font-black text-slate-900 dark:text-white">{{ stats.resumes }}</p>
      </div>
      <div class="bg-white dark:bg-slate-900 p-4 rounded-xl border border-slate-200 dark:border-slate-800">
        <p class="text-xs text-slate-500 font-bold uppercase tracking-wider mb-1">待面试</p>
        <p class="text-2xl font-black text-slate-900 dark:text-white">{{ stats.interviews }}</p>
      </div>
      <div class="bg-white dark:bg-slate-900 p-4 rounded-xl border border-slate-200 dark:border-slate-800">
        <p class="text-xs text-slate-500 font-bold uppercase tracking-wider mb-1">已过期</p>
        <p class="text-2xl font-black text-slate-900 dark:text-white">{{ stats.expired }}</p>
      </div>
    </div>
    
    <!-- Search and Filter Bar -->
    <div class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-4 mb-6">
      <div class="flex flex-col md:flex-row gap-4">
        <!-- Search Input -->
        <div class="flex-1 relative">
          <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400">search</span>
          <input 
            v-model="searchKeyword"
            type="text" 
            placeholder="搜索职位名称..." 
            class="w-full pl-10 pr-4 py-2.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-transparent focus:ring-2 focus:ring-primary focus:border-primary text-sm"
            @input="handleSearch"
          />
        </div>
        
        <!-- Status Filter -->
        <div class="flex gap-3">
          <select 
            v-model="statusFilter"
            class="px-4 py-2.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-transparent focus:ring-2 focus:ring-primary focus:border-primary text-sm min-w-[140px]"
            @change="handleFilterChange"
          >
            <option value="">全部状态</option>
            <option value="ACTIVE">在线</option>
            <option value="DRAFT">草稿</option>
            <option value="CLOSED">已下线</option>
            <option value="EXPIRED">已过期</option>
          </select>
          
          <button 
            @click="resetFilters"
            class="px-4 py-2.5 rounded-lg border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800 text-sm font-medium text-slate-600 dark:text-slate-400 transition-colors"
          >
            重置
          </button>
        </div>
      </div>
    </div>
    
    <!-- Filter Tabs -->
    <div class="mb-6 border-b border-slate-200 dark:border-slate-800">
      <div class="flex gap-8">
        <button 
          @click="activeTab = 'all'; fetchJobs()"
          :class="['pb-3 border-b-2 font-bold text-sm transition-colors', activeTab === 'all' ? 'border-primary text-primary' : 'border-transparent text-slate-500 hover:text-slate-800 dark:hover:text-slate-200']"
        >
          全部职位
        </button>
        <button 
          @click="activeTab = 'active'; fetchJobs()"
          :class="['pb-3 border-b-2 font-bold text-sm transition-colors', activeTab === 'active' ? 'border-primary text-primary' : 'border-transparent text-slate-500 hover:text-slate-800 dark:hover:text-slate-200']"
        >
          进行中
        </button>
        <button 
          @click="activeTab = 'closed'; fetchJobs()"
          :class="['pb-3 border-b-2 font-bold text-sm transition-colors', activeTab === 'closed' ? 'border-primary text-primary' : 'border-transparent text-slate-500 hover:text-slate-800 dark:hover:text-slate-200']"
        >
          已结束
        </button>
        <button 
          @click="activeTab = 'draft'; fetchJobs()"
          :class="['pb-3 border-b-2 font-bold text-sm transition-colors', activeTab === 'draft' ? 'border-primary text-primary' : 'border-transparent text-slate-500 hover:text-slate-800 dark:hover:text-slate-200']"
        >
          草稿
        </button>
      </div>
    </div>
    
    <!-- Main Table Container -->
    <div class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 overflow-hidden shadow-sm">
      <div v-if="loading" class="p-12 text-center">
        <span class="material-symbols-outlined text-4xl text-slate-300 animate-spin">sync</span>
        <p class="text-slate-500 mt-2">加载中...</p>
      </div>
      
      <div v-else-if="jobs.length === 0" class="p-12 text-center">
        <span class="material-symbols-outlined text-5xl text-slate-300 mb-4">work_off</span>
        <p class="text-slate-500 mb-4">暂无职位</p>
        <router-link to="/company/jobs/create" class="text-primary font-bold hover:underline">发布第一个职位</router-link>
      </div>
      
      <div v-else>
        <table class="w-full text-left border-collapse">
          <thead>
            <tr class="bg-slate-50 dark:bg-slate-800/50">
              <th class="px-6 py-4 text-xs font-bold text-slate-500 uppercase tracking-wider">职位名称</th>
              <th class="px-6 py-4 text-xs font-bold text-slate-500 uppercase tracking-wider">薪资</th>
              <th class="px-6 py-4 text-xs font-bold text-slate-500 uppercase tracking-wider">应聘人数</th>
              <th class="px-6 py-4 text-xs font-bold text-slate-500 uppercase tracking-wider">状态</th>
              <th class="px-6 py-4 text-xs font-bold text-slate-500 uppercase tracking-wider">发布日期</th>
              <th class="px-6 py-4 text-xs font-bold text-slate-500 uppercase tracking-wider text-right">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-slate-100 dark:divide-slate-800">
            <tr v-for="job in jobs" :key="job.id" class="hover:bg-slate-50 dark:hover:bg-slate-800/30 transition-colors">
              <td class="px-6 py-5">
                <div class="flex flex-col">
                  <span class="text-sm font-bold text-slate-900 dark:text-white">{{ job.title }}</span>
                  <span class="text-xs text-slate-500">{{ job.category || '研发' }} · {{ job.location || '上海' }}</span>
                </div>
              </td>
              <td class="px-6 py-5">
                <span class="text-sm text-slate-600 dark:text-slate-400">{{ job.salaryRange || '面议' }}</span>
              </td>
              <td class="px-6 py-5">
                <span class="text-sm font-medium text-primary">{{ job.applicants || job.applyCount || 0 }} 人</span>
              </td>
              <td class="px-6 py-5">
                <span :class="['inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-bold', getStatusClass(job.status)]">
                  {{ getStatusText(job.status) }}
                </span>
              </td>
              <td class="px-6 py-5 text-sm text-slate-500">{{ formatDate(job.createTime) }}</td>
              <td class="px-6 py-5 text-right">
                <div class="flex justify-end gap-2">
                  <button 
                    @click="editJob(job)" 
                    class="inline-flex items-center gap-1 px-3 py-1.5 rounded-lg text-xs font-medium text-blue-600 hover:bg-blue-50 dark:hover:bg-blue-900/20 transition-colors"
                  >
                    <span class="material-symbols-outlined text-sm">edit</span>
                    编辑
                  </button>
                  <button 
                    @click="viewApplicants(job)" 
                    class="inline-flex items-center gap-1 px-3 py-1.5 rounded-lg text-xs font-medium text-slate-600 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors"
                  >
                    <span class="material-symbols-outlined text-sm">visibility</span>
                    查看应聘
                  </button>
                  <button 
                    @click="toggleJobStatus(job)" 
                    :class="['inline-flex items-center gap-1 px-3 py-1.5 rounded-lg text-xs font-medium transition-colors', job.status === 'ACTIVE' ? 'text-orange-600 hover:bg-orange-50 dark:hover:bg-orange-900/20' : 'text-green-600 hover:bg-green-50 dark:hover:bg-green-900/20']"
                  >
                    <span class="material-symbols-outlined text-sm">{{ job.status === 'ACTIVE' ? 'toggle_off' : 'toggle_on' }}</span>
                    {{ job.status === 'ACTIVE' ? '下线' : '上线' }}
                  </button>
                  <button 
                    @click="deleteJob(job)" 
                    class="inline-flex items-center gap-1 px-3 py-1.5 rounded-lg text-xs font-medium text-red-600 hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors"
                  >
                    <span class="material-symbols-outlined text-sm">delete</span>
                    删除
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        
        <!-- Pagination -->
        <div class="px-6 py-4 flex items-center justify-between border-t border-slate-100 dark:border-slate-800">
          <span class="text-xs text-slate-500 font-medium tracking-wide">显示 {{ (pagination.page - 1) * pagination.pageSize + 1 }} 到 {{ Math.min(pagination.page * pagination.pageSize, pagination.total) }} 项，共 {{ pagination.total }} 个职位</span>
          <div class="flex gap-1">
            <button 
              @click="handlePageChange(pagination.page - 1)" 
              :disabled="pagination.page === 1"
              class="w-8 h-8 flex items-center justify-center rounded border border-slate-200 dark:border-slate-800 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors disabled:opacity-30"
            >
              <span class="material-symbols-outlined text-sm">chevron_left</span>
            </button>
            <button 
              v-for="p in visiblePages" 
              :key="p"
              @click="handlePageChange(p)"
              :class="['w-8 h-8 flex items-center justify-center rounded text-xs font-bold transition-colors', p === pagination.page ? 'bg-primary text-white' : 'border border-slate-200 dark:border-slate-800 hover:bg-slate-100 dark:hover:bg-slate-800']"
            >
              {{ p }}
            </button>
            <button 
              @click="handlePageChange(pagination.page + 1)" 
              :disabled="pagination.page >= pagination.totalPages"
              class="w-8 h-8 flex items-center justify-center rounded border border-slate-200 dark:border-slate-800 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors disabled:opacity-30"
            >
              <span class="material-symbols-outlined text-sm">chevron_right</span>
            </button>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Delete Confirm Modal -->
    <div v-if="deleteModal.show" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div class="bg-white dark:bg-slate-900 rounded-xl w-full max-w-md p-6 shadow-xl">
        <div class="flex items-center gap-3 mb-4">
          <div class="w-10 h-10 bg-red-100 rounded-full flex items-center justify-center">
            <span class="material-symbols-outlined text-red-600">delete</span>
          </div>
          <h3 class="text-lg font-bold text-slate-900 dark:text-white">确认删除职位</h3>
        </div>
        <p class="text-slate-600 dark:text-slate-400 mb-2">
          即将删除职位 <span class="font-bold text-slate-900 dark:text-white">「{{ deleteModal.job?.title }}」</span>
        </p>
        <p v-if="deleteModal.job?.applicants > 0 || deleteModal.job?.applyCount > 0" class="text-sm text-amber-600 bg-amber-50 dark:bg-amber-900/20 rounded-lg px-3 py-2 mb-4">
          <span class="material-symbols-outlined text-sm align-middle">warning</span>
          该职位已有 {{ deleteModal.job?.applicants || deleteModal.job?.applyCount }} 人投递，删除后相关申请记录也将一并移除。
        </p>
        <p v-else class="text-sm text-slate-500 mb-4">此操作不可恢复，请确认后继续。</p>
        <div class="flex justify-end gap-3">
          <button @click="deleteModal.show = false" class="px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 text-sm font-medium hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors">
            取消
          </button>
          <button @click="confirmDelete" :disabled="deleteModal.loading" class="px-4 py-2 rounded-lg bg-red-600 text-white text-sm font-medium hover:bg-red-700 transition-colors disabled:opacity-50 flex items-center gap-2">
            <span v-if="deleteModal.loading" class="material-symbols-outlined text-sm animate-spin">sync</span>
            {{ deleteModal.loading ? '删除中...' : '确认删除' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'

const router = useRouter()

const loading = ref(false)
const jobs = ref([])
const activeTab = ref('all')
const searchKeyword = ref('')
const statusFilter = ref('')

const stats = ref({
  active: 0,
  newThisWeek: 0,
  resumes: 0,
  interviews: 0,
  expired: 0
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0,
  totalPages: 0
})

const deleteModal = reactive({
  show: false,
  job: null,
  loading: false
})

const visiblePages = computed(() => {
  const pages = []
  const total = pagination.totalPages
  const current = pagination.page
  for (let i = 1; i <= Math.min(total, 5); i++) {
    pages.push(i)
  }
  return pages
})

let searchTimeout = null

onMounted(() => {
  fetchJobs()
  fetchStats()
})

const fetchStats = async () => {
  try {
    const data = await api.getCompanyStats()
    stats.value = {
      active: data.activeJobs || 0,
      newThisWeek: data.newThisWeek || 0,
      resumes: data.totalApplications || 0,
      interviews: data.interviewCount || 0,
      expired: data.expiredJobs || 0
    }
  } catch (error) {
    console.error('获取统计失败', error)
  }
}

const fetchJobs = async () => {
  loading.value = true
  try {
    const statusMap = {
      'all': statusFilter.value || '',
      'active': statusFilter.value || 'ACTIVE',
      'closed': statusFilter.value || 'CLOSED',
      'draft': statusFilter.value || 'DRAFT'
    }
    
    const params = {
      page: pagination.page - 1,
      pageSize: pagination.pageSize,
      status: statusMap[activeTab.value],
      keyword: searchKeyword.value || undefined
    }
    
    const data = await api.getCompanyJobs(params)
    jobs.value = data.content || data.list || []
    pagination.total = data.totalElements || data.total || 0
    pagination.totalPages = Math.ceil(pagination.total / pagination.pageSize)
  } catch (error) {
    console.error('获取职位列表失败', error)
    jobs.value = []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  if (searchTimeout) clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => {
    pagination.page = 1
    fetchJobs()
  }, 300)
}

const handleFilterChange = () => {
  pagination.page = 1
  fetchJobs()
}

const resetFilters = () => {
  searchKeyword.value = ''
  statusFilter.value = ''
  activeTab.value = 'all'
  pagination.page = 1
  fetchJobs()
}

const handlePageChange = (page) => {
  pagination.page = page
  fetchJobs()
}

const getStatusClass = (status) => {
  const map = {
    'ACTIVE': 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
    'PUBLISHED': 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
    'DRAFT': 'bg-slate-100 text-slate-600 dark:bg-slate-800 dark:text-slate-400',
    'CLOSED': 'bg-slate-100 text-slate-600 dark:bg-slate-800 dark:text-slate-400',
    'PAUSED': 'bg-yellow-100 text-yellow-700 dark:bg-yellow-900/30 dark:text-yellow-400',
    'EXPIRED': 'bg-orange-100 text-orange-700 dark:bg-orange-900/30 dark:text-orange-400',
    'DELETED': 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400'
  }
  return map[status] || 'bg-slate-100 text-slate-600'
}

const getStatusText = (status) => {
  const map = {
    'ACTIVE': '在线',
    'PUBLISHED': '在线',
    'DRAFT': '草稿',
    'CLOSED': '已下线',
    'PAUSED': '已暂停',
    'EXPIRED': '已过期',
    'DELETED': '已删除'
  }
  return map[status] || status
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const editJob = (job) => {
  router.push(`/company/jobs/${job.id}/edit`)
}

const viewApplicants = (job) => {
  router.push(`/company/applications?jobId=${job.id}`)
}

const toggleJobStatus = async (job) => {
  const isActive = job.status === 'ACTIVE' || job.status === 'PUBLISHED'
  const newStatus = isActive ? 'CLOSED' : 'ACTIVE'
  
  try {
    await api.toggleJobStatus(job.id, newStatus)
    job.status = newStatus
    fetchStats()
  } catch (error) {
    console.error('操作失败', error)
    const errorMsg = error.response?.data?.message || error.message || '操作失败'
    alert(errorMsg)
  }
}

const deleteJob = (job) => {
  deleteModal.job = job
  deleteModal.show = true
  deleteModal.loading = false
}

const confirmDelete = async () => {
  deleteModal.loading = true
  try {
    await api.deleteCompanyJob(deleteModal.job.id)
    deleteModal.show = false

    // 从当前列表中移除已删除的职位
    const deletedJobId = deleteModal.job.id
    jobs.value = jobs.value.filter(job => job.id !== deletedJobId)

    // 更新分页信息
    pagination.total = Math.max(0, pagination.total - 1)
    pagination.totalPages = Math.ceil(pagination.total / pagination.pageSize)

    // 如果当前页没有数据且不是第一页，跳到上一页
    if (jobs.value.length === 0 && pagination.page > 1) {
      pagination.page--
      await fetchJobs()
    }

    deleteModal.job = null
    fetchStats()
  } catch (error) {
    console.error('删除失败', error)
    alert(error.response?.data?.message || '删除失败，请稍后重试')
  } finally {
    deleteModal.loading = false
  }
}
</script>
