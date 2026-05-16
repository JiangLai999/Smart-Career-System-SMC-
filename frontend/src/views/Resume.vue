<template>
  <div>
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
      <div>
        <h2 class="text-3xl font-black text-slate-900 dark:text-slate-100 tracking-tight">我的简历</h2>
        <p class="text-slate-500 dark:text-slate-400 mt-1">管理您的在线简历和附件，提升求职竞争力</p>
      </div>
      <button 
        @click="$router.push('/app/profile')"
        class="bg-primary hover:bg-primary/90 text-white px-6 py-2.5 rounded-lg font-bold text-sm transition-all shadow-md flex items-center gap-2 w-fit"
      >
        <span class="material-symbols-outlined text-[20px]">edit</span>
        编辑简历
      </button>
    </div>
    
    <!-- Primary Resume Section -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
      <div class="lg:col-span-2 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-xl overflow-hidden shadow-sm">
        <div class="p-4 border-b border-slate-100 dark:border-slate-800 flex justify-between items-center">
          <h3 class="font-bold text-slate-900 dark:text-slate-100 flex items-center gap-2">
            <span class="material-symbols-outlined text-primary">verified</span>
            主简历
          </h3>
          <span class="text-xs bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400 px-2 py-0.5 rounded-full font-medium">公开中</span>
        </div>
        <div class="p-6 flex flex-col sm:flex-row gap-6">
          <div class="w-full sm:w-32 aspect-[3/4] bg-slate-100 dark:bg-slate-800 rounded-lg border border-slate-200 dark:border-slate-700 flex items-center justify-center relative group overflow-hidden cursor-pointer">
            <div class="size-16 rounded-full bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white text-2xl font-bold">
              {{ (profile.realName || userInfo?.username || 'U')?.charAt(0) }}
            </div>
          </div>
          <div class="flex-1 flex flex-col justify-between">
            <div class="space-y-3">
              <p class="text-xl font-bold text-slate-900 dark:text-slate-100 leading-tight">
                {{ profile.realName || userInfo?.username || '用户' }} 的个人简历
              </p>
              <div class="flex flex-wrap gap-2 text-sm text-slate-500 dark:text-slate-400">
                <span v-if="profile.workYears">{{ profile.workYears }}年工作经验</span>
                <span v-if="profile.workYears && profile.education">|</span>
                <span v-if="profile.education">{{ profile.education }}</span>
                <span v-if="(profile.workYears || profile.education) && profile.expectedLocation">|</span>
                <span v-if="profile.expectedLocation">{{ profile.expectedLocation }}</span>
              </div>
              <p v-if="profile.resume" class="text-sm text-slate-600 dark:text-slate-400 line-clamp-2 mt-2 leading-relaxed">
                {{ profile.resume }}
              </p>
              <p v-else class="text-sm text-slate-400 dark:text-slate-500 mt-2 italic">
                暂无个人简介，请完善您的简历信息
              </p>
            </div>
            <div class="flex items-center justify-between mt-6 pt-6 border-t border-slate-100 dark:border-slate-800">
              <span class="text-xs text-slate-400">最后更新: {{ lastUpdate }}</span>
              <button 
                class="bg-primary/10 hover:bg-primary text-primary hover:text-white px-4 py-1.5 rounded-lg text-sm font-bold transition-all"
                @click="$router.push('/app/profile')"
              >
                编辑
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Strength Widget -->
      <div class="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-xl p-6 shadow-sm">
        <h3 class="font-bold text-slate-900 dark:text-slate-100 mb-4">简历完整度</h3>
        <div class="flex items-center justify-center mb-4">
          <div class="relative w-32 h-32">
            <svg class="w-full h-full" viewBox="0 0 100 100">
              <circle cx="50" cy="50" r="45" fill="none" stroke="#e2e8f0" stroke-width="8"/>
              <circle cx="50" cy="50" r="45" fill="none" stroke="#137fec" stroke-width="8" 
                :stroke-dasharray="283" :stroke-dashoffset="283 - (283 * resumeStrength / 100)" 
                transform="rotate(-90 50 50)" stroke-linecap="round"/>
            </svg>
            <div class="absolute inset-0 flex items-center justify-center">
              <span class="text-3xl font-bold text-primary">{{ resumeStrength }}%</span>
            </div>
          </div>
        </div>
        <div class="space-y-2">
          <div v-for="item in resumeItems" :key="item.name" class="flex items-center justify-between text-sm">
            <span class="text-slate-600 dark:text-slate-400">{{ item.name }}</span>
            <span :class="item.done ? 'text-green-500' : 'text-slate-400'">
              <span class="material-symbols-outlined text-sm">{{ item.done ? 'check_circle' : 'radio_button_unchecked' }}</span>
            </span>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Skills Section -->
    <div v-if="profile.skills" class="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-xl overflow-hidden shadow-sm mb-8">
      <div class="p-4 border-b border-slate-100 dark:border-slate-800">
        <h3 class="font-bold text-slate-900 dark:text-slate-100">技能特长</h3>
      </div>
      <div class="p-6">
        <div class="flex flex-wrap gap-2">
          <span 
            v-for="(skill, index) in skillList" 
            :key="index"
            class="px-4 py-2 bg-primary/10 text-primary rounded-full text-sm font-medium"
          >
            {{ skill }}
          </span>
        </div>
      </div>
    </div>
    
    <!-- Other Resumes -->
    <div class="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 rounded-xl overflow-hidden shadow-sm">
      <div class="p-4 border-b border-slate-100 dark:border-slate-800">
        <h3 class="font-bold text-slate-900 dark:text-slate-100">其他简历</h3>
      </div>
      <div class="p-6">
        <div class="text-center py-8">
          <span class="material-symbols-outlined text-4xl text-slate-300 dark:text-slate-600 mb-2">description</span>
          <p class="text-slate-500 dark:text-slate-400">暂无其他简历</p>
          <button 
            class="mt-4 px-6 py-2 bg-primary text-white rounded-xl font-bold text-sm"
            @click="$router.push('/app/profile')"
          >
            完善主简历
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/user'
import api from '@/api'

const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)

const loading = ref(false)
const profile = ref({
  realName: '',
  workYears: null,
  education: '',
  expectedLocation: '',
  skills: '',
  resume: ''
})

const lastUpdate = ref('')

const skillList = computed(() => {
  if (!profile.value.skills) return []
  return profile.value.skills.split(',').map(s => s.trim()).filter(s => s)
})

const resumeStrength = computed(() => {
  let score = 0
  if (profile.value.realName) score += 15
  if (userInfo.value?.phone || profile.value.phone) score += 10
  if (userInfo.value?.email || profile.value.email) score += 10
  if (profile.value.education) score += 15
  if (profile.value.school) score += 10
  if (profile.value.skills) score += 20
  if (profile.value.resume) score += 10
  if (profile.value.expectedPosition) score += 5
  if (profile.value.expectedLocation) score += 5
  return score
})

const resumeItems = computed(() => [
  { name: '基本信息', done: !!(profile.value.realName && (userInfo.value?.phone || profile.value.phone)) },
  { name: '教育背景', done: !!(profile.value.education && profile.value.school) },
  { name: '工作经历', done: !!profile.value.workYears },
  { name: '技能特长', done: !!profile.value.skills },
  { name: '个人简介', done: !!profile.value.resume },
  { name: '求职意向', done: !!(profile.value.expectedPosition && profile.value.expectedLocation) }
])

const fetchProfile = async () => {
  loading.value = true
  try {
    const data = await api.getJobSeekerProfile()
    profile.value = {
      realName: data.realName || '',
      workYears: data.workYears || null,
      education: data.education || '',
      expectedLocation: data.expectedLocation || '',
      skills: data.skills || '',
      resume: data.resume || '',
      phone: data.phone || '',
      email: data.email || '',
      school: data.school || '',
      expectedPosition: data.expectedPosition || ''
    }
    
    if (data.updateTime) {
      lastUpdate.value = new Date(data.updateTime).toLocaleDateString('zh-CN')
    } else if (data.createTime) {
      lastUpdate.value = new Date(data.createTime).toLocaleDateString('zh-CN')
    } else {
      lastUpdate.value = new Date().toLocaleDateString('zh-CN')
    }
  } catch (error) {
    console.error('获取简历信息失败', error)
    lastUpdate.value = new Date().toLocaleDateString('zh-CN')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchProfile()
})
</script>
