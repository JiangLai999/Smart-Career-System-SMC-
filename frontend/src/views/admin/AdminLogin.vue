<template>
  <div class="min-h-screen flex">
    <!-- Left Side: Hero Section -->
    <div class="hidden lg:flex lg:w-1/2 relative overflow-hidden bg-gradient-to-br from-slate-900 to-slate-800 items-center justify-center p-12">
      <div class="absolute inset-0 z-0">
        <div class="absolute inset-0 bg-gradient-to-br from-primary/40 to-blue-600/40 mix-blend-multiply z-10"></div>
        <img alt="Admin dashboard interface" class="w-full h-full object-cover opacity-30" src="https://images.unsplash.com/photo-1551288049-bebda4e38f71?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80" />
      </div>
      <div class="relative z-20 max-w-lg text-white">
        <div class="mb-8">
          <div class="flex items-center gap-3 mb-6">
            <div class="size-12 bg-white/10 backdrop-blur-md rounded-xl flex items-center justify-center border border-white/20">
              <span class="material-symbols-outlined text-3xl">admin_panel_settings</span>
            </div>
            <span class="text-2xl font-bold tracking-tight">管理控制台</span>
          </div>
          <h1 class="text-5xl font-black leading-tight mb-6">
            智能管理<br/>掌控全局
          </h1>
          <p class="text-xl text-white/90 font-light leading-relaxed">
            一站式系统管理平台，全面掌控用户、企业、数据与运营，打造智能化管理体验。
          </p>
        </div>
        <div class="grid grid-cols-2 gap-6 mt-12">
          <div class="bg-white/10 backdrop-blur-md p-4 rounded-xl border border-white/20">
            <p class="text-3xl font-bold mb-1">实时监控</p>
            <p class="text-sm text-white/70">系统运行状态一目了然</p>
          </div>
          <div class="bg-white/10 backdrop-blur-md p-4 rounded-xl border border-white/20">
            <p class="text-3xl font-bold mb-1">智能分析</p>
            <p class="text-sm text-white/70">数据驱动决策优化</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Right Side: Login Form -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-6 sm:p-12 bg-slate-50 dark:bg-slate-900">
      <div class="w-full max-w-[480px] space-y-8 bg-white dark:bg-slate-800 p-10 rounded-xl shadow-xl shadow-primary/5 border border-slate-100 dark:border-slate-700">
        <div class="text-center lg:text-left">
          <!-- Mobile Logo -->
          <div class="lg:hidden flex justify-center mb-6">
            <div class="size-12 bg-primary rounded-xl flex items-center justify-center text-white">
              <span class="material-symbols-outlined text-3xl">admin_panel_settings</span>
            </div>
          </div>
          <h2 class="text-3xl font-bold text-slate-900 dark:text-white">管理员登录</h2>
          <p class="mt-2 text-slate-500 dark:text-slate-400">请输入管理员账号以继续</p>
        </div>

        <form @submit.prevent="handleLogin" class="space-y-5">
          <!-- Username Field -->
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">用户名</label>
            <div class="relative">
              <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 text-xl">account_circle</span>
              <input
                v-model="form.username"
                type="text"
                class="w-full pl-12 pr-4 h-12 rounded-lg border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                placeholder="请输入管理员用户名"
                required
              />
            </div>
          </div>

          <!-- Password Field -->
          <div class="space-y-2">
            <label class="text-sm font-medium text-slate-700 dark:text-slate-300">密码</label>
            <div class="relative">
              <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 text-xl">lock</span>
              <input
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                class="w-full pl-12 pr-12 h-12 rounded-lg border border-slate-200 dark:border-slate-600 bg-slate-50 dark:bg-slate-700 text-slate-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all"
                placeholder="请输入密码"
                required
              />
              <button
                type="button"
                class="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-300"
                @click="showPassword = !showPassword"
              >
                <span class="material-symbols-outlined text-xl">{{ showPassword ? 'visibility_off' : 'visibility' }}</span>
              </button>
            </div>
          </div>

          <!-- Remember Me -->
          <div class="flex items-center justify-between py-2">
            <label class="flex items-center gap-2 cursor-pointer group">
              <input v-model="rememberMe" type="checkbox" class="w-4 h-4 rounded border-slate-300 text-primary focus:ring-primary/20 cursor-pointer" />
              <span class="text-sm text-slate-600 dark:text-slate-400 group-hover:text-slate-900 dark:group-hover:text-slate-200">记住账号</span>
            </label>
          </div>

          <!-- Submit Button -->
          <button
            type="submit"
            :disabled="loading"
            class="w-full h-12 bg-primary hover:bg-primary/90 text-white font-bold rounded-lg shadow-lg shadow-primary/25 transition-all transform active:scale-[0.98] disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>

        <!-- Security Notice -->
        <div class="mt-8 p-4 bg-amber-50 dark:bg-amber-900/20 rounded-lg border border-amber-200 dark:border-amber-800">
          <div class="flex gap-3">
            <span class="material-symbols-outlined text-amber-600 dark:text-amber-400 text-xl">warning</span>
            <div class="flex-1">
              <p class="text-sm font-medium text-amber-900 dark:text-amber-200">安全提示</p>
              <p class="text-xs text-amber-700 dark:text-amber-300 mt-1">此页面仅供系统管理员访问，所有操作将被记录。如非授权人员，请立即退出。</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'

const router = useRouter()
const form = ref({ username: '', password: '' })
const showPassword = ref(false)
const rememberMe = ref(false)
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  try {
    const response = await api.adminLogin(form.value)
    console.log('登录响应:', response)
    if (response && response.token) {
      localStorage.setItem('adminToken', response.token)
      localStorage.setItem('adminInfo', JSON.stringify({
        userId: response.userId,
        username: response.username,
        userType: response.userType
      }))
      router.push('/admin')
    } else {
      alert('登录失败：' + JSON.stringify(response))
    }
  } catch (error) {
    console.error('登录错误:', error)
    alert('登录失败，请检查用户名密码')
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
