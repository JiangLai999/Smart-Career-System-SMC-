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
          <h1 class="text-2xl font-bold tracking-tight">Job Seeker Platform</h1>
        </div>
        <div class="max-w-md">
          <h2 class="text-5xl font-extrabold leading-tight mb-6">Join us and start your career journey</h2>
          <p class="text-xl font-medium text-white/90">加入我们，开启您的职场之旅</p>
        </div>
        <div class="flex items-center gap-4 text-sm text-white/70">
          <span>© 2024 Job Seeker Platform. All rights reserved.</span>
        </div>
      </div>
    </div>
    
    <!-- Right Side: Registration Form -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-6 sm:p-12 md:p-20">
      <div class="w-full max-w-[480px] bg-white dark:bg-slate-900 p-8 md:p-10 rounded-xl shadow-sm border border-slate-200 dark:border-slate-800">
        <div class="mb-8">
          <h2 class="text-3xl font-bold text-slate-900 dark:text-white">创建账号</h2>
          <p class="text-slate-500 dark:text-slate-400 mt-2">Create Account</p>
        </div>
        
        <form @submit.prevent="handleRegister" class="space-y-5">
          <!-- Username -->
          <div class="space-y-2">
            <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">用户名 <span class="text-red-500">*</span></label>
            <div class="relative">
              <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400">person</span>
              <input 
                v-model="form.username"
                class="w-full pl-12 pr-4 py-3.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all placeholder:text-slate-400 text-sm" 
                placeholder="3-50个字符" 
                type="text"
                required
              />
            </div>
          </div>
          
          <!-- Phone & Email -->
          <div class="grid grid-cols-2 gap-3">
            <div class="space-y-2">
              <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">手机号</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 text-lg">phone</span>
                <input 
                  v-model="form.phone"
                  class="w-full pl-11 pr-3 py-3.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all placeholder:text-slate-400 text-sm" 
                  placeholder="可选" 
                  type="tel"
                />
              </div>
            </div>
            <div class="space-y-2">
              <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">邮箱</label>
              <div class="relative">
                <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400 text-lg">mail</span>
                <input 
                  v-model="form.email"
                  class="w-full pl-11 pr-3 py-3.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all placeholder:text-slate-400 text-sm" 
                  placeholder="可选" 
                  type="email"
                />
              </div>
            </div>
          </div>
          
          <!-- Password -->
          <div class="space-y-2">
            <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">设置密码</label>
            <div class="relative">
              <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400">lock</span>
              <input 
                v-model="form.password"
                class="w-full pl-12 pr-4 py-3.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all placeholder:text-slate-400 text-sm" 
                placeholder="请输入密码" 
                type="password"
                required
              />
            </div>
          </div>
          
          <!-- Confirm Password -->
          <div class="space-y-2">
            <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">确认密码</label>
            <div class="relative">
              <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-400">lock_reset</span>
              <input 
                v-model="form.confirmPassword"
                class="w-full pl-12 pr-4 py-3.5 bg-slate-50 dark:bg-slate-800 border border-slate-200 dark:border-slate-700 rounded-lg focus:ring-2 focus:ring-primary focus:border-primary outline-none transition-all placeholder:text-slate-400 text-sm" 
                placeholder="再次输入密码" 
                type="password"
                required
              />
            </div>
          </div>
          
          <!-- Terms Checkbox -->
          <div class="flex items-start gap-3 pt-2">
            <input 
              id="terms"
              v-model="form.agreeTerms"
              class="mt-1 w-4 h-4 text-primary bg-slate-100 border-slate-300 rounded focus:ring-primary focus:ring-2 transition-all" 
              type="checkbox"
              required
            />
            <label class="text-sm text-slate-600 dark:text-slate-400 leading-relaxed" for="terms">
              我已阅读并同意 <a class="text-primary hover:underline" href="#">服务条款</a> 和 <a class="text-primary hover:underline" href="#">隐私政策</a>
            </label>
          </div>
          
          <!-- Register Button -->
          <button 
            type="submit"
            :disabled="loading"
            class="w-full py-4 bg-primary hover:bg-primary/90 text-white font-bold rounded-lg shadow-lg shadow-primary/25 transition-all transform active:scale-[0.98] mt-4 disabled:opacity-50 text-sm"
          >
            {{ loading ? '注册中...' : '立即注册 (Register Now)' }}
          </button>
        </form>
        
        <!-- Footer Link -->
        <div class="mt-8 text-center">
          <p class="text-slate-600 dark:text-slate-400">
            已有账号？ <router-link to="/login" class="text-primary font-bold hover:underline">立即登录</router-link>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { useToast } from '@/composables/useToast'

const router = useRouter()
const userStore = useUserStore()
const toast = useToast()

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  phone: '',
  email: '',
  code: '123456',
  userType: 'JOB_SEEKER',
  agreeTerms: false
})

const loading = ref(false)
const countdown = ref(0)

const sendCode = () => {
  if (countdown.value > 0) return
  
  if (!form.value.username) {
    toast.warning('请先输入手机号或邮箱')
    return
  }
  
  form.value.code = '123456'
  toast.success('演示模式：验证码已自动填充为 123456')
  
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}

const handleRegister = async () => {
  if (!form.value.username || !form.value.password) {
    toast.warning('请填写用户名和密码')
    return
  }
  
  if (form.value.username.length < 3 || form.value.username.length > 50) {
    toast.warning('用户名长度必须在3-50之间')
    return
  }
  
  if (form.value.password.length < 6) {
    toast.warning('密码长度至少6位')
    return
  }
  
  if (form.value.password !== form.value.confirmPassword) {
    toast.error('两次密码输入不一致')
    return
  }
  
  if (!form.value.agreeTerms) {
    toast.warning('请阅读并同意服务条款和隐私政策')
    return
  }
  
  loading.value = true
  try {
    const registerData = {
      username: form.value.username,
      password: form.value.password,
      phone: form.value.phone || null,
      email: form.value.email || null,
      userType: form.value.userType
    }
    
    await userStore.register(registerData)
    toast.success('注册成功！请登录')
    router.push('/login')
  } catch (error) {
    console.error('注册失败', error)
    toast.error(error.message || '注册失败，请重试')
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
