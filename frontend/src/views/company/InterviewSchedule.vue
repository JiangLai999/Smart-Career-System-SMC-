<template>
  <div class="flex-1 overflow-y-auto p-8 flex gap-8">
    <!-- Calendar View Section -->
    <div class="flex-1 space-y-6">
      <div class="flex items-center justify-between">
        <div class="flex items-center gap-4">
          <h3 class="text-lg font-bold">{{ currentMonth }}</h3>
          <div class="flex border border-primary/10 rounded-lg bg-white dark:bg-slate-900">
            <button @click="prevMonth" class="p-2 hover:bg-slate-50 dark:hover:bg-slate-800 border-r border-primary/10">
              <span class="material-symbols-outlined text-lg leading-none">chevron_left</span>
            </button>
            <button @click="nextMonth" class="p-2 hover:bg-slate-50 dark:hover:bg-slate-800">
              <span class="material-symbols-outlined text-lg leading-none">chevron_right</span>
            </button>
          </div>
          <button @click="goToToday" class="px-4 py-2 text-sm font-medium border border-primary/10 rounded-lg bg-white dark:bg-slate-900 hover:bg-slate-50">今天</button>
        </div>
        <div class="flex gap-2">
          <div class="flex bg-slate-100 dark:bg-slate-800 p-1 rounded-lg">
            <button @click="viewMode = 'month'" :class="['px-4 py-1.5 text-sm font-medium rounded-md', viewMode === 'month' ? 'bg-white dark:bg-slate-700 shadow-sm' : 'text-slate-500']">月视图</button>
            <button @click="viewMode = 'week'" :class="['px-4 py-1.5 text-sm font-medium rounded-md', viewMode === 'week' ? 'bg-white dark:bg-slate-700 shadow-sm' : 'text-slate-500']">周视图</button>
            <button @click="viewMode = 'day'" :class="['px-4 py-1.5 text-sm font-medium rounded-md', viewMode === 'day' ? 'bg-white dark:bg-slate-700 shadow-sm' : 'text-slate-500']">日视图</button>
          </div>
          <button @click="openScheduleModal" class="flex items-center gap-2 bg-primary text-white px-5 py-2 rounded-lg font-bold text-sm shadow-lg shadow-primary/20 hover:opacity-90 transition-opacity">
            <span class="material-symbols-outlined text-lg">add</span>
            安排新面试
          </button>
        </div>
      </div>
      
      <!-- Full Calendar Component -->
      <div class="bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-primary/10 overflow-hidden">
        <div class="calendar-grid border-b border-primary/10 bg-slate-50 dark:bg-slate-800/50">
          <div class="py-3 text-center text-xs font-bold text-slate-500 uppercase tracking-wider">周日</div>
          <div class="py-3 text-center text-xs font-bold text-slate-500 uppercase tracking-wider">周一</div>
          <div class="py-3 text-center text-xs font-bold text-slate-500 uppercase tracking-wider">周二</div>
          <div class="py-3 text-center text-xs font-bold text-slate-500 uppercase tracking-wider">周三</div>
          <div class="py-3 text-center text-xs font-bold text-slate-500 uppercase tracking-wider">周四</div>
          <div class="py-3 text-center text-xs font-bold text-slate-500 uppercase tracking-wider">周五</div>
          <div class="py-3 text-center text-xs font-bold text-slate-500 uppercase tracking-wider">周六</div>
        </div>
        <div class="calendar-grid auto-rows-[120px]">
          <div v-for="(day, index) in calendarDays" :key="index" :class="['border-r border-b border-primary/5 p-2', day.isCurrentMonth ? '' : 'bg-slate-50/50 dark:bg-slate-800/20 text-slate-400', day.isToday ? 'bg-primary/5' : '']">
            <div class="flex justify-between items-start mb-1">
              <span :class="['text-sm font-bold', day.isToday ? 'text-primary' : '']">{{ day.date }}</span>
              <span v-if="day.isToday" class="size-1.5 bg-primary rounded-full"></span>
            </div>
            <div class="space-y-1">
              <div 
                v-for="interview in day.interviews" 
                :key="interview.id"
                :class="['px-2 py-0.5 rounded text-[10px] font-bold truncate cursor-pointer', getInterviewClass(interview.type)]"
                @click="viewInterview(interview)"
              >
                {{ interview.time }} - {{ interview.candidateName }}
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Legend -->
      <div class="flex items-center gap-6 pt-2 flex-wrap">
        <div class="flex items-center gap-2">
          <span class="size-3 rounded-full bg-blue-100 border border-blue-400"></span>
          <span class="text-xs font-medium text-slate-500">初试 (First Round)</span>
        </div>
        <div class="flex items-center gap-2">
          <span class="size-3 rounded-full bg-green-100 border border-green-400"></span>
          <span class="text-xs font-medium text-slate-500">技术评测 (Technical)</span>
        </div>
        <div class="flex items-center gap-2">
          <span class="size-3 rounded-full bg-purple-100 border border-purple-400"></span>
          <span class="text-xs font-medium text-slate-500">终审面试 (Final)</span>
        </div>
        <div class="flex items-center gap-2">
          <span class="size-3 rounded-full bg-orange-100 border border-orange-400"></span>
          <span class="text-xs font-medium text-slate-500">现场面试 (Onsite)</span>
        </div>
        <div class="flex items-center gap-2">
          <span class="size-3 rounded-full bg-cyan-100 border border-cyan-400"></span>
          <span class="text-xs font-medium text-slate-500">电话面试 (Phone)</span>
        </div>
        <div class="flex items-center gap-2">
          <span class="size-3 rounded-full bg-pink-100 border border-pink-400"></span>
          <span class="text-xs font-medium text-slate-500">视频面试 (Video)</span>
        </div>
      </div>
    </div>
    
    <!-- Side Panel: Interviews for Today -->
    <div class="w-80 flex flex-col gap-6">
      <div class="bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-primary/10 p-6">
        <div class="flex items-center justify-between mb-6">
          <h3 class="font-bold text-lg">今日面试安排</h3>
          <span class="bg-primary/10 text-primary text-xs font-bold px-2.5 py-1 rounded-full">{{ todayInterviews.length }} 场</span>
        </div>
        <div class="space-y-4">
          <div v-for="interview in todayInterviews" :key="interview.id" class="relative pl-6 pb-6 border-l-2 border-primary/20">
            <div class="absolute -left-[9px] top-0 size-4 bg-white dark:bg-slate-900 border-2 rounded-full" :class="getInterviewBorderClass(interview.type)"></div>
            <div :class="['text-sm font-bold mb-1', getInterviewTextClass(interview.type)]">{{ interview.time }}</div>
            <div class="bg-background-light dark:bg-slate-800 p-4 rounded-lg group hover:ring-1 hover:ring-primary transition-all">
              <h4 class="font-bold text-sm mb-1 group-hover:text-primary transition-colors">{{ interview.candidateName }}</h4>
              <p class="text-xs text-slate-500 mb-3">{{ interview.position }}</p>
              <div class="flex items-center justify-between">
                <span :class="['text-[10px] font-bold px-2 py-0.5 rounded', getInterviewBadgeClass(interview.type)]">{{ interview.typeText }}</span>
                <button @click="viewInterview(interview)" class="text-primary text-[10px] font-bold flex items-center gap-1">
                  详情 <span class="material-symbols-outlined text-xs">arrow_forward</span>
                </button>
              </div>
            </div>
          </div>
          
          <div v-if="todayInterviews.length === 0" class="text-center py-8 text-slate-400">
            <span class="material-symbols-outlined text-4xl mb-2">event_busy</span>
            <p class="text-sm">今日无面试安排</p>
          </div>
        </div>
        <button @click="viewMode = 'day'; currentDate = new Date()" class="w-full mt-8 py-3 text-sm font-bold text-slate-600 dark:text-slate-400 border-t border-primary/10 flex items-center justify-center gap-2 hover:text-primary transition-colors">
          查看全天日程 <span class="material-symbols-outlined text-sm">expand_more</span>
        </button>
      </div>
      
      <!-- Quick Stats -->
      <div class="bg-gradient-to-br from-primary to-blue-600 rounded-xl p-6 text-white shadow-xl shadow-primary/20">
        <h4 class="text-sm font-bold mb-4 flex items-center gap-2">
          <span class="material-symbols-outlined text-base">analytics</span>
          本月概览
        </h4>
        <div class="grid grid-cols-2 gap-4">
          <div>
            <p class="text-xs text-white/70">总面试</p>
            <p class="text-2xl font-bold">{{ stats.total }}</p>
          </div>
          <div>
            <p class="text-xs text-white/70">已录用</p>
            <p class="text-2xl font-bold">{{ stats.hired }}</p>
          </div>
          <div>
            <p class="text-xs text-white/70">待安排</p>
            <p class="text-2xl font-bold">{{ stats.pending }}</p>
          </div>
          <div>
            <p class="text-xs text-white/70">转化率</p>
            <p class="text-2xl font-bold">{{ stats.conversionRate }}%</p>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Schedule Modal -->
    <div v-if="showScheduleModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50" @click.self="closeScheduleModal">
      <div class="bg-white dark:bg-slate-900 rounded-xl shadow-xl w-full max-w-lg mx-4 max-h-[90vh] overflow-y-auto">
        <div class="p-6 border-b border-slate-200 dark:border-slate-800">
          <div class="flex items-center justify-between">
            <h3 class="text-lg font-bold">安排新面试</h3>
            <button @click="closeScheduleModal" class="p-1 hover:bg-slate-100 dark:hover:bg-slate-800 rounded">
              <span class="material-symbols-outlined">close</span>
            </button>
          </div>
        </div>
        <div class="p-6 space-y-4">
          <div>
            <label class="block text-sm font-medium mb-2">选择候选人 <span class="text-red-500">*</span></label>
            <select v-model="scheduleForm.applicationId" class="w-full px-4 py-2 border border-slate-200 dark:border-slate-700 rounded-lg bg-white dark:bg-slate-800">
              <option value="">请选择候选人</option>
              <option v-for="app in pendingApplications" :key="app.applicationId || app.id" :value="app.applicationId || app.id">
                {{ app.userName || '求职者' }} - {{ app.jobTitle || '职位' }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium mb-2">面试时间 <span class="text-red-500">*</span></label>
            <input v-model="scheduleForm.interviewTime" type="datetime-local" class="w-full px-4 py-2 border border-slate-200 dark:border-slate-700 rounded-lg bg-white dark:bg-slate-800" />
          </div>
          <div>
            <label class="block text-sm font-medium mb-2">面试地点 <span class="text-red-500">*</span></label>
            <input v-model="scheduleForm.location" type="text" placeholder="例如：公司会议室A或线上链接" class="w-full px-4 py-2 border border-slate-200 dark:border-slate-700 rounded-lg bg-white dark:bg-slate-800" />
          </div>
          <div>
            <label class="block text-sm font-medium mb-2">面试类型</label>
            <select v-model="scheduleForm.interviewType" class="w-full px-4 py-2 border border-slate-200 dark:border-slate-700 rounded-lg bg-white dark:bg-slate-800">
              <option value="FIRST_ROUND">初试</option>
              <option value="TECHNICAL">技术评测</option>
              <option value="FINAL">终审面试</option>
              <option value="ONSITE">现场面试</option>
              <option value="PHONE">电话面试</option>
              <option value="VIDEO">视频面试</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium mb-2">面试官</label>
            <input v-model="scheduleForm.interviewer" type="text" placeholder="面试官姓名" class="w-full px-4 py-2 border border-slate-200 dark:border-slate-700 rounded-lg bg-white dark:bg-slate-800" />
          </div>
          <div>
            <label class="block text-sm font-medium mb-2">联系电话</label>
            <input v-model="scheduleForm.contactPhone" type="text" placeholder="联系人电话" class="w-full px-4 py-2 border border-slate-200 dark:border-slate-700 rounded-lg bg-white dark:bg-slate-800" />
          </div>
          <div>
            <label class="block text-sm font-medium mb-2">备注</label>
            <textarea v-model="scheduleForm.notes" placeholder="其他需要说明的信息" rows="3" class="w-full px-4 py-2 border border-slate-200 dark:border-slate-700 rounded-lg bg-white dark:bg-slate-800"></textarea>
          </div>
        </div>
        <div class="p-6 border-t border-slate-200 dark:border-slate-800 flex justify-end gap-3">
          <button @click="closeScheduleModal" class="px-4 py-2 border border-slate-200 dark:border-slate-700 rounded-lg hover:bg-slate-50 dark:hover:bg-slate-800">取消</button>
          <button @click="submitSchedule" class="px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary/90">确认安排</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import api from '@/api'

const router = useRouter()
const route = useRoute()

const currentDate = ref(new Date())
const showScheduleModal = ref(false)
const interviews = ref([])
const loading = ref(false)
const viewMode = ref('month')

const stats = ref({
  total: 0,
  hired: 0,
  pending: 0,
  conversionRate: 0
})

const currentMonth = computed(() => {
  return currentDate.value.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long' })
})

const getTypeText = (type) => {
  const map = {
    'FIRST_ROUND': '初试',
    'TECHNICAL': '技术评测',
    'FINAL': '终审面试',
    'ONSITE': '现场面试',
    'PHONE': '电话面试',
    'VIDEO': '视频面试'
  }
  return map[type] || '面试'
}

const todayInterviews = computed(() => {
  const today = new Date()
  return interviews.value.filter(i => {
    const interviewDate = new Date(i.interviewTime)
    return interviewDate.toDateString() === today.toDateString()
  }).map(i => ({
    ...i,
    type: i.interviewType || i.type || 'FIRST_ROUND',
    time: new Date(i.interviewTime).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }),
    candidateName: i.candidateName || i.jobSeekerName || '求职者',
    position: i.jobTitle || '职位',
    typeText: getTypeText(i.interviewType || i.type || 'FIRST_ROUND')
  }))
})

onMounted(async () => {
  await Promise.all([fetchInterviews(), fetchPendingApplications(), fetchStats()])
  // 若从简历详情页跳转过来，预填 applicationId 并自动打开弹窗
  if (route.query.applicationId) {
    scheduleForm.value.applicationId = Number(route.query.applicationId)
    showScheduleModal.value = true
  }
})

const fetchInterviews = async () => {
  loading.value = true
  try {
    const data = await api.getEnterpriseInterviews({ page: 0, size: 200 })
    interviews.value = data.content || data || []
  } catch (error) {
    console.error('获取面试列表失败', error)
    interviews.value = []
  } finally {
    loading.value = false
  }
}

const calendarDays = computed(() => {
  const year = currentDate.value.getFullYear()
  const month = currentDate.value.getMonth()
  const firstDay = new Date(year, month, 1)
  const lastDay = new Date(year, month + 1, 0)

  const days = []

  const firstDayOfWeek = firstDay.getDay()
  for (let i = firstDayOfWeek - 1; i >= 0; i--) {
    const prevDate = new Date(year, month, 0 - i)
    days.push({
      date: prevDate.getDate(),
      fullDate: prevDate,
      isCurrentMonth: false,
      isToday: false,
      interviews: []
    })
  }

  const today = new Date()
  for (let i = 1; i <= lastDay.getDate(); i++) {
    const currentDateStr = new Date(year, month, i).toDateString()
    const isToday = today.getFullYear() === year && today.getMonth() === month && today.getDate() === i
    
    const dayInterviews = interviews.value.filter(interview => {
      const interviewDate = new Date(interview.interviewTime)
      return interviewDate.getFullYear() === year && 
             interviewDate.getMonth() === month && 
             interviewDate.getDate() === i
    }).map(i => ({
      ...i,
      type: i.interviewType || i.type || 'FIRST_ROUND',
      time: new Date(i.interviewTime).toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }),
      candidateName: i.candidateName || i.jobSeekerName || '求职者'
    }))

    days.push({
      date: i,
      fullDate: new Date(year, month, i),
      isCurrentMonth: true,
      isToday,
      interviews: dayInterviews
    })
  }

  const remaining = 42 - days.length
  for (let i = 1; i <= remaining; i++) {
    days.push({
      date: i,
      fullDate: new Date(year, month + 1, i),
      isCurrentMonth: false,
      isToday: false,
      interviews: []
    })
  }

  return days
})

const prevMonth = () => {
  currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() - 1, 1)
}

const nextMonth = () => {
  currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() + 1, 1)
}

const goToToday = () => {
  currentDate.value = new Date()
}

const getInterviewClass = (type) => {
  const map = {
    'FIRST_ROUND': 'bg-blue-100 dark:bg-blue-900/40 text-blue-700 dark:text-blue-300',
    'TECHNICAL': 'bg-green-100 dark:bg-green-900/40 text-green-700 dark:text-green-300',
    'FINAL': 'bg-purple-100 dark:bg-purple-900/40 text-purple-700 dark:text-purple-300',
    'ONSITE': 'bg-orange-100 dark:bg-orange-900/40 text-orange-700 dark:text-orange-300',
    'PHONE': 'bg-cyan-100 dark:bg-cyan-900/40 text-cyan-700 dark:text-cyan-300',
    'VIDEO': 'bg-pink-100 dark:bg-pink-900/40 text-pink-700 dark:text-pink-300'
  }
  return map[type] || 'bg-slate-100 dark:bg-slate-800/40 text-slate-700 dark:text-slate-300'
}

const getInterviewTextClass = (type) => {
  const map = {
    'FIRST_ROUND': 'text-blue-600',
    'TECHNICAL': 'text-green-600',
    'FINAL': 'text-purple-600',
    'ONSITE': 'text-orange-600',
    'PHONE': 'text-cyan-600',
    'VIDEO': 'text-pink-600'
  }
  return map[type] || 'text-slate-600'
}

const getInterviewBorderClass = (type) => {
  const map = {
    'FIRST_ROUND': 'border-primary',
    'TECHNICAL': 'border-green-500',
    'FINAL': 'border-purple-500',
    'ONSITE': 'border-orange-500',
    'PHONE': 'border-cyan-500',
    'VIDEO': 'border-pink-500'
  }
  return map[type] || 'border-slate-300'
}

const getInterviewBadgeClass = (type) => {
  const map = {
    'FIRST_ROUND': 'bg-blue-100 text-blue-700',
    'TECHNICAL': 'bg-green-100 text-green-700',
    'FINAL': 'bg-purple-100 text-purple-700',
    'ONSITE': 'bg-orange-100 text-orange-700',
    'PHONE': 'bg-cyan-100 text-cyan-700',
    'VIDEO': 'bg-pink-100 text-pink-700'
  }
  return map[type] || 'bg-slate-100 text-slate-600'
}

const viewInterview = (interview) => {
  router.push(`/company/interview/feedback/${interview.id}`)
}

const openScheduleModal = () => {
  showScheduleModal.value = true
}

const closeScheduleModal = () => {
  showScheduleModal.value = false
  scheduleForm.value = {
    applicationId: null,
    interviewTime: '',
    location: '',
    interviewType: 'FIRST_ROUND',
    interviewer: '',
    contactPhone: '',
    notes: ''
  }
}

const scheduleForm = ref({
  applicationId: null,
  interviewTime: '',
  location: '',
  interviewType: 'FIRST_ROUND',
  interviewer: '',
  contactPhone: '',
  notes: ''
})

const pendingApplications = ref([])

const fetchPendingApplications = async () => {
  try {
    const data = await api.getCompanyApplications({ page: 0, pageSize: 100 })
    const all = data.content || data || []
    // 排除已录用、已拒绝的终态
    pendingApplications.value = all.filter(app =>
      !['HIRED', 'OFFERED', 'REJECTED'].includes(app.status)
    )
  } catch (error) {
    console.error('获取待面试申请失败', error)
    pendingApplications.value = []
  }
}

const submitSchedule = async () => {
  if (!scheduleForm.value.applicationId || !scheduleForm.value.interviewTime || !scheduleForm.value.location) {
    alert('请填写必填信息')
    return
  }
  
  try {
    await api.createInterview({
      applicationId: scheduleForm.value.applicationId,
      interviewTime: scheduleForm.value.interviewTime,
      location: scheduleForm.value.location,
      interviewType: scheduleForm.value.interviewType,
      interviewer: scheduleForm.value.interviewer,
      contactPhone: scheduleForm.value.contactPhone,
      notes: scheduleForm.value.notes
    })
    alert('面试安排成功')
    closeScheduleModal()
    fetchInterviews()
    fetchStats()
  } catch (error) {
    console.error('安排面试失败', error)
    alert(error.response?.data?.message || '安排面试失败')
  }
}

const fetchStats = async () => {
  try {
    const data = await api.getInterviewStatistics()
    stats.value = {
      total: data.totalCount || 0,
      hired: data.passCount || 0,
      pending: (data.pendingCount || 0) + (data.confirmedCount || 0),
      conversionRate: data.totalCount > 0 ? Math.round(((data.passCount || 0) / data.totalCount) * 100) : 0
    }
  } catch (error) {
    console.error('获取统计失败', error)
  }
}
</script>

<style scoped>
.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
}
.bg-background-light {
  background-color: #f6f7f8;
}
</style>
