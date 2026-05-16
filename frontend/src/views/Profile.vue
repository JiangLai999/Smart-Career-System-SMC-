<template>
  <div>
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
      <div>
        <h2 class="text-3xl font-extrabold text-slate-900 dark:text-slate-100 tracking-tight">个人中心</h2>
        <p class="text-slate-500 dark:text-slate-400 mt-2 text-base font-normal">管理您的个人信息和账号设置</p>
      </div>
    </div>
    
    <!-- Profile Content -->
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- Left - Avatar & Basic Info -->
      <div class="lg:col-span-1">
        <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 text-center">
          <div class="w-24 h-24 mx-auto rounded-full bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white text-3xl font-bold mb-4">
            {{ profile.realName?.charAt(0)?.toUpperCase() || userInfo?.username?.charAt(0)?.toUpperCase() || 'U' }}
          </div>
          <h3 class="text-xl font-bold text-slate-900 dark:text-white tracking-tight">{{ profile.realName || userInfo?.username }}</h3>
          <p class="text-slate-500 dark:text-slate-400 text-sm font-medium mt-1">{{ getMemberType() }}</p>
          
          <div class="mt-6 space-y-3">
            <div class="flex items-center justify-center gap-2 text-sm text-slate-600 dark:text-slate-400">
              <span class="material-symbols-outlined text-lg">email</span>
              {{ profile.email || userInfo?.email || '未设置' }}
            </div>
            <div class="flex items-center justify-center gap-2 text-sm text-slate-600 dark:text-slate-400">
              <span class="material-symbols-outlined text-lg">phone</span>
              {{ profile.phone || userInfo?.phone || '未设置' }}
            </div>
          </div>
          
          <button 
            class="w-full mt-6 py-2.5 bg-primary/10 text-primary rounded-xl font-bold text-sm hover:bg-primary hover:text-white transition-colors tracking-wide"
            @click="activeTab = 'basic'"
          >
            编辑资料
          </button>
        </div>
      </div>
      
      <!-- Right - Settings -->
      <div class="lg:col-span-2">
        <!-- Tabs -->
        <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 overflow-hidden">
          <div class="flex border-b border-slate-200 dark:border-slate-800">
            <button 
              v-for="tab in tabs" 
              :key="tab.id"
              :class="[
                'flex-1 py-4 text-sm font-bold transition-colors tracking-wide',
                activeTab === tab.id 
                  ? 'text-primary border-b-2 border-primary' 
                  : 'text-slate-500 dark:text-slate-400 hover:text-slate-700'
              ]"
              @click="activeTab = tab.id"
            >
              {{ tab.label }}
            </button>
          </div>
          
          <!-- Basic Info Tab -->
          <div v-if="activeTab === 'basic'" class="p-6 space-y-6">
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2 tracking-wide">真实姓名</label>
              <input v-model="profile.realName" type="text" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm font-medium" placeholder="请输入真实姓名" />
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2 tracking-wide">性别</label>
              <div class="flex gap-4">
                <label class="flex items-center gap-2 cursor-pointer">
                  <input v-model="profile.gender" type="radio" value="男" class="text-primary focus:ring-primary" />
                  <span class="text-sm text-slate-700 dark:text-slate-300 font-medium">男</span>
                </label>
                <label class="flex items-center gap-2 cursor-pointer">
                  <input v-model="profile.gender" type="radio" value="女" class="text-primary focus:ring-primary" />
                  <span class="text-sm text-slate-700 dark:text-slate-300 font-medium">女</span>
                </label>
              </div>
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2 tracking-wide">手机号码</label>
              <input v-model="profile.phone" type="tel" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm font-medium" placeholder="请输入手机号码" />
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2 tracking-wide">邮箱</label>
              <input v-model="profile.email" type="email" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm font-medium" placeholder="请输入邮箱" />
            </div>
            <button 
              class="px-6 py-2.5 bg-primary text-white rounded-xl font-bold text-sm hover:bg-primary/90 tracking-wide"
              :disabled="saving"
              @click="saveBasicInfo"
            >
              {{ saving ? '保存中...' : '保存修改' }}
            </button>
          </div>
          
          <!-- Education Tab -->
          <div v-if="activeTab === 'education'" class="p-6 space-y-6">
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">学历</label>
              <select v-model="profile.education" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm">
                <option value="">请选择学历</option>
                <option value="博士">博士</option>
                <option value="硕士">硕士</option>
                <option value="本科">本科</option>
                <option value="大专">大专</option>
                <option value="高中">高中</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">毕业学校</label>
              <input v-model="profile.school" type="text" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="请输入毕业学校" />
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">专业</label>
              <input v-model="profile.major" type="text" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="请输入专业" />
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">工作年限</label>
              <input v-model="profile.workYears" type="number" min="0" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="请输入工作年限" />
            </div>
            <button 
              class="px-6 py-2 bg-primary text-white rounded-xl font-bold text-sm hover:bg-primary/90"
              :disabled="saving"
              @click="saveEducationInfo"
            >
              {{ saving ? '保存中...' : '保存修改' }}
            </button>
          </div>
          
          <!-- Skills Tab -->
          <div v-if="activeTab === 'skills'" class="p-6 space-y-6">
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">期望职位</label>
              <input v-model="profile.expectedPosition" type="text" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="请输入期望职位" />
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">技能标签</label>
              <input v-model="profile.skills" type="text" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="请输入技能标签，用逗号分隔" />
              <p class="text-xs text-slate-500 mt-2">例如：Java, Python, MySQL, React</p>
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">期望工作地点</label>
              <input v-model="profile.expectedLocation" type="text" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="请输入期望工作地点" />
            </div>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">期望薪资下限（元/月）</label>
                <input v-model="profile.expectedSalaryMin" type="number" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="如：15000" />
              </div>
              <div>
                <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">期望薪资上限（元/月）</label>
                <input v-model="profile.expectedSalaryMax" type="number" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="如：25000" />
              </div>
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">个人简介</label>
              <textarea v-model="profile.resume" rows="4" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="请输入个人简介"></textarea>
            </div>
            <button 
              class="px-6 py-2 bg-primary text-white rounded-xl font-bold text-sm hover:bg-primary/90"
              :disabled="saving"
              @click="saveSkillsInfo"
            >
              {{ saving ? '保存中...' : '保存修改' }}
            </button>
          </div>
          
          <!-- Security Tab -->
          <div v-if="activeTab === 'security'" class="p-6 space-y-6">
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">当前密码</label>
              <input v-model="security.oldPassword" type="password" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="请输入当前密码" />
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">新密码</label>
              <input v-model="security.newPassword" type="password" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="请输入新密码" />
            </div>
            <div>
              <label class="block text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">确认新密码</label>
              <input v-model="security.confirmPassword" type="password" class="w-full px-4 py-3 bg-slate-100 dark:bg-slate-800 rounded-xl border-none focus:ring-1 focus:ring-primary text-sm" placeholder="请再次输入新密码" />
            </div>
            <button 
              class="px-6 py-2 bg-primary text-white rounded-xl font-bold text-sm hover:bg-primary/90"
              :disabled="saving"
              @click="changePassword"
            >
              {{ saving ? '修改中...' : '修改密码' }}
            </button>
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

const activeTab = ref('basic')
const saving = ref(false)

const tabs = [
  { id: 'basic', label: '基本信息' },
  { id: 'education', label: '教育背景' },
  { id: 'skills', label: '技能特长' },
  { id: 'security', label: '账号安全' }
]

const profile = ref({
  realName: '',
  gender: '男',
  phone: '',
  email: '',
  education: '',
  school: '',
  major: '',
  workYears: null,
  expectedPosition: '',
  skills: '',
  expectedLocation: '',
  expectedSalaryMin: null,
  expectedSalaryMax: null,
  resume: ''
})

const security = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const fetchProfile = async () => {
  try {
    const data = await api.getJobSeekerProfile()
    if (data) {
      profile.value = {
        realName: data.realName || '',
        gender: data.gender || '男',
        phone: data.phone || userInfo.value?.phone || '',
        email: data.email || userInfo.value?.email || '',
        education: data.education || '',
        school: data.school || '',
        major: data.major || '',
        workYears: data.workYears || null,
        expectedPosition: data.expectedPosition || '',
        skills: data.skills || '',
        expectedLocation: data.expectedLocation || '',
        expectedSalaryMin: data.expectedSalaryMin || null,
        expectedSalaryMax: data.expectedSalaryMax || null,
        resume: data.resume || ''
      }
    }
  } catch (error) {
    console.error('获取个人信息失败', error)
    // 使用本地存储的用户信息作为后备
    profile.value.phone = userInfo.value?.phone || ''
    profile.value.email = userInfo.value?.email || ''
  }
}

const saveBasicInfo = async () => {
  saving.value = true
  try {
    await api.updateJobSeekerProfile({
      realName: profile.value.realName,
      gender: profile.value.gender,
      phone: profile.value.phone,
      email: profile.value.email
    })
    toast.success('基本信息保存成功')
  } catch (error) {
    console.error('保存失败', error)
    toast.error('保存失败，请重试')
  } finally {
    saving.value = false
  }
}

const saveEducationInfo = async () => {
  saving.value = true
  try {
    await api.updateJobSeekerProfile({
      education: profile.value.education,
      school: profile.value.school,
      major: profile.value.major,
      workYears: profile.value.workYears
    })
    toast.success('教育背景保存成功')
  } catch (error) {
    console.error('保存失败', error)
    toast.error('保存失败，请重试')
  } finally {
    saving.value = false
  }
}

const saveSkillsInfo = async () => {
  saving.value = true
  try {
    await api.updateJobSeekerProfile({
      expectedPosition: profile.value.expectedPosition,
      skills: profile.value.skills,
      expectedLocation: profile.value.expectedLocation,
      expectedSalaryMin: profile.value.expectedSalaryMin,
      expectedSalaryMax: profile.value.expectedSalaryMax,
      resume: profile.value.resume
    })
    toast.success('技能特长保存成功')
  } catch (error) {
    console.error('保存失败', error)
    toast.error('保存失败，请重试')
  } finally {
    saving.value = false
  }
}

const changePassword = async () => {
  if (!security.value.oldPassword || !security.value.newPassword || !security.value.confirmPassword) {
    toast.warning('请填写完整的密码信息')
    return
  }
  
  if (security.value.newPassword !== security.value.confirmPassword) {
    toast.warning('两次输入的密码不一致')
    return
  }
  
  if (security.value.newPassword.length < 6) {
    toast.warning('密码长度至少6位')
    return
  }
  
  saving.value = true
  try {
    await api.changePassword(security.value.oldPassword, security.value.newPassword)
    toast.success('密码修改成功')
    security.value = {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    }
  } catch (error) {
    console.error('修改密码失败', error)
    toast.error('修改密码失败，请检查原密码是否正确')
  } finally {
    saving.value = false
  }
}

const getMemberType = () => {
  const completeness = calculateCompleteness()
  if (completeness >= 90) return '优秀会员'
  if (completeness >= 70) return '活跃会员'
  if (completeness >= 50) return '标准会员'
  return '新注册用户'
}

const calculateCompleteness = () => {
  let score = 0
  if (profile.value.realName) score += 15
  if (profile.value.phone) score += 10
  if (profile.value.email) score += 10
  if (profile.value.education) score += 15
  if (profile.value.school) score += 10
  if (profile.value.skills) score += 20
  if (profile.value.resume) score += 10
  if (profile.value.expectedPosition) score += 5
  if (profile.value.expectedLocation) score += 5
  return score
}

onMounted(() => {
  fetchProfile()
})
</script>
