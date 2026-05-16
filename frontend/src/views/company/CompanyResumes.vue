<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h2 class="text-2xl font-bold text-slate-800">简历筛选</h2>
        <p class="text-sm text-slate-500 mt-1">查看和管理所有求职者投递的简历</p>
      </div>
      <div class="flex items-center gap-2">
        <button
          @click="viewMode = 'card'"
          :class="['p-2 rounded-lg transition-colors', viewMode === 'card' ? 'bg-blue-100 text-blue-600' : 'bg-slate-100 text-slate-600 hover:bg-slate-200']"
          title="卡片视图"
        >
          <span class="material-symbols-outlined">grid_view</span>
        </button>
        <button
          @click="viewMode = 'table'"
          :class="['p-2 rounded-lg transition-colors', viewMode === 'table' ? 'bg-blue-100 text-blue-600' : 'bg-slate-100 text-slate-600 hover:bg-slate-200']"
          title="表格视图"
        >
          <span class="material-symbols-outlined">table_rows</span>
        </button>
      </div>
    </div>

    <!-- Statistics Overview -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <div v-for="stat in stats" :key="stat.label" class="bg-white rounded-xl border border-slate-200 p-4 flex items-center gap-4">
        <div :class="['w-12 h-12 rounded-xl flex items-center justify-center', stat.bg]">
          <span :class="['material-symbols-outlined text-xl', stat.color]">{{ stat.icon }}</span>
        </div>
        <div>
          <p class="text-2xl font-bold text-slate-800">{{ stat.value }}</p>
          <p class="text-xs text-slate-500">{{ stat.label }}</p>
        </div>
      </div>
    </div>

    <!-- Filters -->
    <div class="bg-white rounded-xl border border-slate-200 p-4">
      <div class="flex flex-wrap items-center gap-3">
        <input
          v-model="filters.keyword"
          @keyup.enter="handleSearch"
          type="text"
          placeholder="搜索姓名..."
          class="px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm flex-1 min-w-[160px] focus:outline-none focus:ring-2 focus:ring-blue-300"
        />
        <select v-model="filters.jobId" class="px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
          <option value="">全部职位</option>
          <option v-for="job in jobs" :key="job.id" :value="job.id">{{ job.title }}</option>
        </select>
        <select v-model="filters.status" class="px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
          <option value="">全部状态</option>
          <option value="PENDING">待处理</option>
          <option value="REVIEWING">查看中</option>
          <option value="INTERVIEW">面试中</option>
          <option value="REJECTED">已拒绝</option>
          <option value="HIRED">已录用</option>
        </select>
        <button @click="handleSearch" class="px-5 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 text-sm font-medium transition-colors">
          筛选
        </button>
        <button @click="resetFilters" class="px-5 py-2 bg-slate-100 text-slate-600 rounded-lg hover:bg-slate-200 text-sm font-medium transition-colors">
          重置
        </button>
      </div>
    </div>

    <!-- Loading -->
    <div v-if="loading" class="bg-white rounded-xl border border-slate-200 p-12 text-center">
      <span class="material-symbols-outlined text-4xl text-slate-300 animate-spin">sync</span>
      <p class="text-slate-500 mt-2">加载中...</p>
    </div>

    <!-- Empty -->
    <div v-else-if="applications.length === 0" class="bg-white rounded-xl border border-slate-200 p-12 text-center">
      <span class="material-symbols-outlined text-5xl text-slate-300 mb-3">inbox</span>
      <p class="text-slate-500">暂无简历投递</p>
    </div>

    <!-- Card View -->
    <div v-else-if="viewMode === 'card'" class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
      <div
        v-for="app in applications"
        :key="app.applicationId || app.id"
        class="bg-white rounded-xl border border-slate-200 p-5 hover:shadow-md transition-shadow"
      >
        <div class="flex items-start justify-between mb-4">
          <div class="flex items-center gap-3">
            <div class="w-12 h-12 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-full flex items-center justify-center text-white font-bold text-lg">
              {{ app.userName?.charAt(0) || 'U' }}
            </div>
            <div>
              <h4 class="font-bold text-slate-800">{{ app.userName || '未知用户' }}</h4>
              <p class="text-xs text-slate-500 mt-0.5">{{ formatDate(app.applyTime) }}</p>
            </div>
          </div>
          <span :class="['text-xs px-2 py-1 rounded-full font-medium', getStatusClass(app.status)]">
            {{ getStatusText(app.status) }}
          </span>
        </div>
        <div class="flex items-center gap-1 text-sm text-slate-600 mb-4">
          <span class="material-symbols-outlined text-base text-slate-400">work</span>
          {{ app.jobTitle || '-' }}
        </div>
        <div class="flex items-center gap-2">
          <select
            :value="app.status"
            @change="updateStatus(app, $event.target.value)"
            class="flex-1 px-3 py-1.5 bg-slate-50 border border-slate-200 rounded-lg text-xs"
          >
            <option value="PENDING">待处理</option>
            <option value="REVIEWING">查看中</option>
            <option value="INTERVIEW">面试中</option>
            <option value="REJECTED">已拒绝</option>
            <option value="HIRED">已录用</option>
          </select>
          <button @click="viewResume(app)" class="p-1.5 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors" title="查看简历">
            <span class="material-symbols-outlined text-xl">visibility</span>
          </button>
        </div>
      </div>
    </div>

    <!-- Table View -->
    <div v-else class="bg-white rounded-xl border border-slate-200 overflow-hidden">
      <table class="w-full">
        <thead class="bg-slate-50 border-b border-slate-200">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 uppercase">姓名</th>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 uppercase">应聘职位</th>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 uppercase">投递时间</th>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 uppercase">状态</th>
            <th class="px-6 py-3 text-left text-xs font-bold text-slate-500 uppercase">操作</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-slate-100">
          <tr v-for="app in applications" :key="app.applicationId || app.id" class="hover:bg-slate-50 transition-colors">
            <td class="px-6 py-4">
              <div class="flex items-center gap-3">
                <div class="w-9 h-9 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-full flex items-center justify-center text-white font-bold text-sm">
                  {{ app.userName?.charAt(0) || 'U' }}
                </div>
                <span class="font-medium text-slate-800">{{ app.userName || '未知用户' }}</span>
              </div>
            </td>
            <td class="px-6 py-4 text-slate-600">{{ app.jobTitle || '-' }}</td>
            <td class="px-6 py-4 text-slate-500 text-sm">{{ formatDate(app.applyTime) }}</td>
            <td class="px-6 py-4">
              <span :class="['text-xs px-2 py-1 rounded-full font-medium', getStatusClass(app.status)]">
                {{ getStatusText(app.status) }}
              </span>
            </td>
            <td class="px-6 py-4">
              <div class="flex items-center gap-2">
                <select
                  :value="app.status"
                  @change="updateStatus(app, $event.target.value)"
                  class="px-2 py-1 bg-slate-50 border border-slate-200 rounded-lg text-xs"
                >
                  <option value="PENDING">待处理</option>
                  <option value="REVIEWING">查看中</option>
                  <option value="INTERVIEW">面试中</option>
                  <option value="REJECTED">已拒绝</option>
                  <option value="HIRED">已录用</option>
                </select>
                <button @click="viewResume(app)" class="p-1.5 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors" title="查看简历">
                  <span class="material-symbols-outlined text-lg">visibility</span>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div v-if="!loading && total > pageSize" class="flex justify-center">
      <div class="flex items-center gap-2">
        <button @click="handlePageChange(page - 1)" :disabled="page === 1" class="px-4 py-2 rounded-lg border border-slate-200 text-sm disabled:opacity-50 hover:bg-slate-50 transition-colors">
          上一页
        </button>
        <span class="text-sm text-slate-500 px-2">{{ page }} / {{ Math.ceil(total / pageSize) }}</span>
        <button @click="handlePageChange(page + 1)" :disabled="page >= Math.ceil(total / pageSize)" class="px-4 py-2 rounded-lg border border-slate-200 text-sm disabled:opacity-50 hover:bg-slate-50 transition-colors">
          下一页
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const applications = ref([])
const jobs = ref([])
const total = ref(0)
const page = ref(1)
const pageSize = ref(12)
const viewMode = ref('card')

const filters = reactive({
  keyword: '',
  jobId: route.query.jobId || '',
  status: route.query.status || ''
})

const stats = computed(() => {
  const all = applications.value
  const countByStatus = (s) => applications.value.filter(a => a.status === s).length
  return [
    { label: '全部投递', value: total.value, icon: 'assignment', bg: 'bg-blue-50', color: 'text-blue-600' },
    { label: '待处理', value: countByStatus('PENDING'), icon: 'pending', bg: 'bg-yellow-50', color: 'text-yellow-600' },
    { label: '面试中', value: countByStatus('INTERVIEW'), icon: 'calendar_month', bg: 'bg-purple-50', color: 'text-purple-600' },
    { label: '已录用', value: countByStatus('HIRED'), icon: 'check_circle', bg: 'bg-green-50', color: 'text-green-600' }
  ]
})

onMounted(async () => {
  await Promise.all([fetchJobs(), fetchApplications()])
})

const fetchJobs = async () => {
  try {
    const data = await api.getCompanyJobs({ pageSize: 100 })
    jobs.value = data.content || data.list || []
  } catch (e) {
    jobs.value = []
  }
}

const fetchApplications = async () => {
  loading.value = true
  try {
    const data = await api.getCompanyApplications({
      jobId: filters.jobId || undefined,
      status: filters.status || undefined,
      keyword: filters.keyword || undefined,
      page: page.value - 1,
      pageSize: pageSize.value
    })
    applications.value = data.content || data.list || data || []
    total.value = data.totalElements || data.total || applications.value.length || 0
  } catch (e) {
    applications.value = []
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  page.value = 1
  fetchApplications()
}

const resetFilters = () => {
  filters.keyword = ''
  filters.jobId = ''
  filters.status = ''
  page.value = 1
  fetchApplications()
}

const handlePageChange = (newPage) => {
  page.value = newPage
  fetchApplications()
}

const getStatusClass = (status) => {
  const map = {
    'PENDING': 'bg-yellow-100 text-yellow-700',
    'REVIEWING': 'bg-blue-100 text-blue-700',
    'INTERVIEW': 'bg-purple-100 text-purple-700',
    'REJECTED': 'bg-red-100 text-red-700',
    'HIRED': 'bg-green-100 text-green-700'
  }
  return map[status] || 'bg-slate-100 text-slate-600'
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '待处理',
    'REVIEWING': '查看中',
    'INTERVIEW': '面试中',
    'REJECTED': '已拒绝',
    'HIRED': '已录用'
  }
  return map[status] || status
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const viewResume = (app) => {
  const id = app.applicationId || app.id
  router.push(`/company/resumes/${id}`)
}

const updateStatus = async (app, status) => {
  const id = app.applicationId || app.id
  try {
    await api.updateApplicationStatus(id, status)
    app.status = status
  } catch (e) {
    console.error('更新状态失败', e)
    fetchApplications()
  }
}
</script>
