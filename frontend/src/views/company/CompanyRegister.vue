<template>
  <div class="relative flex min-h-screen flex-col bg-background-light dark:bg-background-dark font-display">
    <!-- Top Navigation -->
    <header class="flex items-center justify-between border-b border-slate-200 dark:border-slate-800 bg-white/80 dark:bg-slate-900/80 backdrop-blur-md px-6 md:px-20 py-4 sticky top-0 z-50">
      <div class="flex items-center gap-3">
        <div class="text-primary">
          <span class="material-symbols-outlined text-4xl">corporate_fare</span>
        </div>
        <h2 class="text-xl font-bold tracking-tight">企业注册系统</h2>
      </div>
      <div class="flex items-center gap-8">
        <nav class="hidden md:flex items-center gap-8">
          <a class="text-sm font-medium hover:text-primary transition-colors" href="#">首页</a>
          <a class="text-sm font-medium hover:text-primary transition-colors" href="#">服务指南</a>
          <a class="text-sm font-medium hover:text-primary transition-colors" href="#">政策中心</a>
        </nav>
        <router-link to="/company/login" class="bg-primary hover:bg-primary/90 text-white px-6 py-2 rounded-lg text-sm font-bold transition-all shadow-md shadow-primary/20">
          登录
        </router-link>
      </div>
    </header>
    
    <main class="flex-1 flex flex-col md:flex-row w-full max-w-7xl mx-auto p-4 md:p-10 gap-10">
      <!-- Left Side: Hero / Info Section -->
      <div class="hidden lg:flex flex-col w-2/5 gap-8 sticky top-28 h-fit">
        <div class="relative aspect-[4/5] rounded-xl overflow-hidden shadow-2xl group" style='background-image: linear-gradient(180deg, rgba(0,0,0,0) 40%, rgba(19,127,236,0.8) 100%), url("https://images.unsplash.com/photo-1497366216548-37526070297c?w=800"); background-size: cover; background-position: center;'>
          <div class="absolute bottom-0 left-0 p-8 text-white">
            <span class="bg-white/20 backdrop-blur-md px-3 py-1 rounded-full text-xs font-bold mb-4 inline-block">数字化转型</span>
            <h1 class="text-3xl font-bold leading-tight mb-4">加入我们的企业生态圈</h1>
            <p class="text-white/80 leading-relaxed">连接全球资源，享受一站式企业数字化服务。我们将协助您的企业完成快速合规注册。</p>
          </div>
        </div>
        <div class="grid grid-cols-2 gap-4">
          <div class="bg-white dark:bg-slate-800 p-4 rounded-xl shadow-sm border border-slate-100 dark:border-slate-700">
            <span class="material-symbols-outlined text-primary mb-2">verified_user</span>
            <h4 class="font-bold text-sm">快速审核</h4>
            <p class="text-xs text-slate-500">24小时内完成初步审核</p>
          </div>
          <div class="bg-white dark:bg-slate-800 p-4 rounded-xl shadow-sm border border-slate-100 dark:border-slate-700">
            <span class="material-symbols-outlined text-primary mb-2">security</span>
            <h4 class="font-bold text-sm">信息加密</h4>
            <p class="text-xs text-slate-500">银行级数据安全保障</p>
          </div>
        </div>
      </div>
      
      <!-- Right Side: Registration Form -->
      <div class="flex-1 bg-white dark:bg-slate-900 rounded-2xl shadow-xl border border-slate-100 dark:border-slate-800 overflow-hidden">
        <div class="p-8 border-b border-slate-100 dark:border-slate-800">
          <div>
            <h2 class="text-2xl font-bold">企业入驻申请</h2>
            <p class="text-slate-500 text-sm mt-1">请填写以下信息以开始您的企业注册流程</p>
          </div>
        </div>
        
        <form @submit.prevent="handleRegister" class="p-8 space-y-8 custom-scrollbar max-h-[70vh] overflow-y-auto">
          <!-- Section: Basic Info -->
          <div>
            <div class="flex items-center gap-2 mb-6">
              <span class="w-1 h-6 bg-primary rounded-full"></span>
              <h3 class="font-bold text-lg">基本工商信息</h3>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="flex flex-col gap-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">公司法定全称</label>
                <input 
                  v-model="form.companyName"
                  class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all p-3 text-sm" 
                  placeholder="请输入营业执照上的名称" 
                  type="text"
                  required
                />
              </div>
              <div class="flex flex-col gap-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">统一社会信用代码</label>
                <input 
                  v-model="form.businessLicense"
                  class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all p-3 text-sm" 
                  placeholder="18位营业执照注册号" 
                  type="text"
                  required
                />
              </div>
              <div class="flex flex-col gap-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">所属行业</label>
                <select 
                  v-model="form.industry"
                  class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all p-3 text-sm"
                >
                  <option value="">请选择行业分类</option>
                  <option value="IT">信息技术/互联网</option>
                  <option value="MANUFACTURING">制造业</option>
                  <option value="FINANCE">金融贸易</option>
                  <option value="MEDIA">文化传媒</option>
                  <option value="OTHER">其他</option>
                </select>
              </div>
              <div class="flex flex-col gap-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">企业规模</label>
                <div class="grid grid-cols-2 gap-2">
                  <button 
                    type="button"
                    :class="['border rounded-lg py-2 text-xs font-bold transition-all', form.scale === 'SMALL' ? 'border-primary bg-primary/5 text-primary' : 'border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800']"
                    @click="form.scale = 'SMALL'"
                  >
                    50人以下
                  </button>
                  <button 
                    type="button"
                    :class="['border rounded-lg py-2 text-xs font-bold transition-all', form.scale === 'MEDIUM' ? 'border-primary bg-primary/5 text-primary' : 'border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800']"
                    @click="form.scale = 'MEDIUM'"
                  >
                    50-200人
                  </button>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Section: Contact Info -->
          <div>
            <div class="flex items-center gap-2 mb-6 pt-4 border-t border-slate-100 dark:border-slate-800">
              <span class="w-1 h-6 bg-primary rounded-full"></span>
              <h3 class="font-bold text-lg">联系人信息</h3>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div class="flex flex-col gap-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">主要联系人姓名</label>
                <input 
                  v-model="form.contactName"
                  class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all p-3 text-sm" 
                  placeholder="请输入姓名" 
                  type="text"
                  required
                />
              </div>
              <div class="flex flex-col gap-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">手机号码</label>
                <div class="flex">
                  <span class="inline-flex items-center px-3 rounded-l-lg border border-r-0 border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-slate-500 text-sm">+86</span>
                  <input 
                    v-model="form.contactPhone"
                    class="w-full rounded-r-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all p-3 text-sm" 
                    placeholder="11位手机号" 
                    type="tel"
                    required
                  />
                </div>
              </div>
              <div class="flex flex-col gap-2 md:col-span-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">企业邮箱</label>
                <input 
                  v-model="form.contactEmail"
                  class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all p-3 text-sm" 
                  placeholder="example@company.com" 
                  type="email"
                />
              </div>
              <div class="flex flex-col gap-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">登录用户名</label>
                <input 
                  v-model="form.username"
                  class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all p-3 text-sm" 
                  placeholder="用于登录的用户名" 
                  type="text"
                  required
                />
              </div>
              <div class="flex flex-col gap-2">
                <label class="text-sm font-semibold text-slate-700 dark:text-slate-300">设置密码</label>
                <input 
                  v-model="form.password"
                  class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all p-3 text-sm" 
                  placeholder="8-16位大小写字母及数字组合" 
                  type="password"
                  required
                />
              </div>
            </div>
          </div>
          
          <!-- Agreement -->
          <div class="flex items-start gap-3 mt-6">
            <input 
              v-model="agreed" 
              class="mt-1 rounded border-slate-300 text-primary focus:ring-primary" 
              type="checkbox"
            />
            <p class="text-xs text-slate-500 leading-relaxed">
              我已阅读并同意 <a class="text-primary hover:underline" href="#">《企业注册服务协议》</a> 和 <a class="text-primary hover:underline" href="#">《隐私权政策》</a>。我确认所提供的信息真实有效。
            </p>
          </div>
        </form>
        
        <!-- Form Footer Actions -->
        <div class="p-8 bg-slate-50 dark:bg-slate-800/50 flex flex-col md:flex-row justify-between items-center gap-4">
          <router-link to="/company/login" class="text-slate-500 text-sm font-bold flex items-center gap-2 hover:text-slate-700 transition-colors order-2 md:order-1">
            <span class="material-symbols-outlined text-lg">arrow_back</span>
            返回登录
          </router-link>
          <button 
            @click="handleRegister"
            :disabled="loading || !agreed"
            class="w-full md:w-auto bg-primary hover:bg-primary/90 text-white px-10 py-4 rounded-xl font-bold transition-all shadow-lg shadow-primary/30 flex items-center justify-center gap-2 order-1 md:order-2 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ loading ? '提交中...' : '提交注册申请' }}
            <span class="material-symbols-outlined">send</span>
          </button>
        </div>
      </div>
    </main>
    
    <footer class="py-8 px-6 md:px-20 text-center">
      <p class="text-xs text-slate-400">© 2024 企业注册系统. 版权所有. 浙ICP备12345678号</p>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'

const router = useRouter()

const loading = ref(false)
const agreed = ref(false)

const form = ref({
  companyName: '',
  businessLicense: '',
  industry: '',
  scale: '',
  contactName: '',
  contactPhone: '',
  contactEmail: '',
  username: '',
  password: ''
})

const handleRegister = async () => {
  if (!form.value.companyName || !form.value.businessLicense || !form.value.industry || 
      !form.value.scale || !form.value.contactName || !form.value.contactPhone || 
      !form.value.username || !form.value.password) {
    alert('请填写所有必填信息')
    return
  }
  
  if (form.value.password.length < 8) {
    alert('密码长度至少8位')
    return
  }
  
  loading.value = true
  try {
    await api.companyRegister({
      companyName: form.value.companyName,
      businessLicense: form.value.businessLicense,
      industry: form.value.industry,
      scale: form.value.scale,
      contactName: form.value.contactName,
      contactPhone: form.value.contactPhone,
      contactEmail: form.value.contactEmail,
      username: form.value.username,
      password: form.value.password
    })
    alert('注册申请已提交，预计1-3个工作日内完成审核')
    alert('请先登录企业账户以继续注册')
  } catch (error) {
    console.error('注册失败', error)
    const errorMsg = error.response?.data?.message || error.message || '注册失败，请稍后重试'
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
.text-primary {
  color: #137fec;
}
.bg-primary {
  background-color: #137fec;
}
.shadow-primary\/20 {
  --tw-shadow-color: rgb(19 127 236 / 0.2);
  --tw-shadow: var(--tw-shadow-colored);
}
.shadow-primary\/30 {
  --tw-shadow-color: rgb(19 127 236 / 0.3);
  --tw-shadow: var(--tw-shadow-colored);
}
</style>
