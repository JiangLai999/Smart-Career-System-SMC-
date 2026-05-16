<template>
  <div class="max-w-4xl">
    <div class="bg-white rounded-xl border border-slate-200 shadow-sm">
      <div class="p-6 border-b border-slate-100">
        <h2 class="text-xl font-bold text-slate-800">企业资料</h2>
        <p class="text-sm text-slate-500 mt-1">查看和管理企业基本信息</p>
      </div>

      <div class="p-6">
        <div v-if="loading" class="text-center py-8">
          <span class="material-symbols-outlined text-4xl text-slate-300 animate-spin">sync</span>
        </div>

        <div v-else-if="isEditing" class="space-y-6">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="text-sm text-slate-500 mb-1 block">企业名称</label>
              <input v-model="editForm.companyName" type="text" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label class="text-sm text-slate-500 mb-1 block">行业类别</label>
              <select v-model="editForm.industry" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option value="IT">互联网/IT</option>
                <option value="FINANCE">金融/银行</option>
                <option value="EDUCATION">教育培训</option>
                <option value="HEALTHCARE">医疗健康</option>
                <option value="RETAIL">零售/批发</option>
                <option value="MANUFACTURING">制造业</option>
                <option value="REAL_ESTATE">房地产</option>
                <option value="CONSULTING">咨询/专业服务</option>
                <option value="MEDIA">传媒/娱乐</option>
                <option value="OTHER">其他</option>
              </select>
            </div>
            <div>
              <label class="text-sm text-slate-500 mb-1 block">企业规模</label>
              <select v-model="editForm.scale" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option value="SMALL">20人以下</option>
                <option value="MEDIUM_SMALL">20-99人</option>
                <option value="MEDIUM">100-499人</option>
                <option value="MEDIUM_LARGE">500-999人</option>
                <option value="LARGE">1000人以上</option>
              </select>
            </div>
            <div>
              <label class="text-sm text-slate-500 mb-1 block">企业网站</label>
              <input v-model="editForm.website" type="text" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="https://example.com" />
            </div>
          </div>
          <div>
            <label class="text-sm text-slate-500 mb-1 block">企业简介</label>
            <textarea v-model="editForm.description" rows="4" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="请输入企业简介..."></textarea>
          </div>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="text-sm text-slate-500 mb-1 block">联系人</label>
              <input v-model="editForm.contactPerson" type="text" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label class="text-sm text-slate-500 mb-1 block">联系电话</label>
              <input v-model="editForm.contactPhone" type="text" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label class="text-sm text-slate-500 mb-1 block">联系邮箱</label>
              <input v-model="editForm.contactEmail" type="email" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label class="text-sm text-slate-500 mb-1 block">企业地址</label>
              <input v-model="editForm.address" type="text" class="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
          </div>
          <div class="flex gap-3 pt-4">
            <button @click="handleSave" :disabled="saving" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 text-sm font-medium disabled:opacity-50">
              {{ saving ? '保存中...' : '保存修改' }}
            </button>
            <button @click="cancelEdit" class="px-4 py-2 bg-slate-100 text-slate-700 rounded-lg hover:bg-slate-200 text-sm font-medium">
              取消
            </button>
          </div>
        </div>

        <div v-else class="space-y-6">
          <!-- Company Status -->
          <div class="flex items-center justify-between p-4 bg-slate-50 rounded-lg">
            <div class="flex items-center gap-3">
              <span class="material-symbols-outlined text-blue-600">business</span>
              <div>
                <p class="font-medium text-slate-800">{{ companyInfo.companyName }}</p>
                <p class="text-sm text-slate-500">企业认证状态</p>
              </div>
            </div>
            <span :class="['text-sm px-3 py-1 rounded-full', getStatusClass(companyInfo.status)]">
              {{ getStatusText(companyInfo.status) }}
            </span>
          </div>

          <!-- Basic Info -->
          <div>
            <h3 class="font-semibold text-slate-800 mb-4">基本信息</h3>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <p class="text-sm text-slate-500 mb-1">企业名称</p>
                <p class="font-medium text-slate-800">{{ companyInfo.companyName || '-' }}</p>
              </div>
              <div>
                <p class="text-sm text-slate-500 mb-1">营业执照号</p>
                <p class="font-medium text-slate-800">{{ companyInfo.businessLicense || '-' }}</p>
              </div>
              <div>
                <p class="text-sm text-slate-500 mb-1">行业类别</p>
                <p class="font-medium text-slate-800">{{ getIndustryText(companyInfo.industry) }}</p>
              </div>
              <div>
                <p class="text-sm text-slate-500 mb-1">企业规模</p>
                <p class="font-medium text-slate-800">{{ getScaleText(companyInfo.scale) }}</p>
              </div>
              <div>
                <p class="text-sm text-slate-500 mb-1">企业网站</p>
                <p class="font-medium text-slate-800">{{ companyInfo.website || '-' }}</p>
              </div>
              <div>
                <p class="text-sm text-slate-500 mb-1">企业地址</p>
                <p class="font-medium text-slate-800">{{ companyInfo.address || '-' }}</p>
              </div>
            </div>
            <div class="mt-4">
              <p class="text-sm text-slate-500 mb-1">企业简介</p>
              <p class="text-slate-700">{{ companyInfo.description || '暂无简介' }}</p>
            </div>
          </div>

          <!-- Contact Info -->
          <div>
            <h3 class="font-semibold text-slate-800 mb-4">联系人信息</h3>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <p class="text-sm text-slate-500 mb-1">联系人</p>
                <p class="font-medium text-slate-800">{{ companyInfo.contactName || '-' }}</p>
              </div>
              <div>
                <p class="text-sm text-slate-500 mb-1">联系电话</p>
                <p class="font-medium text-slate-800">{{ companyInfo.contactPhone || '-' }}</p>
              </div>
              <div>
                <p class="text-sm text-slate-500 mb-1">联系邮箱</p>
                <p class="font-medium text-slate-800">{{ companyInfo.contactEmail || '-' }}</p>
              </div>
            </div>
          </div>

          <!-- Actions -->
          <div class="pt-4 border-t border-slate-100">
            <button @click="handleEdit" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 text-sm font-medium">
              编辑资料
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const loading = ref(true)
const saving = ref(false)
const isEditing = ref(false)
const companyInfo = ref({})
const editForm = ref({})

onMounted(async () => {
  await fetchCompanyInfo()
})

const fetchCompanyInfo = async () => {
  loading.value = true
  try {
    const data = await api.getCompanyInfo()
    companyInfo.value = data || JSON.parse(localStorage.getItem('companyInfo') || '{}')
  } catch (error) {
    companyInfo.value = JSON.parse(localStorage.getItem('companyInfo') || '{}')
  } finally {
    loading.value = false
  }
}

const handleEdit = () => {
  editForm.value = { ...companyInfo.value }
  isEditing.value = true
}

const cancelEdit = () => {
  isEditing.value = false
}

const handleSave = async () => {
  saving.value = true
  try {
    const updateData = {
      companyName: editForm.value.companyName,
      industry: editForm.value.industry,
      scale: editForm.value.scale,
      description: editForm.value.description,
      contactPerson: editForm.value.contactPerson,
      contactPhone: editForm.value.contactPhone,
      contactEmail: editForm.value.contactEmail,
      address: editForm.value.address,
      website: editForm.value.website
    }
    const updated = await api.updateCompanyInfo(updateData)
    if (updated) {
      companyInfo.value = {
        ...companyInfo.value,
        companyName: updated.companyName || updateData.companyName,
        industry: updated.industry || updateData.industry,
        scale: updated.scale || updateData.scale,
        description: updated.description || updateData.description,
        contactName: updated.contactName || updateData.contactPerson,
        contactPhone: updated.contactPhone || updateData.contactPhone,
        contactEmail: updated.contactEmail || updateData.contactEmail,
        address: updated.address || updateData.address,
        website: updated.website || updateData.website,
        businessLicense: updated.businessLicense || companyInfo.value.businessLicense,
        status: updated.status || companyInfo.value.status
      }
    }
    localStorage.setItem('companyInfo', JSON.stringify({
      id: companyInfo.value.id,
      companyName: companyInfo.value.companyName,
      status: companyInfo.value.status
    }))
    isEditing.value = false
    alert('保存成功')
  } catch (error) {
    console.error('保存失败', error)
    alert('保存失败，请稍后重试')
  } finally {
    saving.value = false
  }
}

const getStatusClass = (status) => {
  const map = {
    'PENDING': 'bg-yellow-100 text-yellow-700',
    'APPROVED': 'bg-green-100 text-green-700',
    'REJECTED': 'bg-red-100 text-red-700'
  }
  return map[status] || 'bg-slate-100 text-slate-600'
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '待审核',
    'APPROVED': '已认证',
    'REJECTED': '已拒绝'
  }
  return map[status] || status
}

const getIndustryText = (industry) => {
  const map = {
    'IT': '互联网/IT',
    'FINANCE': '金融/银行',
    'EDUCATION': '教育培训',
    'HEALTHCARE': '医疗健康',
    'RETAIL': '零售/批发',
    'MANUFACTURING': '制造业',
    'REAL_ESTATE': '房地产',
    'CONSULTING': '咨询/专业服务',
    'MEDIA': '传媒/娱乐',
    'OTHER': '其他'
  }
  return map[industry] || industry || '-'
}

const getScaleText = (scale) => {
  const map = {
    'SMALL': '20人以下',
    'MEDIUM_SMALL': '20-99人',
    'MEDIUM': '100-499人',
    'MEDIUM_LARGE': '500-999人',
    'LARGE': '1000人以上'
  }
  return map[scale] || scale || '-'
}
</script>
