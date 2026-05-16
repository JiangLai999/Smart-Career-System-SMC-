<template>
  <div class="space-y-6">
    <!-- Welcome Section -->
    <div class="bg-gradient-to-r from-blue-600 to-indigo-600 rounded-2xl p-6 text-white">
      <h2 class="text-2xl font-bold mb-2">欢迎回来，{{ companyInfo?.contactName || 'HR' }}！</h2>
      <p class="text-blue-100">这里是您的招聘数据概览</p>
    </div>
    
    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <div v-for="stat in stats" :key="stat.label" class="bg-white rounded-xl p-5 border border-slate-200 shadow-sm">
        <div class="flex items-center justify-between mb-3">
          <span :class="['p-2 rounded-lg', stat.bgColor]">
            <span :class="['material-symbols-outlined', stat.iconColor]">{{ stat.icon }}</span>
          </span>
          <span :class="['text-xs font-medium px-2 py-1 rounded-full', stat.trendClass]">
            {{ stat.trend }}
          </span>
        </div>
        <p class="text-2xl font-bold text-slate-800">{{ stat.value }}</p>
        <p class="text-sm text-slate-500">{{ stat.label }}</p>
      </div>
    </div>
    
    <!-- Main Content Grid -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Recent Jobs -->
      <div class="lg:col-span-2 bg-white rounded-xl border border-slate-200 shadow-sm">
        <div class="flex items-center justify-between p-5 border-b border-slate-100">
          <h3 class="font-bold text-slate-800">职位列表</h3>
          <router-link to="/company/jobs" class="text-sm text-blue-600 hover:text-blue-700 font-medium">查看全部</router-link>
        </div>
        <div class="p-5">
          <div v-if="jobs.length === 0" class="text-center py-8">
            <span class="material-symbols-outlined text-4xl text-slate-300 mb-2">work_off</span>
            <p class="text-slate-500">暂无发布的职位</p>
            <router-link to="/company/jobs/create" class="text-sm text-blue-600 hover:text-blue-700 font-medium mt-2 inline-block">发布第一个职位</router-link>
          </div>
          <div v-else class="space-y-3">
            <div v-for="job in jobs.slice(0, 5)" :key="job.id" class="flex items-center justify-between p-3 bg-slate-50 rounded-lg hover:bg-slate-100 transition-colors">
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
                  <span class="material-symbols-outlined text-blue-600">work</span>
                </div>
                <div>
                  <p class="font-medium text-slate-800">{{ job.title }}</p>
                  <p class="text-xs text-slate-500">{{ job.applicants || 0 }}人投递 · {{ job.views || 0 }}浏览</p>
                </div>
              </div>
              <span :class="['text-xs px-2 py-1 rounded-full', getJobStatusClass(job.status)]">
                {{ getJobStatusText(job.status) }}
              </span>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Quick Actions -->
      <div class="bg-white rounded-xl border border-slate-200 shadow-sm">
        <div class="p-5 border-b border-slate-100">
          <h3 class="font-bold text-slate-800">快捷操作</h3>
        </div>
        <div class="p-5 space-y-3">
          <router-link to="/company/jobs/create" class="flex items-center gap-3 p-3 rounded-lg bg-blue-50 text-blue-700 hover:bg-blue-100 transition-colors">
            <span class="material-symbols-outlined">add_circle</span>
            <span class="font-medium">发布新职位</span>
          </router-link>
          <router-link to="/company/resumes" class="flex items-center gap-3 p-3 rounded-lg bg-slate-50 text-slate-700 hover:bg-slate-100 transition-colors">
            <span class="material-symbols-outlined">search</span>
            <span class="font-medium">筛选简历</span>
          </router-link>
          <router-link to="/company/applications" class="flex items-center gap-3 p-3 rounded-lg bg-slate-50 text-slate-700 hover:bg-slate-100 transition-colors">
            <span class="material-symbols-outlined">assignment</span>
            <span class="font-medium">查看投递</span>
          </router-link>
          <router-link to="/company/profile" class="flex items-center gap-3 p-3 rounded-lg bg-slate-50 text-slate-700 hover:bg-slate-100 transition-colors">
            <span class="material-symbols-outlined">edit</span>
            <span class="font-medium">企业资料</span>
          </router-link>
        </div>
      </div>
    </div>
    
    <!-- Recent Applications -->
    <div class="bg-white rounded-xl border border-slate-200 shadow-sm">
      <div class="flex items-center justify-between p-5 border-b border-slate-100">
        <h3 class="font-bold text-slate-800">最近投递</h3>
        <router-link to="/company/applications" class="text-sm text-blue-600 hover:text-blue-700 font-medium">查看全部</router-link>
      </div>
      <div class="p-5">
        <div v-if="applications.length === 0" class="text-center py-8">
          <span class="material-symbols-outlined text-4xl text-slate-300 mb-2">inbox</span>
          <p class="text-slate-500">暂无简历投递</p>
        </div>
        <div v-else class="overflow-x-auto">
          <table class="w-full">
            <thead>
              <tr class="text-left text-xs text-slate-500 uppercase">
                <th class="pb-3 font-medium">求职者</th>
                <th class="pb-3 font-medium">应聘职位</th>
                <th class="pb-3 font-medium">投递时间</th>
                <th class="pb-3 font-medium">状态</th>
                <th class="pb-3 font-medium">操作</th>
              </tr>
            </thead>
            <tbody class="text-sm">
              <tr v-for="app in applications.slice(0, 5)" :key="app.applicationId || app.id" class="border-t border-slate-100">
                <td class="py-3">
                  <div class="flex items-center gap-2">
                    <div class="w-8 h-8 bg-slate-200 rounded-full flex items-center justify-center text-slate-600 font-bold text-xs">
                      {{ app.userName?.charAt(0) || 'U' }}
                    </div>
                    <span class="font-medium text-slate-800">{{ app.userName || '未知用户' }}</span>
                  </div>
                </td>
                <td class="py-3 text-slate-600">{{ app.jobTitle || '-' }}</td>
                <td class="py-3 text-slate-500">{{ formatDate(app.applyTime) }}</td>
                <td class="py-3">
                  <span :class="['text-xs px-2 py-1 rounded-full', getApplicationStatusClass(app.status)]">
                    {{ getApplicationStatusText(app.status) }}
                  </span>
                </td>
                <td class="py-3">
                  <button @click="viewResume(app)" class="text-blue-600 hover:text-blue-700 font-medium text-sm">查看</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- System Announcements -->
    <div v-if="announcements.length > 0" class="bg-white rounded-xl border border-slate-200 shadow-sm">
      <div class="flex items-center justify-between p-5 border-b border-slate-100">
        <h3 class="font-bold text-slate-800 flex items-center gap-2">
          <span class="material-symbols-outlined text-blue-600">campaign</span>
          系统公告
        </h3>
      </div>
      <div class="p-5 space-y-3">
        <div
          v-for="announcement in announcements"
          :key="announcement.id"
          @click="viewAnnouncement(announcement)"
          class="p-4 rounded-lg bg-slate-50 hover:bg-slate-100 cursor-pointer transition-colors"
        >
          <div class="flex items-start justify-between gap-3">
            <div class="flex-1">
              <div class="flex items-center gap-2 mb-1">
                <span :class="['text-xs px-2 py-0.5 rounded-full font-medium', getAnnouncementTypeClass(announcement.type)]">
                  {{ getAnnouncementTypeName(announcement.type) }}
                </span>
                <span v-if="announcement.priority === 'HIGH'" class="text-xs px-2 py-0.5 rounded-full font-medium bg-red-100 text-red-700">
                  重要
                </span>
              </div>
              <h4 class="font-medium text-slate-800 mb-1">{{ announcement.title }}</h4>
              <p class="text-sm text-slate-600 line-clamp-2">{{ announcement.content }}</p>
            </div>
            <span class="text-xs text-slate-400 whitespace-nowrap">{{ formatAnnouncementTime(announcement.publishTime) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'

const router = useRouter()
const companyInfo = ref(JSON.parse(localStorage.getItem('companyInfo') || '{}'))

const stats = ref([
  { label: '在招职位', value: '0', icon: 'work', bgColor: 'bg-blue-100', iconColor: 'text-blue-600', trend: '0', trendClass: 'bg-slate-100 text-slate-600' },
  { label: '收到简历', value: '0', icon: 'folder_shared', bgColor: 'bg-green-100', iconColor: 'text-green-600', trend: '0', trendClass: 'bg-slate-100 text-slate-600' },
  { label: '面试邀请', value: '0', icon: 'event_available', bgColor: 'bg-purple-100', iconColor: 'text-purple-600', trend: '0', trendClass: 'bg-slate-100 text-slate-600' },
  { label: '简历待处理', value: '0', icon: 'pending_actions', bgColor: 'bg-yellow-100', iconColor: 'text-yellow-600', trend: '0', trendClass: 'bg-slate-100 text-slate-600' }
])

const jobs = ref([])
const applications = ref([])
const announcements = ref([])

onMounted(async () => {
  try {
    const [statsData, jobsData, appsData, announcementsData] = await Promise.all([
      api.getCompanyStats().catch(() => ({})),
      api.getCompanyJobs({ page: 0, pageSize: 5 }).catch(() => ({ content: [] })),
      api.getCompanyApplications({ page: 0, pageSize: 5 }).catch(() => ({ content: [] })),
      api.getActiveAnnouncements().catch(() => [])
    ])

    if (statsData) {
      stats.value = [
        { label: '在招职位', value: statsData.activeJobs || statsData.totalJobs || '0', icon: 'work', bgColor: 'bg-blue-100', iconColor: 'text-blue-600', trend: '+0', trendClass: 'bg-green-100 text-green-600' },
        { label: '收到简历', value: statsData.totalApplications || '0', icon: 'folder_shared', bgColor: 'bg-green-100', iconColor: 'text-green-600', trend: '+0', trendClass: 'bg-green-100 text-green-600' },
        { label: '面试邀请', value: statsData.interviewCount || '0', icon: 'event_available', bgColor: 'bg-purple-100', iconColor: 'text-purple-600', trend: '+0', trendClass: 'bg-green-100 text-green-600' },
        { label: '简历待处理', value: statsData.pendingCount || '0', icon: 'pending_actions', bgColor: 'bg-yellow-100', iconColor: 'text-yellow-600', trend: '0', trendClass: 'bg-slate-100 text-slate-600' }
      ]
    }

    jobs.value = jobsData.content || jobsData.list || jobsData || []
    applications.value = appsData.content || appsData.list || appsData || []
    announcements.value = (announcementsData || []).slice(0, 3)
  } catch (error) {
    console.error('获取数据失败', error)
  }
})

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const getApplicationStatusClass = (status) => {
  const map = {
    'PENDING': 'bg-yellow-100 text-yellow-700',
    'REVIEWING': 'bg-blue-100 text-blue-700',
    'INTERVIEW': 'bg-purple-100 text-purple-700',
    'REJECTED': 'bg-red-100 text-red-700',
    'HIRED': 'bg-green-100 text-green-700'
  }
  return map[status] || 'bg-slate-100 text-slate-600'
}

const getApplicationStatusText = (status) => {
  const map = {
    'PENDING': '待处理',
    'REVIEWING': '查看中',
    'INTERVIEW': '面试中',
    'REJECTED': '已拒绝',
    'HIRED': '已录用'
  }
  return map[status] || status
}

const getJobStatusClass = (status) => {
  const map = {
    'ACTIVE': 'bg-green-100 text-green-700',
    'PUBLISHED': 'bg-green-100 text-green-700',
    'DRAFT': 'bg-slate-100 text-slate-600',
    'CLOSED': 'bg-slate-100 text-slate-600',
    'PAUSED': 'bg-yellow-100 text-yellow-700',
    'EXPIRED': 'bg-orange-100 text-orange-700'
  }
  return map[status] || 'bg-slate-100 text-slate-600'
}

const getJobStatusText = (status) => {
  const map = {
    'ACTIVE': '招聘中',
    'PUBLISHED': '招聘中',
    'DRAFT': '草稿',
    'CLOSED': '已下线',
    'PAUSED': '已暂停',
    'EXPIRED': '已过期'
  }
  return map[status] || status
}

const getAnnouncementTypeName = (type) => {
  const map = {
    'SYSTEM': '系统公告',
    'MAINTENANCE': '维护公告',
    'UPDATE': '更新公告',
    'PROMOTION': '推广公告'
  }
  return map[type] || '公告'
}

const getAnnouncementTypeClass = (type) => {
  const map = {
    'SYSTEM': 'bg-blue-100 text-blue-700',
    'MAINTENANCE': 'bg-orange-100 text-orange-700',
    'UPDATE': 'bg-purple-100 text-purple-700',
    'PROMOTION': 'bg-pink-100 text-pink-700'
  }
  return map[type] || 'bg-slate-100 text-slate-600'
}

const formatAnnouncementTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  if (days === 0) return '今天'
  if (days === 1) return '昨天'
  if (days < 7) return `${days}天前`
  return date.toLocaleDateString('zh-CN')
}

const viewAnnouncement = (announcement) => {
  alert(`${announcement.title}\n\n${announcement.content}`)
}

const viewResume = (app) => {
  router.push(`/company/resumes/${app.applicationId || app.id}`)
}
</script>
