<template>
  <div class="flex min-h-screen bg-background-light dark:bg-background-dark font-display text-slate-900 dark:text-slate-100 antialiased">
    <!-- Left Side: Hero Section -->
    <div class="hidden lg:flex lg:w-1/2 relative overflow-hidden bg-primary items-center justify-center p-12">
      <div class="absolute inset-0 z-0">
        <div class="absolute inset-0 bg-gradient-to-br from-primary/80 to-primary mix-blend-multiply z-10"></div>
        <div class="w-full h-full bg-cover bg-center" style="background-image: url('https://images.unsplash.com/photo-1497366216548-37526070297c?w=1920')"></div>
      </div>
      <div class="relative z-20 max-w-lg text-white">
        <div class="mb-8">
          <div class="flex items-center gap-2 mb-6">
            <span class="material-symbols-outlined text-4xl">hub</span>
            <span class="text-2xl font-bold tracking-tight">企业招聘系统</span>
          </div>
          <h1 class="text-5xl font-black leading-tight mb-6">
            企业门户：<br/>发现顶尖人才
          </h1>
          <p class="text-xl text-white/90 font-light leading-relaxed">
            专业高效的人才招聘管理平台，助力企业精准连接全球精英，构建卓越团队。
          </p>
        </div>
        <div class="grid grid-cols-2 gap-6 mt-12">
          <div class="bg-white/10 backdrop-blur-md p-4 rounded-xl border border-white/20">
            <p class="text-3xl font-bold mb-1">10k+</p>
            <p class="text-sm text-white/70">入驻名企</p>
          </div>
          <div class="bg-white/10 backdrop-blur-md p-4 rounded-xl border border-white/20">
            <p class="text-3xl font-bold mb-1">98%</p>
            <p class="text-sm text-white/70">招聘成功率</p>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Right Side: Login Form -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-6 sm:p-12 bg-background-light dark:bg-background-dark">
      <div class="w-full max-w-[480px] space-y-8">
        <div class="text-center lg:text-left">
          <h2 class="text-3xl font-bold text-slate-900 dark:text-white">企业登录</h2>
          <p class="mt-2 text-slate-600 dark:text-slate-400">请输入您的企业账号信息以继续</p>
        </div>
        
        <form @submit.prevent="handleLogin" class="space-y-5">
          <!-- Account Field -->
           <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">账号</label>
             <div class="relative">
              <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 text-xl">account_circle</span>
               <input 
                v-model="form.username"
                type="text"
                 class="w-full pl-12 pr-4 h-12 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all outline-none"
                placeholder="用户名 / 手机号 / 邮箱"
                 required
               />
             </div>
            <p class="text-xs text-slate-500 dark:text-slate-400 mt-1">支持用户名、手机号或邮箱登录</p>
           </div>
          
          <!-- Password Field -->
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">密码</label>
            <div class="relative">
              <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 text-xl">lock</span>
              <input 
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                class="w-full pl-12 pr-12 h-12 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all outline-none"
                placeholder="请输入您的密码"
                required
              />
              <button 
                type="button"
                class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200"
                @click="showPassword = !showPassword"
              >
                <span class="material-symbols-outlined text-xl">{{ showPassword ? 'visibility_off' : 'visibility' }}</span>
              </button>
            </div>
          </div>
          
          <!-- Captcha Field -->
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">验证码</label>
            <div class="flex gap-3">
              <div class="relative flex-1">
                <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 text-xl">verified_user</span>
                <input 
                  v-model="form.captcha"
                  type="text"
                  class="w-full pl-12 pr-4 h-12 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-900 dark:text-white focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all outline-none"
                  placeholder="请输入验证码"
                  required
                />
              </div>
              <div class="w-32 h-12 rounded-lg bg-slate-100 dark:bg-slate-700 flex items-center justify-center border border-slate-200 dark:border-slate-600 cursor-pointer overflow-hidden" @click="refreshCaptcha">
                <div class="bg-gradient-to-r from-primary/10 to-primary/5 w-full h-full flex items-center justify-center italic font-bold tracking-widest text-primary select-none">
                  {{ captchaText }}
                </div>
              </div>
            </div>
          </div>
          
          <!-- Remember & Forgot -->
           <div class="flex items-center justify-between py-2">
             <label class="flex items-center gap-2 cursor-pointer group">
               <input v-model="rememberMe" type="checkbox" class="w-4 h-4 rounded border-slate-300 text-primary focus:ring-primary/20" />
               <span class="text-sm text-slate-600 dark:text-slate-400 group-hover:text-slate-900 dark:group-hover:text-slate-200">记住账号</span>
             </label>
             <router-link to="/company/forgot-password" class="text-sm font-medium text-primary hover:underline">忘记密码？</router-link>
           </div>
          
          <!-- Submit -->
          <button 
            type="submit"
            :disabled="loading"
            class="w-full h-12 bg-primary hover:bg-primary/90 text-white font-bold rounded-lg shadow-lg shadow-primary/20 transition-all transform active:scale-[0.98]"
          >
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>
        
        <!-- Third Party Login -->
        <div class="relative py-4">
          <div class="absolute inset-0 flex items-center">
            <div class="w-full border-t border-slate-200 dark:border-slate-700"></div>
          </div>
          <div class="relative flex justify-center text-sm">
            <span class="bg-background-light dark:bg-background-dark px-4 text-slate-500">第三方账号登录</span>
          </div>
        </div>
        
<div class="grid grid-cols-2 gap-4">
  <button class="flex items-center justify-center gap-2 h-11 border border-slate-200 dark:border-slate-700 rounded-lg bg-white dark:bg-slate-800 hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors opacity-50 cursor-not-allowed" disabled>
    <svg class="w-5 h-5 text-green-600" fill="currentColor" viewBox="0 0 24 24">
      <path d="M8.691 2.188C3.891 2.188 0 5.476 0 9.53c0 2.212 1.17 4.203 3.002 5.55a.59.59 0 01.213.665l-.39 1.48c-.019.07-.048.141-.048.213 0 .163.13.295.29.295a.326.326 0 00.167-.054l1.903-1.114a.864.864 0 01.717-.098 10.16 10.16 0 002.837.403c.276 0 .543-.027.811-.05-.857-2.578.157-4.972 1.932-6.446 1.703-1.415 3.882-1.98 5.853-1.838-.576-3.583-4.196-6.348-8.596-6.348zM5.785 5.991c.642 0 1.162.529 1.162 1.18a1.17 1.17 0 01-1.162 1.178A1.17 1.17 0 014.623 7.17c0-.651.52-1.18 1.162-1.18zm5.813 0c.642 0 1.162.529 1.162 1.18a1.17 1.17 0 01-1.162 1.178 1.17 1.17 0 01-1.162-1.178c0-.651.52-1.18 1.162-1.18zm5.34 2.867c-1.797-.052-3.746.512-5.28 1.786-1.72 1.428-2.687 3.72-1.78 6.22.942 2.453 3.666 4.229 6.884 4.229.826 0 1.622-.12 2.361-.336a.722.722 0 01.598.082l1.584.926a.272.272 0 00.14.045c.134 0 .24-.11.24-.246 0-.06-.023-.12-.038-.177l-.327-1.233a.582.582 0 01-.023-.156.49.49 0 01.201-.398C23.024 18.48 24 16.82 24 14.98c0-3.21-2.931-5.837-6.656-6.088V8.89c-.135.01-.27.028-.406.028zm2.829 4.72c.535 0 .969.44.969.982a.976.976 0 01-.969.983.976.976 0 01-.969-.983c0-.542.434-.982.97-.982z"/>
    </svg>
    <span class="text-sm font-medium">企业微信</span>
    <span class="text-xs text-slate-400">(暂不支持)</span>
  </button>
  <button class="flex items-center justify-center gap-2 h-11 border border-slate-200 dark:border-slate-700 rounded-lg bg-white dark:bg-slate-800 hover:bg-slate-50 dark:hover:bg-slate-700 transition-colors opacity-50 cursor-not-allowed" disabled>
    <svg class="w-5 h-5 text-blue-500" fill="currentColor" viewBox="0 0 24 24">
      <path d="M12 0C5.373 0 0 5.373 0 12s5.373 12 12 12 12-5.373 12-12S18.627 0 12 0zm5.562 8.161c-.18 1.897-.962 6.502-1.359 8.627-.168.9-.5 1.201-.82 1.23-.696.064-1.225-.46-1.901-.903-1.056-.693-1.653-1.124-2.678-1.8-1.185-.78-.417-1.21.258-1.91.177-.184 3.247-2.977 3.307-3.23.007-.032.014-.15-.056-.212s-.174-.041-.249-.024c-.106.024-1.793 1.139-5.062 3.345-.479.329-.913.49-1.302.48-.428-.008-1.252-.242-1.865-.44-.751-.245-1.349-.374-1.297-.789.027-.216.325-.437.893-.663 3.498-1.524 5.83-2.529 6.998-3.015 3.333-1.386 4.025-1.627 4.476-1.635z"/>
    </svg>
    <span class="text-sm font-medium">钉钉登录</span>
    <span class="text-xs text-slate-400">(暂不支持)</span>
  </button>
</div>
        
        <!-- Register Link -->
        <div class="text-center pt-8">
          <p class="text-sm text-slate-600 dark:text-slate-400">
            还没有企业账号？ 
            <router-link to="/company/register" class="text-primary font-bold hover:underline">立即入驻</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import api from '@/api'

const router = useRouter()
const route = useRoute()

const form = ref({
  username: '',
  password: '',
  captcha: ''
})
const showPassword = ref(false)
const rememberMe = ref(false)
const loading = ref(false)
const captchaText = ref('')

const generateCaptcha = () => {
  const chars = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789'
  let result = ''
  for (let i = 0; i < 4; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length)) + ' '
  }
  captchaText.value = result.trim()
}

const refreshCaptcha = () => {
  generateCaptcha()
}

onMounted(() => {
  generateCaptcha()
})

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    alert('请输入用户名和密码')
    return
  }
  
  loading.value = true
  try {
    const data = await api.companyLogin({
      username: form.value.username,
      password: form.value.password
    })
    localStorage.setItem('companyToken', data.token)
    localStorage.setItem('companyInfo', JSON.stringify({
      id: data.enterpriseId,
      companyName: data.enterpriseName,
      status: data.auditStatus === 1 ? 'APPROVED' : data.auditStatus === 2 ? 'REJECTED' : 'PENDING'
    }))
    
    const redirect = route.query.redirect || '/company/dashboard'
    router.push(redirect)
  } catch (error) {
    console.error('登录失败', error)
    const errorMsg = error.response?.data?.message || error.message || '登录失败，请检查账号密码'
    alert(errorMsg)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.bg-background-light {
  background-color: #f6f7f8;
}
.bg-background-dark {
  background-color: #101922;
}
</style>