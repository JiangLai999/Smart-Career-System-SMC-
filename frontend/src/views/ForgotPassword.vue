<template>
  <div class="flex min-h-screen">
    <!-- Left Side: Visual/Branding -->
    <div class="hidden lg:flex lg:w-1/2 relative overflow-hidden bg-primary">
      <div class="absolute inset-0 z-0">
        <div class="w-full h-full bg-cover bg-center" 
          style="background-image: linear-gradient(rgba(19, 127, 236, 0.8), rgba(19, 127, 236, 0.6)), url('https://lh3.googleusercontent.com/aida-public/AB6AXuDS_e-m2VLmUSDi2-6mDST5MJBPF7aizBDmCkgYcioncjHpbL_qvJWIwUsW-f_MhKlyrOu7varGAruPoAS18yEzbunyxscTdEhH5qB3deRYAr2hWJFIx2vNvpNQK9dkE9vnOLJ3Jtqa-ggNLcr504tIz2NtIB1ll4NqSU4G0ASqrt4ZPg8uxtSW0UzGvLuK4NMwjFIeJzJIz7GZU7TCOaFxx2TXoyXhYSyZtvXU0KG4nHZzj_1ZY_ywJscEDzBQSi8W9Z2QPj8Afe0')">
        </div>
      </div>
      <div class="relative z-10 flex flex-col justify-between p-12 text-white w-full">
        <div class="flex items-center gap-3">
          <div class="bg-white/20 p-2 rounded-lg backdrop-blur-md">
            <span class="material-symbols-outlined text-3xl">work</span>
          </div>
          <h1 class="text-2xl font-bold tracking-tight">求职者平台</h1>
        </div>
        <div class="max-w-md">
          <h2 class="text-5xl font-extrabold leading-tight mb-6">找回密码</h2>
          <p class="text-xl font-medium text-white/90">输入您的手机号或邮箱，我们将发送验证码帮您重置密码</p>
        </div>
        <div class="flex items-center gap-4 text-sm text-white/70">
          <span>© 2024 智能职业规划系统. All rights reserved.</span>
        </div>
      </div>
    </div>
    
    <!-- Right Side: Form -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-6 sm:p-12 md:p-20">
      <div class="w-full max-w-[480px] bg-white dark:bg-slate-900 p-8 md:p-10 rounded-xl shadow-sm border border-slate-200 dark:border-slate-800">
        <!-- Step 1: 输入账号 -->
        <div v-if="step === 1">
          <div class="mb-8">
            <h2 class="text-3xl font-bold text-slate-900 dark:text-white">找回密码</h2>
            <p class="text-slate-500 dark:text-slate-400 mt-2">请输入您的手机号或邮箱</p>
          </div>
          
          <form @submit.prevent="sendCode" class="space-y-5">
            <div class="space-y-2">
              <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">手机号或邮箱</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400">person</span>
                <input 
                  v-model="form.account"
                  class="w-full pl-12 pr-4 py-3.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all placeholder:text-slate-400 text-sm" 
                  placeholder="请输入手机号或邮箱" 
                  type="text"
                  required
                />
              </div>
            </div>
            
            <button 
              type="submit"
              :disabled="loading"
              class="w-full py-4 bg-primary hover:bg-primary/90 text-white font-bold rounded-lg shadow-lg shadow-primary/25 transition-all transform active:scale-[0.98] disabled:opacity-50 text-sm"
            >
              {{ loading ? '发送中...' : '发送验证码' }}
            </button>
          </form>
        </div>
        
        <!-- Step 2: 输入验证码 -->
        <div v-if="step === 2">
          <div class="mb-8">
            <h2 class="text-3xl font-bold text-slate-900 dark:text-white">输入验证码</h2>
            <p class="text-slate-500 dark:text-slate-400 mt-2">验证码已发送至 {{ form.account }}</p>
          </div>
          
          <form @submit.prevent="verifyCode" class="space-y-5">
            <div class="space-y-2">
              <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">验证码</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400">key</span>
                <input 
                  v-model="form.code"
                  class="w-full pl-12 pr-4 py-3.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all placeholder:text-slate-400 text-sm" 
                  placeholder="请输入6位验证码" 
                  type="text"
                  maxlength="6"
                  required
                />
              </div>
            </div>
            
            <div class="flex justify-between items-center text-sm">
              <button type="button" @click="step = 1" class="text-slate-500 hover:text-primary">返回上一步</button>
              <button type="button" @click="sendCode" :disabled="countdown > 0" class="text-primary hover:underline disabled:text-slate-400">
                {{ countdown > 0 ? `${countdown}秒后重发` : '重新发送' }}
              </button>
            </div>
            
            <button 
              type="submit"
              :disabled="loading"
              class="w-full py-4 bg-primary hover:bg-primary/90 text-white font-bold rounded-lg shadow-lg shadow-primary/25 transition-all transform active:scale-[0.98] disabled:opacity-50 text-sm"
            >
              {{ loading ? '验证中...' : '验证' }}
            </button>
          </form>
        </div>
        
        <!-- Step 3: 设置新密码 -->
        <div v-if="step === 3">
          <div class="mb-8">
            <h2 class="text-3xl font-bold text-slate-900 dark:text-white">设置新密码</h2>
            <p class="text-slate-500 dark:text-slate-400 mt-2">请输入您的新密码</p>
          </div>
          
          <form @submit.prevent="resetPassword" class="space-y-5">
            <div class="space-y-2">
              <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">新密码</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400">lock</span>
                <input 
                  v-model="form.newPassword"
                  class="w-full pl-12 pr-4 py-3.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all placeholder:text-slate-400 text-sm" 
                  placeholder="请输入新密码（至少6位）" 
                  :type="showPassword ? 'text' : 'password'"
                  required
                  minlength="6"
                />
                <button type="button" @click="showPassword = !showPassword" class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600">
                  <span class="material-symbols-outlined text-xl">{{ showPassword ? 'visibility_off' : 'visibility' }}</span>
                </button>
              </div>
            </div>
            
            <div class="space-y-2">
              <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">确认新密码</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400">lock_reset</span>
                <input 
                  v-model="form.confirmPassword"
                  class="w-full pl-12 pr-4 py-3.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all placeholder:text-slate-400 text-sm" 
                  placeholder="请再次输入新密码" 
                  :type="showConfirmPassword ? 'text' : 'password'"
                  required
                  minlength="6"
                />
                <button type="button" @click="showConfirmPassword = !showConfirmPassword" class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600">
                  <span class="material-symbols-outlined text-xl">{{ showConfirmPassword ? 'visibility_off' : 'visibility' }}</span>
                </button>
              </div>
            </div>
            
            <button 
              type="submit"
              :disabled="loading"
              class="w-full py-4 bg-primary hover:bg-primary/90 text-white font-bold rounded-lg shadow-lg shadow-primary/25 transition-all transform active:scale-[0.98] disabled:opacity-50 text-sm"
            >
              {{ loading ? '重置中...' : '重置密码' }}
            </button>
          </form>
        </div>
        
        <!-- Success -->
        <div v-if="step === 4" class="text-center py-8">
          <div class="w-20 h-20 rounded-full bg-green-100 flex items-center justify-center mx-auto mb-6">
            <span class="material-symbols-outlined text-5xl text-green-500">check_circle</span>
          </div>
          <h2 class="text-2xl font-bold text-slate-900 dark:text-white mb-2">密码重置成功</h2>
          <p class="text-slate-500 dark:text-slate-400 mb-6">您的密码已成功重置，请使用新密码登录</p>
          <router-link to="/login" class="inline-block px-8 py-3 bg-primary text-white font-bold rounded-lg hover:bg-primary/90 transition-colors">
            立即登录
          </router-link>
        </div>
        
        <!-- Footer -->
        <div v-if="step < 4" class="mt-8 text-center">
          <p class="text-slate-600 dark:text-slate-400">
            想起密码了？ <router-link to="/login" class="text-primary font-bold hover:underline">返回登录</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from '@/composables/useToast'
import api from '@/api'

const router = useRouter()
const toast = useToast()

const step = ref(1)
const loading = ref(false)
const countdown = ref(0)
const showPassword = ref(false)
const showConfirmPassword = ref(false)

const form = ref({
  account: '',
  code: '',
  newPassword: '',
  confirmPassword: ''
})

const sendCode = async () => {
  if (!form.value.account) {
    toast.warning('请输入手机号或邮箱')
    return
  }
  
  if (!/^1[3-9]\d{9}$/.test(form.value.account)) {
    toast.warning('请输入正确的手机号')
    return
  }
  
  loading.value = true
  try {
    await api.sendVerificationCode(form.value.account, 'RESET_PASSWORD')
    toast.success('验证码已发送，有效期5分钟')
    step.value = 2
    
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
  } catch (error) {
    console.error('发送验证码失败', error)
    const message = error.response?.data?.message || error.message || '发送失败，请稍后重试'
    toast.error(message)
  } finally {
    loading.value = false
  }
}

const verifyCode = async () => {
  if (!form.value.code) {
    toast.warning('请输入验证码')
    return
  }
  
  if (!/^\d{6}$/.test(form.value.code)) {
    toast.warning('请输入6位数字验证码')
    return
  }
  
  loading.value = true
  try {
    await api.verifyCode(form.value.account, form.value.code, 'RESET_PASSWORD')
    toast.success('验证成功')
    step.value = 3
  } catch (error) {
    console.error('验证码验证失败', error)
    const message = error.response?.data?.message || error.message || '验证失败，请重试'
    toast.error(message)
  } finally {
    loading.value = false
  }
}

const resetPassword = async () => {
  if (form.value.newPassword.length < 6) {
    toast.warning('密码长度至少6位')
    return
  }
  
  if (form.value.newPassword !== form.value.confirmPassword) {
    toast.error('两次输入的密码不一致')
    return
  }
  
  loading.value = true
  try {
    await api.resetPassword(form.value.account, form.value.code, form.value.newPassword)
    toast.success('密码重置成功')
    step.value = 4
  } catch (error) {
    console.error('重置密码失败', error)
    const message = error.response?.data?.message || error.message || '重置失败，请重试'
    toast.error(message)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.material-symbols-outlined {
  font-variation-settings: 'FILL' 0, 'wght' 400, 'GRAD' 0, 'opsz' 24;
}
</style>
