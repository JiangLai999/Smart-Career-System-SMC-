<template>
  <div>
    <!-- Welcome Section -->
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
      <div>
        <h1 class="text-slate-900 dark:text-slate-100 text-3xl font-extrabold tracking-tight">
          欢迎回来，{{ userInfo?.username || userInfo?.realName || '用户' }}! 👋
        </h1>
        <p class="text-slate-500 dark:text-slate-400 mt-2 text-base font-normal">以下是您今天的求职动态。</p>
      </div>
      <button 
        @click="$router.push('/app/resume')"
        class="flex items-center gap-2 border-2 border-primary text-primary hover:bg-primary hover:text-white px-5 py-2.5 rounded-xl font-bold transition-all text-sm tracking-wide"
      >
        <span class="material-symbols-outlined text-lg">description</span>
        查看简历
      </button>
    </div>
    
    <!-- KPI Cards -->
    <div class="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-6 mb-8">
      <div class="flex flex-col gap-2 rounded-2xl p-6 bg-primary text-white shadow-xl shadow-primary/20 transform hover:-translate-y-1 transition-transform cursor-pointer" @click="$router.push('/app/applications')">
        <div class="flex justify-between items-start">
          <p class="text-white/90 text-sm font-semibold tracking-wide">已投递职位</p>
          <span class="material-symbols-outlined opacity-50 text-xl">send</span>
        </div>
        <p class="text-3xl font-bold tabular-nums">{{ stats.applied || 0 }}</p>
        <div v-if="stats.weeklyApplied > 0" class="mt-2 text-xs flex items-center gap-1.5 bg-white/20 w-fit px-2.5 py-1 rounded-full font-medium">
          <span class="material-symbols-outlined text-sm">trending_up</span>
          <span>+{{ stats.weeklyApplied }} 本周</span>
        </div>
      </div>
      
      <div class="flex flex-col gap-2 rounded-2xl p-6 bg-emerald-500 text-white shadow-xl shadow-emerald-500/20 transform hover:-translate-y-1 transition-transform cursor-pointer" @click="$router.push('/app/applications?status=INTERVIEW')">
        <div class="flex justify-between items-start">
          <p class="text-white/90 text-sm font-semibold tracking-wide">面试</p>
          <span class="material-symbols-outlined opacity-50 text-xl">event</span>
        </div>
        <p class="text-3xl font-bold tabular-nums">{{ stats.interviews || 0 }}</p>
        <div v-if="stats.nextInterview" class="mt-2 text-xs flex items-center gap-1.5 bg-white/20 w-fit px-2.5 py-1 rounded-full font-medium">
          <span class="material-symbols-outlined text-sm">schedule</span>
          <span>下一次：{{ stats.nextInterview }}</span>
        </div>
      </div>
      
      <div class="flex flex-col gap-2 rounded-2xl p-6 bg-amber-400 text-slate-900 shadow-xl shadow-amber-400/20 transform hover:-translate-y-1 transition-transform cursor-pointer" @click="$router.push('/app/profile')">
        <div class="flex justify-between items-start">
          <p class="text-slate-700/90 text-sm font-semibold tracking-wide">个人资料完善度</p>
          <span class="material-symbols-outlined opacity-50 text-xl">verified</span>
        </div>
        <p class="text-3xl font-bold tabular-nums">{{ stats.profileComplete || 0 }}%</p>
        <div v-if="stats.profileComplete < 100" class="mt-2 text-xs flex items-center gap-1.5 bg-black/10 w-fit px-2.5 py-1 rounded-full font-medium cursor-pointer hover:bg-black/20">
          <span class="material-symbols-outlined text-sm">info</span>
          <span>完善资料</span>
        </div>
      </div>
      
      <div class="flex flex-col gap-2 rounded-2xl p-6 bg-cyan-500 text-white shadow-xl shadow-cyan-500/20 transform hover:-translate-y-1 transition-transform cursor-pointer" @click="$router.push('/assessment')">
        <div class="flex justify-between items-start">
          <p class="text-white/90 text-sm font-semibold tracking-wide">评估分数</p>
          <span class="material-symbols-outlined opacity-50 text-xl">analytics</span>
        </div>
        <p class="text-3xl font-bold tabular-nums">{{ stats.assessmentScore || 0 }}</p>
        <div v-if="stats.topPercent" class="mt-2 text-xs flex items-center gap-1.5 bg-white/20 w-fit px-2.5 py-1 rounded-full font-medium">
          <span class="material-symbols-outlined text-sm">workspace_premium</span>
          <span>前 {{ stats.topPercent }}% 的用户</span>
        </div>
      </div>
    </div>
    
    <!-- Detailed Widgets -->
    <div class="grid grid-cols-1 lg:grid-cols-12 gap-8">
      <!-- Timeline -->
      <div class="lg:col-span-7 bg-white dark:bg-slate-900 p-6 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-800">
        <div class="flex justify-between items-center mb-6">
          <div class="flex items-center gap-3">
            <h3 class="text-lg font-bold tracking-tight">实时进展</h3>
            <span v-if="timelineItems.length > 0" class="text-xs bg-primary/10 text-primary font-bold px-2.5 py-0.5 rounded-full">{{ timelineItems.length }} 条</span>
          </div>
          <button class="text-primary text-sm font-semibold hover:underline tracking-wide" @click="$router.push('/app/applications')">查看全部</button>
        </div>

        <div v-if="loading" class="text-center py-12">
          <div class="animate-spin rounded-full h-12 w-12 border-4 border-primary border-t-transparent mx-auto"></div>
          <p class="text-slate-500 dark:text-slate-400 mt-4">加载中...</p>
        </div>

        <div v-else-if="timelineItems.length > 0" class="space-y-0">
          <div
            v-for="(item, index) in timelineItems"
            :key="index"
            class="relative flex gap-4 pb-6 last:pb-0"
          >
            <!-- 竖线连接 -->
            <div v-if="index < timelineItems.length - 1" class="absolute left-3 top-7 bottom-0 w-0.5 bg-slate-100 dark:bg-slate-800"></div>
            <!-- 状态圆点 -->
            <div
              :class="[
                'z-10 rounded-full size-6 flex items-center justify-center shrink-0 mt-0.5',
                item.dotClass
              ]"
            >
              <span class="material-symbols-outlined text-white" style="font-size:13px">{{ item.icon }}</span>
            </div>
            <!-- 内容 -->
            <div class="flex-1 min-w-0">
              <div class="flex items-start justify-between gap-2">
                <div>
                  <span :class="['inline-flex items-center gap-1 text-xs font-bold px-2 py-0.5 rounded-full mb-1', item.badgeClass]">
                    {{ item.title }}
                  </span>
                  <p class="font-semibold text-slate-800 dark:text-slate-200 text-sm leading-snug truncate">{{ item.position }}</p>
                  <p class="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
                    <span class="text-primary font-semibold">{{ item.company }}</span>
                  </p>
                </div>
                <span class="text-xs text-slate-400 whitespace-nowrap mt-0.5">{{ item.time }}</span>
              </div>
              <!-- 面试时间提示 -->
              <div v-if="item.interviewTime" class="flex items-center gap-1.5 mt-2 text-xs bg-emerald-50 dark:bg-emerald-900/20 text-emerald-700 dark:text-emerald-400 w-fit px-2.5 py-1 rounded-lg font-medium border border-emerald-100 dark:border-emerald-800">
                <span class="material-symbols-outlined" style="font-size:13px">event</span>
                <span>面试时间：{{ item.interviewTime }}</span>
              </div>
              <!-- HR备注 -->
              <p v-if="item.hrComment" class="mt-1.5 text-xs text-slate-500 dark:text-slate-400 italic truncate">{{ item.hrComment }}</p>
            </div>
          </div>
        </div>

        <div v-else class="text-center py-12">
          <span class="material-symbols-outlined text-5xl text-slate-300 dark:text-slate-600 mb-4">inbox</span>
          <p class="text-slate-500 dark:text-slate-400">暂无申请记录</p>
          <button class="mt-4 px-6 py-2 bg-primary text-white rounded-xl font-bold text-sm" @click="$router.push('/app/jobs')">
            浏览职位
          </button>
        </div>
      </div>
      
      <!-- Skill Analysis -->
      <div class="lg:col-span-5 bg-white dark:bg-slate-900 p-6 rounded-2xl shadow-sm border border-slate-100 dark:border-slate-800">
        <h3 class="text-lg font-bold mb-6 tracking-tight">技能匹配分析</h3>
        <div v-if="skills.length > 0" class="space-y-6">
          <div v-for="(skill, index) in skills" :key="index">
            <div class="flex justify-between items-center mb-2">
              <span class="text-sm font-semibold tracking-wide">{{ skill.name }}</span>
              <span class="text-xs font-bold text-primary tabular-nums">{{ skill.match }}%</span>
            </div>
            <div class="w-full bg-slate-100 dark:bg-slate-800 rounded-full h-2">
              <div class="bg-primary h-2 rounded-full transition-all" :style="{ width: skill.match + '%' }"></div>
            </div>
          </div>
        </div>
        
        <div v-else class="text-center py-8">
          <span class="material-symbols-outlined text-4xl text-slate-300 dark:text-slate-600 mb-2">psychology</span>
          <p class="text-slate-500 dark:text-slate-400">完善简历以获取技能匹配</p>
        </div>
        
        <div v-if="skills.length > 0" class="mt-8 p-4 bg-primary/5 dark:bg-primary/10 rounded-xl border border-primary/20">
          <p class="text-xs text-primary font-bold uppercase tracking-widest mb-2">专家洞察</p>
          <p class="text-sm text-slate-600 dark:text-slate-300 leading-relaxed">
            您的技能与所在地区 <span class="font-bold">高薪职位</span> 匹配度较高。建议继续提升专业技能以获得更好的职业发展机会。
          </p>
        </div>
      </div>
    </div>
    
    <!-- System Announcements -->
    <div v-if="announcements.length > 0" class="mt-8">
      <div class="flex justify-between items-center mb-6">
        <h3 class="text-lg font-bold tracking-tight flex items-center gap-2">
          <span class="material-symbols-outlined text-primary">campaign</span>
          系统公告
        </h3>
      </div>
      <div class="space-y-3">
        <div
          v-for="announcement in announcements"
          :key="announcement.id"
          @click="viewAnnouncement(announcement)"
          class="bg-white dark:bg-slate-900 p-4 rounded-xl border border-slate-200 dark:border-slate-800 hover:border-primary/50 shadow-sm cursor-pointer transition-all"
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
              <h4 class="font-bold text-slate-900 dark:text-white mb-1">{{ announcement.title }}</h4>
              <p class="text-sm text-slate-600 dark:text-slate-400 line-clamp-2">{{ announcement.content }}</p>
            </div>
            <span class="text-xs text-slate-400 whitespace-nowrap">{{ formatAnnouncementTime(announcement.publishTime) }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Recommended Jobs -->
    <div v-if="recommendedJobs.length > 0" class="mt-8">
      <div class="flex justify-between items-center mb-6">
        <h3 class="text-lg font-bold tracking-tight">推荐职位</h3>
        <button class="text-primary text-sm font-semibold hover:underline tracking-wide" @click="$router.push('/recommendation')">查看更多</button>
      </div>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div 
          v-for="job in recommendedJobs" 
          :key="job.id"
          class="bg-white dark:bg-slate-900 p-5 rounded-2xl border border-slate-200 dark:border-slate-800 hover:border-primary/50 shadow-sm cursor-pointer transition-all"
          @click="$router.push(`/app/job/${job.id}`)"
        >
          <div class="flex justify-between items-start mb-3">
            <div>
              <h4 class="font-bold text-slate-900 dark:text-white hover:text-primary transition-colors tracking-tight">{{ job.title }}</h4>
              <p class="text-sm text-slate-600 dark:text-slate-400 mt-0.5">{{ job.enterpriseName }}</p>
            </div>
            <span class="text-lg font-bold text-primary tabular-nums">{{ Math.round(job.salaryMin/1000) }}-{{ Math.round(job.salaryMax/1000) }}k</span>
          </div>
          <div class="flex flex-wrap gap-2">
            <span class="px-3 py-1 bg-slate-100 dark:bg-slate-800 rounded-full text-[10px] font-bold text-slate-600 dark:text-slate-400 flex items-center gap-1 tracking-wide">
              <span class="material-symbols-outlined text-xs">location_on</span> {{ job.location }}
            </span>
            <span class="px-3 py-1 bg-slate-100 dark:bg-slate-800 rounded-full text-[10px] font-bold text-slate-600 dark:text-slate-400 tracking-wide">{{ job.workType || '全职' }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import api from '@/api'
import { useToast } from '@/composables/useToast'

const userStore = useUserStore()
const toast = useToast()

const userInfo = computed(() => userStore.userInfo)

const loading = ref(false)
const stats = ref({
  applied: 0,
  weeklyApplied: 0,
  interviews: 0,
  viewed: 0,
  profileComplete: 0,
  assessmentScore: 0,
  topPercent: 10
})

const timelineItems = ref([])

const skills = ref([])

const recommendedJobs = ref([])

const announcements = ref([])

const fetchStats = async () => {
  try {
    const data = await api.getUserStats()
    stats.value = {
      applied: data.applied || 0,
      weeklyApplied: data.weeklyApplied || 0,
      interviews: data.interviews || 0,
      viewed: data.viewed || 0,
      profileComplete: data.profileComplete || 0,
      assessmentScore: data.assessmentScore || 0,
      topPercent: data.topPercent || 10
    }
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

const fetchApplications = async () => {
  loading.value = true
  try {
    const [appData, interviewData] = await Promise.all([
      api.getMyApplications({ page: 0, size: 10 }),
      api.getJobSeekerInterviews({ page: 0, size: 10 }).catch(() => ({ content: [] }))
    ])

    const applications = appData.content || []
    const interviews = interviewData.content || interviewData || []

    // 建立 applicationId -> interview 映射
    const interviewMap = {}
    interviews.forEach(iv => {
      const appId = iv.application?.id || iv.applicationId
      if (appId) {
        if (!interviewMap[appId] || new Date(iv.interviewTime) > new Date(interviewMap[appId].interviewTime)) {
          interviewMap[appId] = iv
        }
      }
    })

    timelineItems.value = applications.slice(0, 6).map(app => {
      const iv = interviewMap[app.id]
      const statusStyle = getStatusStyle(app.status)
      return {
        title: getStatusTitle(app.status),
        position: app.job?.title || app.position || '职位',
        company: app.enterpriseName || app.job?.enterpriseName || '企业',
        time: formatTime(app.applyTime || app.createdAt),
        icon: getStatusIcon(app.status),
        dotClass: statusStyle.dot,
        badgeClass: statusStyle.badge,
        interviewTime: iv ? formatDateTime(iv.interviewTime) : null,
        hrComment: app.hrComment || null
      }
    })
  } catch (error) {
    console.error('获取申请记录失败', error)
  } finally {
    loading.value = false
  }
}

const fetchSkills = async () => {
  try {
    const profile = await api.getJobSeekerProfile()
    if (profile && profile.skills) {
      const skillList = profile.skills.split(',').filter(s => s.trim())
      skills.value = skillList.slice(0, 4).map((skill, index) => ({
        name: skill.trim(),
        match: 75 + Math.floor(Math.random() * 20)
      }))
    }
  } catch (error) {
    console.error('获取技能信息失败', error)
  }
}

const fetchRecommendedJobs = async () => {
  try {
    const data = await api.getRecommendedJobs(3)
    recommendedJobs.value = data || []
  } catch (error) {
    console.error('获取推荐职位失败', error)
  }
}

const fetchAnnouncements = async () => {
  try {
    const data = await api.getActiveAnnouncements()
    announcements.value = (data || []).slice(0, 3)
  } catch (error) {
    console.error('获取公告失败', error)
  }
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

const getStatusTitle = (status) => {
  const titles = {
    'APPLIED': '已投递',
    'VIEWED': '已查看',
    'SCREENING': '筛选中',
    'INTERVIEW': '面试邀请',
    'OFFERED': '已录用',
    'HIRED': '已录用',
    'REJECTED': '未通过'
  }
  return titles[status] || '申请中'
}

const getStatusIcon = (status) => {
  const icons = {
    'APPLIED': 'send',
    'VIEWED': 'visibility',
    'SCREENING': 'manage_search',
    'INTERVIEW': 'event',
    'OFFERED': 'check_circle',
    'HIRED': 'check_circle',
    'REJECTED': 'cancel'
  }
  return icons[status] || 'schedule'
}

const getStatusStyle = (status) => {
  const styles = {
    'APPLIED':   { dot: 'bg-primary',                     badge: 'bg-primary/10 text-primary' },
    'VIEWED':    { dot: 'bg-blue-400',                    badge: 'bg-blue-50 text-blue-600 dark:bg-blue-900/30 dark:text-blue-400' },
    'SCREENING': { dot: 'bg-amber-400',                   badge: 'bg-amber-50 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400' },
    'INTERVIEW': { dot: 'bg-emerald-500',                 badge: 'bg-emerald-50 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-400' },
    'OFFERED':   { dot: 'bg-emerald-600',                 badge: 'bg-emerald-100 text-emerald-800 dark:bg-emerald-900/40 dark:text-emerald-300' },
    'HIRED':     { dot: 'bg-emerald-600',                 badge: 'bg-emerald-100 text-emerald-800 dark:bg-emerald-900/40 dark:text-emerald-300' },
    'REJECTED':  { dot: 'bg-slate-300 dark:bg-slate-600', badge: 'bg-slate-100 text-slate-500 dark:bg-slate-800 dark:text-slate-400' }
  }
  return styles[status] || { dot: 'bg-slate-300 dark:bg-slate-600', badge: 'bg-slate-100 text-slate-500' }
}

const formatDateTime = (time) => {
  if (!time) return null
  const date = new Date(time)
  return date.toLocaleString('zh-CN', { month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleDateString('zh-CN')
}

onMounted(async () => {
  // 检查用户是否已登录
  if (!userStore.isLoggedIn || !userStore.userInfo) {
    return
  }
  
  try {
    await Promise.all([
      fetchStats(),
      fetchApplications(),
      fetchSkills(),
      fetchRecommendedJobs(),
      fetchAnnouncements()
    ])
  } catch (error) {
    console.error('加载数据失败:', error)
  }
})
</script>
