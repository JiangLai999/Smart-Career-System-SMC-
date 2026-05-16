<template>
  <div>
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-6">
      <div>
        <h1 class="text-3xl font-black text-slate-900 dark:text-white">企业审核</h1>
        <p class="text-slate-500 dark:text-slate-400">审核企业入驻申请</p>
      </div>
      <div class="flex gap-2">
        <button 
          v-for="tab in statusTabs" 
          :key="tab.value"
          :class="[
            'px-4 py-2 rounded-lg font-medium text-sm transition-colors',
            activeStatus === tab.value 
              ? 'bg-primary text-white' 
              : 'bg-white dark:bg-slate-900 text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'
          ]"
          @click="activeStatus = tab.value; fetchAudits()"
        >
          {{ tab.label }}
        </button>
      </div>
    </div>
    
    <!-- Audit List -->
    <div class="space-y-4">
      <div 
        v-for="audit in audits" 
        :key="audit.id"
        class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6"
      >
        <div class="flex justify-between items-start mb-4">
          <div class="flex items-center gap-4">
            <div class="size-14 rounded-xl bg-gradient-to-br from-blue-500 to-indigo-600 flex items-center justify-center text-white text-xl font-bold">
              {{ audit.enterpriseName?.charAt(0) || 'E' }}
            </div>
            <div>
              <h3 class="text-lg font-bold text-slate-900 dark:text-white">{{ audit.enterpriseName }}</h3>
              <p class="text-sm text-slate-500">{{ audit.industry }} · {{ audit.scale }}</p>
            </div>
          </div>
          <span :class="getStatusClass(audit.auditStatus)" class="px-3 py-1 rounded-full text-xs font-bold">
            {{ getStatusText(audit.auditStatus) }}
          </span>
        </div>
        
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-4">
          <div>
            <p class="text-xs text-slate-500">统一社会信用代码</p>
            <p class="text-sm font-medium text-slate-900 dark:text-white">{{ audit.licenseNumber || '-' }}</p>
          </div>
          <div>
            <p class="text-xs text-slate-500">联系人</p>
            <p class="text-sm font-medium text-slate-900 dark:text-white">{{ audit.contactPhone || '-' }}</p>
          </div>
          <div>
            <p class="text-xs text-slate-500">邮箱</p>
            <p class="text-sm font-medium text-slate-900 dark:text-white">{{ audit.contactEmail || '-' }}</p>
          </div>
          <div>
            <p class="text-xs text-slate-500">提交时间</p>
            <p class="text-sm font-medium text-slate-900 dark:text-white">{{ formatTime(audit.submitTime) }}</p>
          </div>
        </div>
        
        <div class="mb-4">
          <p class="text-xs text-slate-500 mb-1">企业简介</p>
          <p class="text-sm text-slate-600 dark:text-slate-400">{{ audit.description || '暂无描述' }}</p>
        </div>
        
        <div v-if="audit.auditComment" class="mb-4 p-3 bg-slate-50 dark:bg-slate-800 rounded-lg">
          <p class="text-xs text-slate-500">审核意见</p>
          <p class="text-sm text-slate-900 dark:text-white">{{ audit.auditComment }}</p>
        </div>
        
        <div v-if="audit.auditStatus === 'PENDING'" class="flex gap-3">
          <button 
            @click="handleAudit(audit.id, 'approve')"
            class="flex-1 py-2 bg-green-500 text-white rounded-lg font-bold text-sm hover:bg-green-600 transition-colors"
          >
            通过审核
          </button>
          <button 
            @click="handleAudit(audit.id, 'reject')"
            class="flex-1 py-2 bg-red-500 text-white rounded-lg font-bold text-sm hover:bg-red-600 transition-colors"
          >
            拒绝
          </button>
        </div>
      </div>
      
      <!-- Empty State -->
      <div v-if="audits.length === 0" class="text-center py-12 bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800">
        <span class="material-symbols-outlined text-5xl text-slate-300 dark:text-slate-600 mb-4">fact_check</span>
        <p class="text-slate-500 dark:text-slate-400">暂无待审核的企业</p>
      </div>
    </div>
    
    <!-- Pagination -->
    <div class="flex justify-center mt-6">
      <div class="flex items-center gap-2">
        <button 
          class="p-2 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400"
          :disabled="currentPage === 0"
          @click="changePage(currentPage - 1)"
        >
          <span class="material-symbols-outlined">chevron_left</span>
        </button>
        <span class="px-4 text-sm text-slate-600 dark:text-slate-400">
          第 {{ currentPage + 1 }} 页
        </span>
        <button 
          class="p-2 rounded-lg bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400"
          :disabled="audits.length < pageSize"
          @click="changePage(currentPage + 1)"
        >
          <span class="material-symbols-outlined">chevron_right</span>
        </button>
      </div>
    </div>
    
    <!-- Audit Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-white dark:bg-slate-900 rounded-2xl w-full max-w-md">
        <div class="p-6 border-b border-slate-200 dark:border-slate-800">
          <h3 class="text-xl font-bold text-slate-900 dark:text-white">
            {{ auditAction === 'approve' ? '通过审核' : '拒绝申请' }}
          </h3>
        </div>
        <div class="p-6">
          <label class="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-2">
            审核意见
          </label>
          <textarea 
            v-model="auditComment"
            class="w-full px-4 py-3 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800 text-sm"
            rows="4"
            placeholder="请输入审核意见..."
          ></textarea>
        </div>
        <div class="p-6 border-t border-slate-200 dark:border-slate-800 flex gap-3">
          <button 
            class="flex-1 py-2 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 rounded-lg font-bold text-sm"
            @click="showModal = false"
          >
            取消
          </button>
          <button 
            class="flex-1 py-2 bg-primary text-white rounded-lg font-bold text-sm"
            @click="submitAudit"
          >
            确认
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const audits = ref([])
const currentPage = ref(0)
const pageSize = ref(10)
const activeStatus = ref('PENDING')
const showModal = ref(false)
const auditAction = ref('')
const currentAuditId = ref(null)
const auditComment = ref('')

const statusTabs = [
  { label: '待审核', value: 'PENDING' },
  { label: '已通过', value: 'APPROVED' },
  { label: '已拒绝', value: 'REJECTED' },
  { label: '全部', value: 'ALL' }
]

const fetchAudits = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      status: activeStatus.value
    }
    const response = await api.getAdminAudits(params)
    if (response && response.content) {
      audits.value = response.content
    } else if (Array.isArray(response)) {
      audits.value = response
    }
  } catch (error) {
    console.error('获取审核列表失败', error)
  }
}

const handleAudit = (auditId, action) => {
  currentAuditId.value = auditId
  auditAction.value = action
  auditComment.value = ''
  showModal.value = true
}

const submitAudit = async () => {
  try {
    if (auditAction.value === 'approve') {
      await api.approveAudit(currentAuditId.value, auditComment.value)
      alert('审核已通过')
    } else {
      await api.rejectAudit(currentAuditId.value, auditComment.value)
      alert('审核已拒绝')
    }
    showModal.value = false
    fetchAudits()
  } catch (error) {
    alert('操作失败')
  }
}

const changePage = (page) => {
  currentPage.value = page
  fetchAudits()
}

const getStatusClass = (status) => {
  const classes = {
    'PENDING': 'bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400',
    'APPROVED': 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
    'REJECTED': 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400'
  }
  return classes[status] || 'bg-slate-100 text-slate-700'
}

const getStatusText = (status) => {
  const texts = {
    'PENDING': '待审核',
    'APPROVED': '已通过',
    'REJECTED': '已拒绝'
  }
  return texts[status] || status
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString()
}

onMounted(() => {
  fetchAudits()
})
</script>