<template>
  <div class="max-w-4xl mx-auto">
    <div class="mb-6">
      <button @click="goBack" class="flex items-center gap-2 text-slate-600 hover:text-slate-900 transition-colors">
        <span class="material-symbols-outlined">arrow_back</span>
        <span class="font-medium">返回列表</span>
      </button>
    </div>

    <div v-if="loading" class="text-center py-12">
      <span class="material-symbols-outlined text-4xl text-slate-300 animate-spin">sync</span>
      <p class="text-slate-500 mt-2">加载中...</p>
    </div>

    <div v-else-if="error" class="text-center py-12">
      <span class="material-symbols-outlined text-5xl text-red-300 mb-4">error</span>
      <p class="text-red-500">{{ error }}</p>
      <button @click="goBack" class="mt-4 px-4 py-2 bg-slate-100 rounded-lg text-sm font-medium hover:bg-slate-200">返回列表</button>
    </div>

    <div v-else class="space-y-6">
      <div class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6">
        <div class="flex items-start justify-between">
          <div class="flex items-center gap-4">
            <div class="w-16 h-16 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-full flex items-center justify-center text-white text-2xl font-bold">
              {{ resume.userName?.charAt(0) || 'U' }}
            </div>
            <div>
              <h1 class="text-2xl font-bold text-slate-900 dark:text-white">{{ resume.userName || '求职者' }}</h1>
              <p class="text-slate-500 mt-1">{{ resume.education || '学历待定' }} · {{ resume.experience || '经验不限' }}</p>
            </div>
          </div>
          <div class="flex items-center gap-2">
            <span :class="['px-3 py-1 rounded-full text-sm font-medium', getStatusClass(application.status)]">
              {{ getStatusText(application.status) }}
            </span>
          </div>
        </div>

        <div class="mt-6 grid grid-cols-2 md:grid-cols-4 gap-4">
          <div class="bg-slate-50 dark:bg-slate-800 rounded-lg p-4">
            <p class="text-xs text-slate-500 font-medium">应聘职位</p>
            <p class="text-sm font-bold mt-1">{{ application.jobTitle || '-' }}</p>
          </div>
          <div class="bg-slate-50 dark:bg-slate-800 rounded-lg p-4">
            <p class="text-xs text-slate-500 font-medium">投递时间</p>
            <p class="text-sm font-bold mt-1">{{ formatDate(application.applyTime) }}</p>
          </div>
          <div class="bg-slate-50 dark:bg-slate-800 rounded-lg p-4">
            <p class="text-xs text-slate-500 font-medium">联系电话</p>
            <p class="text-sm font-bold mt-1">{{ resume.phone || '-' }}</p>
          </div>
          <div class="bg-slate-50 dark:bg-slate-800 rounded-lg p-4">
            <p class="text-xs text-slate-500 font-medium">电子邮箱</p>
            <p class="text-sm font-bold mt-1">{{ resume.email || '-' }}</p>
          </div>
        </div>
      </div>

      <div class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6">
        <h2 class="text-lg font-bold mb-4">技能标签</h2>
        <div v-if="resume.skills && resume.skills.length > 0" class="flex flex-wrap gap-2">
          <span v-for="skill in resume.skills" :key="skill" class="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-sm font-medium">
            {{ skill }}
          </span>
        </div>
        <p v-else class="text-slate-500">暂无技能信息</p>
      </div>

      <div class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6">
        <h2 class="text-lg font-bold mb-4">个人简介</h2>
        <p class="text-slate-600 dark:text-slate-400 whitespace-pre-line">{{ resume.description || '暂无个人简介' }}</p>
      </div>

      <div v-if="resume.workExperience && resume.workExperience.length > 0" class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6">
        <h2 class="text-lg font-bold mb-4">工作经历</h2>
        <div class="space-y-4">
          <div v-for="exp in resume.workExperience" :key="exp.id" class="border-l-2 border-blue-500 pl-4">
            <div class="flex items-center justify-between">
              <h3 class="font-bold">{{ exp.company }}</h3>
              <span class="text-sm text-slate-500">{{ exp.startTime }} - {{ exp.endTime || '至今' }}</span>
            </div>
            <p class="text-slate-600 dark:text-slate-400">{{ exp.position }}</p>
            <p v-if="exp.description" class="text-sm text-slate-500 mt-2">{{ exp.description }}</p>
          </div>
        </div>
      </div>

      <div v-if="resume.educationHistory && resume.educationHistory.length > 0" class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6">
        <h2 class="text-lg font-bold mb-4">教育经历</h2>
        <div class="space-y-4">
          <div v-for="edu in resume.educationHistory" :key="edu.id" class="border-l-2 border-green-500 pl-4">
            <div class="flex items-center justify-between">
              <h3 class="font-bold">{{ edu.school }}</h3>
              <span class="text-sm text-slate-500">{{ edu.startTime }} - {{ edu.endTime || '至今' }}</span>
            </div>
            <p class="text-slate-600 dark:text-slate-400">{{ edu.major }} · {{ edu.degree }}</p>
          </div>
        </div>
      </div>

      <div class="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 p-6">
        <h2 class="text-lg font-bold mb-4">操作</h2>
        <div class="flex flex-wrap gap-3">
          <button @click="updateStatus('REVIEWING')" class="px-4 py-2 bg-blue-600 text-white rounded-lg text-sm font-medium hover:bg-blue-700 transition-colors">
            标记为查看中
          </button>
          <button @click="goScheduleInterview" class="px-4 py-2 bg-purple-600 text-white rounded-lg text-sm font-medium hover:bg-purple-700 transition-colors">
            安排面试
          </button>
          <button @click="updateStatus('OFFERED')" class="px-4 py-2 bg-green-600 text-white rounded-lg text-sm font-medium hover:bg-green-700 transition-colors">
            确认录用
          </button>
          <button @click="updateStatus('REJECTED')" class="px-4 py-2 bg-red-600 text-white rounded-lg text-sm font-medium hover:bg-red-700 transition-colors">
            拒绝
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const error = ref('')
const resume = ref({})
const application = ref({})

onMounted(async () => {
  const id = route.params.id
  if (!id) {
    error.value = '缺少简历ID'
    loading.value = false
    return
  }

  try {
    const data = await api.getResumeById(id)
    resume.value = data || {}
    application.value = {
      id: id,
      status: data.status || 'PENDING',
      jobTitle: data.jobTitle || '',
      applyTime: data.applyTime || null
    }
  } catch (err) {
    console.error('获取简历详情失败', err)
    error.value = err.response?.data?.message || err.message || '获取简历详情失败'
  } finally {
    loading.value = false
  }
})

const goBack = () => {
  router.push('/company/applications')
}

const goScheduleInterview = () => {
  // 跳转到面试管理页，携带 applicationId 以便预填表单
  const appId = route.params.id
  router.push({ path: '/company/interviews', query: { applicationId: appId } })
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const getStatusClass = (status) => {
  const map = {
    'PENDING': 'bg-yellow-100 text-yellow-700',
    'APPLIED': 'bg-yellow-100 text-yellow-700',
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
    'APPLIED': '已投递',
    'REVIEWING': '查看中',
    'INTERVIEW': '面试中',
    'REJECTED': '已拒绝',
    'HIRED': '已录用'
  }
  return map[status] || status
}

const updateStatus = async (status) => {
  const id = route.params.id
  try {
    await api.updateApplicationStatus(id, status)
    application.value.status = status
    alert('状态更新成功')
  } catch (err) {
    console.error('更新状态失败', err)
    alert(err.response?.data?.message || '更新状态失败')
  }
}
</script>
