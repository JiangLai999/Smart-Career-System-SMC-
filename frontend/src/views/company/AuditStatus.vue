<template>
  <div class="max-w-3xl mx-auto space-y-6">

    <!-- 页面标题 -->
    <div>
      <h2 class="text-2xl font-bold text-slate-900">注册审核进度</h2>
      <p class="text-slate-500 mt-1 text-sm">查看您的企业注册审核状态与详情</p>
    </div>

    <!-- 加载中 -->
    <div v-if="loading" class="flex justify-center items-center py-24">
      <span class="material-symbols-outlined text-4xl text-slate-300 animate-spin">sync</span>
    </div>

    <template v-else>

      <!-- 企业信息卡片 -->
      <div class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <!-- 卡头：企业名称 + 状态 -->
        <div class="flex items-center justify-between px-6 py-5 border-b border-slate-100">
          <div class="flex items-center gap-3">
            <div class="w-11 h-11 bg-gradient-to-br from-blue-500 to-indigo-600 rounded-xl flex items-center justify-center">
              <span class="material-symbols-outlined text-white text-xl">business</span>
            </div>
            <div>
              <h3 class="text-lg font-bold text-slate-900">{{ companyInfo.companyName || '企业名称' }}</h3>
              <p class="text-xs text-slate-500 mt-0.5">统一社会信用代码：{{ companyInfo.businessLicense || '-' }}</p>
            </div>
          </div>
          <div :class="['flex items-center gap-2 px-4 py-2 rounded-full text-sm font-bold', statusBadgeClass]">
            <span class="material-symbols-outlined text-base">{{ statusIcon }}</span>
            {{ statusText }}
          </div>
        </div>

        <!-- 企业基本信息 -->
        <div class="grid grid-cols-2 gap-px bg-slate-100">
          <div class="bg-white px-5 py-4">
            <p class="text-xs text-slate-400 font-medium uppercase tracking-wide mb-1">所属行业</p>
            <p class="text-sm font-medium text-slate-800">{{ companyInfo.industry || '-' }}</p>
          </div>
          <div class="bg-white px-5 py-4">
            <p class="text-xs text-slate-400 font-medium uppercase tracking-wide mb-1">企业规模</p>
            <p class="text-sm font-medium text-slate-800">{{ companyInfo.scale || '-' }}</p>
          </div>
          <div class="bg-white px-5 py-4">
            <p class="text-xs text-slate-400 font-medium uppercase tracking-wide mb-1">联系人</p>
            <p class="text-sm font-medium text-slate-800">{{ companyInfo.contactName || '-' }}</p>
          </div>
          <div class="bg-white px-5 py-4">
            <p class="text-xs text-slate-400 font-medium uppercase tracking-wide mb-1">联系电话</p>
            <p class="text-sm font-medium text-slate-800">{{ companyInfo.contactPhone || '-' }}</p>
          </div>
          <div class="bg-white px-5 py-4">
            <p class="text-xs text-slate-400 font-medium uppercase tracking-wide mb-1">联系邮箱</p>
            <p class="text-sm font-medium text-slate-800">{{ companyInfo.contactEmail || '-' }}</p>
          </div>
          <div class="bg-white px-5 py-4">
            <p class="text-xs text-slate-400 font-medium uppercase tracking-wide mb-1">提交时间</p>
            <p class="text-sm font-medium text-slate-800">{{ formatDate(companyInfo.createTime) }}</p>
          </div>
        </div>

        <!-- 审核时间（仅审核完成时显示） -->
        <div v-if="auditStatus !== 0" class="px-6 py-4 border-t border-slate-100">
          <div class="flex items-center gap-2 text-sm">
            <span class="material-symbols-outlined text-base text-slate-400">schedule</span>
            <span class="text-slate-500">审核时间：</span>
            <span class="font-medium text-slate-800">{{ formatDate(statusData.auditTime) || '-' }}</span>
          </div>
        </div>
      </div>

      <!-- 审核流程时间线 -->
      <div class="bg-white rounded-2xl border border-slate-200 shadow-sm p-6">
        <h4 class="font-bold text-slate-800 mb-6">审核流程</h4>

        <div class="space-y-0">
          <!-- Step 1: 信息填写 -->
          <div class="flex gap-4">
            <div class="flex flex-col items-center">
              <div class="w-9 h-9 rounded-full bg-green-500 flex items-center justify-center flex-shrink-0">
                <span class="material-symbols-outlined text-white text-lg">check</span>
              </div>
              <div class="w-0.5 h-8 bg-slate-200 mt-1"></div>
            </div>
            <div class="pb-8 pt-1">
              <p class="font-semibold text-slate-800">信息填写完成</p>
              <p class="text-sm text-slate-500 mt-0.5">企业基本信息填写并提交注册申请</p>
              <span class="inline-block mt-2 text-xs text-green-600 bg-green-50 px-2 py-0.5 rounded-full font-medium">已完成</span>
            </div>
          </div>

          <!-- Step 2: 资质审核 -->
          <div class="flex gap-4">
            <div class="flex flex-col items-center">
              <div :class="['w-9 h-9 rounded-full flex items-center justify-center flex-shrink-0', step2BubbleClass]">
                <span class="material-symbols-outlined text-white text-lg">{{ step2Icon }}</span>
              </div>
              <div v-if="auditStatus !== 2" class="w-0.5 h-8 bg-slate-200 mt-1"></div>
              <div v-else class="w-0.5 h-8 bg-red-200 mt-1"></div>
            </div>
            <div class="pb-8 pt-1">
              <p class="font-semibold text-slate-800">资质审核</p>
              <p class="text-sm text-slate-500 mt-0.5">管理员核查企业营业执照及资质信息真实性</p>
              <span v-if="auditStatus === 0" class="inline-block mt-2 text-xs text-amber-600 bg-amber-50 px-2 py-0.5 rounded-full font-medium animate-pulse">审核中...</span>
              <span v-else-if="auditStatus === 1" class="inline-block mt-2 text-xs text-green-600 bg-green-50 px-2 py-0.5 rounded-full font-medium">已通过</span>
              <span v-else class="inline-block mt-2 text-xs text-red-600 bg-red-50 px-2 py-0.5 rounded-full font-medium">未通过</span>
            </div>
          </div>

          <!-- Step 3: 审核完成 -->
          <div class="flex gap-4">
            <div class="flex flex-col items-center">
              <div :class="['w-9 h-9 rounded-full flex items-center justify-center flex-shrink-0', step3BubbleClass]">
                <span class="material-symbols-outlined text-white text-lg">verified</span>
              </div>
            </div>
            <div class="pt-1">
              <p class="font-semibold text-slate-800">审核完成</p>
              <p class="text-sm text-slate-500 mt-0.5">审核通过后即可发布职位开始招聘</p>
              <span v-if="auditStatus === 1" class="inline-block mt-2 text-xs text-green-600 bg-green-50 px-2 py-0.5 rounded-full font-medium">已完成，可开始使用</span>
              <span v-else-if="auditStatus === 2" class="inline-block mt-2 text-xs text-red-500 bg-red-50 px-2 py-0.5 rounded-full font-medium">审核未通过</span>
              <span v-else class="inline-block mt-2 text-xs text-slate-400 bg-slate-50 px-2 py-0.5 rounded-full font-medium">等待审核</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 状态详情提示卡 -->

      <!-- 审核中 -->
      <div v-if="auditStatus === 0" class="bg-blue-50 border border-blue-200 rounded-2xl p-6">
        <div class="flex items-start gap-4">
          <div class="w-10 h-10 bg-blue-100 rounded-xl flex items-center justify-center flex-shrink-0">
            <span class="material-symbols-outlined text-blue-600">pending</span>
          </div>
          <div class="flex-1">
            <p class="font-bold text-blue-800">审核进行中</p>
            <p class="text-sm text-blue-600 mt-1">您的企业注册申请已提交，管理员将在 <strong>1-3 个工作日</strong> 内完成审核。</p>
            <ul class="mt-3 space-y-1.5 text-sm text-blue-600">
              <li class="flex items-center gap-2"><span class="material-symbols-outlined text-base text-blue-400">info</span>审核期间请保持联系电话畅通</li>
              <li class="flex items-center gap-2"><span class="material-symbols-outlined text-base text-blue-400">info</span>如有疑问可前往企业资料页补充完善信息</li>
              <li class="flex items-center gap-2"><span class="material-symbols-outlined text-base text-blue-400">info</span>审核结果将通过站内通知推送，请关注通知中心</li>
            </ul>
          </div>
        </div>
      </div>

      <!-- 审核通过 -->
      <div v-if="auditStatus === 1" class="bg-green-50 border border-green-200 rounded-2xl p-6">
        <div class="flex items-start gap-4">
          <div class="w-10 h-10 bg-green-100 rounded-xl flex items-center justify-center flex-shrink-0">
            <span class="material-symbols-outlined text-green-600">check_circle</span>
          </div>
          <div class="flex-1">
            <p class="font-bold text-green-800">审核已通过，恭喜！</p>
            <p class="text-sm text-green-600 mt-1">您的企业已完成认证，现在可以发布职位并开始招聘。</p>
            <div class="flex gap-3 mt-4">
              <router-link
                to="/company/jobs/create"
                class="inline-flex items-center gap-2 bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg text-sm font-bold transition-colors"
              >
                <span class="material-symbols-outlined text-base">add_circle</span>
                发布职位
              </router-link>
              <router-link
                to="/company/dashboard"
                class="inline-flex items-center gap-2 border border-green-300 text-green-700 hover:bg-green-100 px-4 py-2 rounded-lg text-sm font-medium transition-colors"
              >
                <span class="material-symbols-outlined text-base">dashboard</span>
                进入仪表盘
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 审核未通过 -->
      <div v-if="auditStatus === 2" class="bg-red-50 border border-red-200 rounded-2xl p-6">
        <div class="flex items-start gap-4">
          <div class="w-10 h-10 bg-red-100 rounded-xl flex items-center justify-center flex-shrink-0">
            <span class="material-symbols-outlined text-red-600">cancel</span>
          </div>
          <div class="flex-1">
            <p class="font-bold text-red-800">审核未通过</p>
            <div v-if="auditComment" class="mt-2 bg-white border border-red-200 rounded-lg px-4 py-3">
              <p class="text-xs text-red-400 font-medium uppercase mb-1">审核意见</p>
              <p class="text-sm text-red-700">{{ auditComment }}</p>
            </div>
            <p class="text-sm text-red-600 mt-3">请根据审核意见修改企业信息后重新提交。</p>
            <div class="flex gap-3 mt-4">
              <router-link
                to="/company/profile"
                class="inline-flex items-center gap-2 bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg text-sm font-bold transition-colors"
              >
                <span class="material-symbols-outlined text-base">edit</span>
                修改企业信息
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 底部操作 -->
      <div class="flex items-center justify-between pt-1">
        <router-link
          to="/company/dashboard"
          class="flex items-center gap-2 text-slate-500 text-sm font-medium hover:text-slate-700 transition-colors"
        >
          <span class="material-symbols-outlined text-lg">arrow_back</span>
          返回仪表盘
        </router-link>
        <button
          @click="refreshStatus"
          :disabled="loading"
          class="flex items-center gap-2 text-blue-600 text-sm font-medium hover:text-blue-700 transition-colors disabled:opacity-50"
        >
          <span :class="['material-symbols-outlined text-lg', refreshing ? 'animate-spin' : '']">refresh</span>
          刷新状态
        </button>
      </div>

    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'

const router = useRouter()

const loading = ref(true)
const refreshing = ref(false)
const auditStatus = ref(0)   // 0=待审核, 1=通过, 2=拒绝
const auditComment = ref('')
const companyInfo = ref({})
const statusData = ref({})

let autoRefreshTimer = null

onMounted(() => {
  fetchAuditStatus()
})

onUnmounted(() => {
  if (autoRefreshTimer) clearInterval(autoRefreshTimer)
})

const fetchAuditStatus = async (silent = false) => {
  if (!silent) loading.value = true
  else refreshing.value = true
  try {
    const [info, status] = await Promise.all([
      api.getCompanyInfo(),
      api.getCompanyRegisterStatus()
    ])
    companyInfo.value = info || {}
    statusData.value = status || {}

    // auditStatus 从 register-status 返回 Integer，兼容 string 形式
    const raw = status?.auditStatus
    if (raw === 1 || raw === '1') {
      auditStatus.value = 1
    } else if (raw === 2 || raw === '2') {
      auditStatus.value = 2
    } else {
      auditStatus.value = 0
    }
    auditComment.value = status?.auditComment || ''

    // 待审核时每 30 秒自动刷新
    if (auditStatus.value === 0 && !autoRefreshTimer) {
      autoRefreshTimer = setInterval(() => fetchAuditStatus(true), 30000)
    } else if (auditStatus.value !== 0 && autoRefreshTimer) {
      clearInterval(autoRefreshTimer)
      autoRefreshTimer = null
    }
  } catch (error) {
    console.error('获取审核状态失败', error)
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

const refreshStatus = () => {
  fetchAuditStatus(true)
}

// ---- 计算属性 ----

const statusText = computed(() => {
  if (auditStatus.value === 1) return '审核通过'
  if (auditStatus.value === 2) return '审核未通过'
  return '审核中'
})

const statusIcon = computed(() => {
  if (auditStatus.value === 1) return 'verified'
  if (auditStatus.value === 2) return 'cancel'
  return 'pending'
})

const statusBadgeClass = computed(() => {
  if (auditStatus.value === 1) return 'bg-green-100 text-green-700'
  if (auditStatus.value === 2) return 'bg-red-100 text-red-700'
  return 'bg-amber-100 text-amber-700'
})

const step2BubbleClass = computed(() => {
  if (auditStatus.value === 0) return 'bg-amber-400'
  if (auditStatus.value === 1) return 'bg-green-500'
  return 'bg-red-500'
})

const step2Icon = computed(() => {
  if (auditStatus.value === 0) return 'hourglass_top'
  if (auditStatus.value === 1) return 'check'
  return 'close'
})

const step3BubbleClass = computed(() => {
  if (auditStatus.value === 1) return 'bg-green-500'
  return 'bg-slate-300'
})

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
  })
}
</script>
