<template>
  <div class="min-h-screen flex">
    <!-- Left Side: Visual/Branding -->
    <div class="hidden lg:flex lg:w-1/2 relative flex-col justify-between p-12 overflow-hidden bg-primary/10">
      <!-- Background Image with Overlay -->
      <div class="absolute inset-0 z-0">
        <div class="absolute inset-0 bg-gradient-to-br from-primary/90 to-primary/40 mix-blend-multiply z-10"></div>
        <img alt="Professional modern office space" class="w-full h-full object-cover" src="https://images.unsplash.com/photo-1497366216548-37526070297c?ixlib=rb-4.0.3&auto=format&fit=crop&w=2069&q=80" />
      </div>
      <!-- Content -->
      <div class="relative z-20 flex items-center gap-3 text-white">
        <div class="size-10 bg-white rounded-lg flex items-center justify-center text-primary">
          <span class="material-symbols-outlined text-3xl">work</span>
        </div>
        <h1 class="text-2xl font-bold tracking-tight">智能职业规划系统</h1>
      </div>
      <div class="relative z-20">
        <h2 class="text-white text-5xl font-bold leading-tight mb-6">
          连接梦想与机遇
        </h2>
        <p class="text-white/80 text-lg max-w-md leading-relaxed">
          发现最适合您的职业路径，与顶尖企业建立联系，开启您的职场新篇章。
        </p>
      </div>
      <div class="relative z-20 flex gap-4">
        <div class="flex -space-x-3 overflow-hidden">
          <img alt="User 1" class="inline-block h-10 w-10 rounded-full ring-2 ring-white object-cover" src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&h=100&fit=crop" />
          <img alt="User 2" class="inline-block h-10 w-10 rounded-full ring-2 ring-white object-cover" src="https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=100&h=100&fit=crop" />
          <img alt="User 3" class="inline-block h-10 w-10 rounded-full ring-2 ring-white object-cover" src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&h=100&fit=crop" />
        </div>
        <div class="text-white text-sm">
          <p class="font-bold">超过 10,000+ 求职者</p>
          <p class="text-white/70">已经通过我们找到了理想工作</p>
        </div>
      </div>
    </div>

    <!-- Right Side: Login Form -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-8 bg-slate-50 dark:bg-slate-900">
      <div class="w-full max-w-md space-y-8 bg-white dark:bg-slate-800 p-10 rounded-xl shadow-xl shadow-primary/5 border border-slate-100 dark:border-slate-700">
        <div class="text-center lg:text-left">
          <!-- Mobile Logo -->
          <div class="lg:hidden flex justify-center mb-6">
            <div class="size-12 bg-primary rounded-lg flex items-center justify-center text-white">
              <span class="material-symbols-outlined text-3xl">work</span>
            </div>
          </div>
          <h2 class="text-3xl font-bold text-slate-900 dark:text-white">欢迎回来</h2>
          <p class="mt-2 text-slate-500 dark:text-slate-400">请登录您的账号以继续</p>
        </div>

        <form @submit.prevent="handleLogin" class="mt-8 space-y-6">
          <div class="space-y-4">
            <!-- Account Input -->
            <div>
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1" for="account">邮箱/手机号</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 text-xl">mail</span>
                <input
                  id="account"
                  v-model="form.username"
                  class="appearance-none block w-full pl-10 pr-3 py-3 border border-slate-200 dark:border-slate-600 rounded-lg bg-slate-50 dark:bg-slate-700 text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all sm:text-sm"
                  placeholder="请输入您的邮箱或手机号"
                  required
                  type="text"
                />
              </div>
            </div>

            <!-- Password Input -->
            <div>
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1" for="password">密码</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 text-xl">lock</span>
                <input
                  id="password"
                  v-model="form.password"
                  :type="showPassword ? 'text' : 'password'"
                  class="appearance-none block w-full pl-10 pr-12 py-3 border border-slate-200 dark:border-slate-600 rounded-lg bg-slate-50 dark:bg-slate-700 text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all sm:text-sm"
                  placeholder="请输入您的密码"
                  required
                />
                <button
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-300"
                  type="button"
                  @click="showPassword = !showPassword"
                >
                  <span class="material-symbols-outlined text-xl">{{ showPassword ? 'visibility_off' : 'visibility' }}</span>
                </button>
              </div>
            </div>

            <!-- Captcha -->
            <div>
              <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1" for="captcha">验证码</label>
              <div class="flex gap-3">
                <div class="relative flex-1">
                  <span class="material-symbols-outlined absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 text-xl">verified_user</span>
                  <input
                    id="captcha"
                    v-model="form.captcha"
                    class="appearance-none block w-full pl-10 pr-3 py-3 border border-slate-200 dark:border-slate-600 rounded-lg bg-slate-50 dark:bg-slate-700 text-slate-900 dark:text-white placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent transition-all sm:text-sm"
                    :class="{ '!border-red-500': captchaError }"
                    placeholder="请输入验证码"
                    required
                    type="text"
                  />
                  <p v-if="captchaError" class="mt-1.5 text-xs text-red-500 flex items-center gap-1">
                    <span class="material-symbols-outlined text-sm">error</span>
                    验证码错误，请重新输入
                  </p>
                </div>
                <div class="flex items-center gap-2">
                  <div
                    class="h-[46px] px-4 bg-slate-100 dark:bg-slate-600 border border-slate-200 dark:border-slate-500 rounded-lg flex items-center justify-center select-none cursor-pointer hover:bg-slate-200 dark:hover:bg-slate-500 transition-colors"
                    @click="refreshCaptcha"
                    :title="'点击刷新验证码'"
                  >
                    <span class="text-lg font-bold tracking-widest text-slate-600 dark:text-slate-300 italic" style="font-family: serif; transform: skew(-5deg);">{{ captchaCode }}</span>
                  </div>
                  <button
                    class="text-xs text-primary hover:text-primary/80 flex flex-col items-center gap-0.5 transition-colors"
                    type="button"
                    @click="refreshCaptcha"
                  >
                    <span class="material-symbols-outlined text-sm">refresh</span>
                    <span>换一张</span>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Remember Me & Forgot Password -->
          <div class="flex items-center justify-between mt-6">
            <div class="flex items-center">
              <input
                id="remember-me"
                v-model="rememberMe"
                class="h-4 w-4 text-primary focus:ring-primary border-slate-300 rounded cursor-pointer transition-colors"
                type="checkbox"
              />
              <label class="ml-2 block text-sm text-slate-700 dark:text-slate-300 cursor-pointer" for="remember-me">
                记住我
              </label>
            </div>
            <div class="text-sm">
              <router-link to="/forgot-password" class="font-medium text-primary hover:text-primary/80 transition-colors">
                忘记密码？
              </router-link>
            </div>
          </div>

          <!-- Login Button -->
          <div>
            <button
              type="submit"
              :disabled="loading"
              class="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-bold rounded-lg text-white bg-primary hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-all shadow-lg shadow-primary/25 disabled:opacity-50"
            >
              {{ loading ? '登录中...' : '登录' }}
            </button>
          </div>
        </form>

        <!-- Social Login -->
        <div class="mt-8">
          <div class="relative">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-slate-200 dark:border-slate-600"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-2 bg-white dark:bg-slate-800 text-slate-500">或者使用以下方式登录</span>
            </div>
          </div>
          <div class="mt-6 grid grid-cols-2 gap-4">
            <button
              type="button"
              class="flex w-full items-center justify-center gap-3 rounded-lg border border-slate-200 dark:border-slate-600 bg-white dark:bg-slate-700 px-4 py-2.5 text-sm font-medium text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-600 transition-colors"
            >
              <svg class="h-5 w-5" viewBox="0 0 24 24" fill="currentColor">
                <path d="M8.691 2.188C3.891 2.188 0 5.476 0 9.53c0 2.212 1.17 4.203 3.002 5.55a.59.59 0 01.213.665l-.39 1.48c-.019.07-.048.141-.048.213 0 .163.13.295.29.295a.326.326 0 00.167-.054l1.903-1.114a.864.864 0 01.717-.098 10.16 10.16 0 002.837.403c.276 0 .543-.027.811-.05-.857-2.578.157-4.972 1.932-6.446 1.703-1.415 3.882-1.98 5.853-1.838-.576-3.583-4.196-6.348-8.596-6.348zM5.785 5.991c.642 0 1.162.529 1.162 1.18a1.17 1.17 0 01-1.162 1.178A1.17 1.17 0 014.623 7.17c0-.651.52-1.18 1.162-1.18zm5.813 0c.642 0 1.162.529 1.162 1.18a1.17 1.17 0 01-1.162 1.178 1.17 1.17 0 01-1.162-1.178c0-.651.52-1.18 1.162-1.18zm5.34 2.867c-1.797-.052-3.746.512-5.28 1.786-1.72 1.428-2.687 3.72-1.78 6.22.942 2.453 3.666 4.229 6.884 4.229.826 0 1.622-.12 2.361-.336a.722.722 0 01.598.082l1.584.926a.272.272 0 00.14.047c.134 0 .24-.111.24-.247 0-.06-.023-.12-.038-.177l-.327-1.233a.582.582 0 01-.023-.156.49.49 0 01.201-.398C23.024 18.48 24 16.82 24 14.98c0-3.21-2.931-5.837-7.062-6.122zm-2.036 2.905c.535 0 .969.44.969.982a.976.976 0 01-.969.983.976.976 0 01-.969-.983c0-.542.434-.982.97-.982zm4.844 0c.535 0 .969.44.969.982a.976.976 0 01-.969.983.976.976 0 01-.969-.983c0-.542.434-.982.97-.982z"/>
              </svg>
              <span>微信</span>
            </button>
            <button
              type="button"
              class="flex w-full items-center justify-center gap-3 rounded-lg border border-slate-200 dark:border-slate-600 bg-white dark:bg-slate-700 px-4 py-2.5 text-sm font-medium text-slate-700 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-600 transition-colors"
            >
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 24 24">
                <path clip-rule="evenodd" d="M12 2C6.477 2 2 6.484 2 12.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0112 6.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.202 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.943.359.309.678.92.678 1.855 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0022 12.017C22 6.484 17.522 2 12 2z" fill-rule="evenodd"></path>
              </svg>
              <span>GitHub</span>
            </button>
          </div>
        </div>

        <!-- Register Link -->
        <div class="mt-10 text-center">
          <p class="text-sm text-slate-600 dark:text-slate-400">
            还没有账号？
            <router-link to="/register" class="font-bold text-primary hover:text-primary/80 transition-colors">
              立即注册
            </router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { useToast } from '@/composables/useToast'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const toast = useToast()

const form = ref({ username: '', password: '', captcha: '' })
const showPassword = ref(false)
const rememberMe = ref(false)
const loading = ref(false)
const captchaCode = ref('')
const captchaError = ref(false)

const generateCaptcha = () => {
  const chars = 'ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789'
  let result = ''
  for (let i = 0; i < 4; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

const refreshCaptcha = () => {
  captchaCode.value = generateCaptcha()
  captchaError.value = false
}

onMounted(async () => {
  const token = localStorage.getItem('token')
  if (token) {
    try {
      // 验证 token 是否仍然有效
      await userStore.fetchUserInfo()
      router.replace('/app/home')
      return
    } catch {
      // token 已过期或无效，清除后正常显示登录页
      userStore.logout()
    }
  }
  refreshCaptcha()
})

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    toast.warning('请输入用户名和密码')
    return
  }

  if (!form.value.captcha) {
    toast.warning('请输入验证码')
    return
  }

  if (form.value.captcha.toLowerCase() !== captchaCode.value.toLowerCase()) {
    captchaError.value = true
    toast.error('验证码错误')
    refreshCaptcha()
    return
  }

  loading.value = true
  try {
    await userStore.login({ username: form.value.username, password: form.value.password })
    toast.success('登录成功，正在跳转...')
    const redirect = route.query.redirect || '/app/home'
    setTimeout(() => { router.push(redirect) }, 500)
  } catch (error) {
    console.error('登录失败', error)
    toast.error('登录失败，请检查账号密码')
    refreshCaptcha()
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
