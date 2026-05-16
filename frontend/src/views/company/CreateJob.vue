<template>
  <div class="max-w-4xl mx-auto w-full py-12 px-8">
    <div class="mb-10">
      <h2 class="text-3xl font-black tracking-tight">{{ isEditing ? '编辑职位' : '发布新招聘职位' }}</h2>
      <p class="text-slate-500 mt-2">{{ isEditing ? '修改职位信息后点击保存' : '请详细填写以下职位信息，以吸引最合适的人才。' }}</p>
    </div>
    
    <form @submit.prevent="handleSubmit" class="space-y-10">
      <!-- Basic Info -->
      <section class="bg-white dark:bg-slate-900 rounded-xl p-8 shadow-sm border border-slate-200 dark:border-slate-800">
        <h3 class="text-lg font-bold mb-6 flex items-center gap-2">
          <span class="size-2 rounded-full bg-primary"></span>
          基本信息
        </h3>
        <div class="grid grid-cols-2 gap-6">
          <div class="col-span-2">
            <label class="block text-sm font-bold mb-2">职位名称 <span class="text-red-500">*</span></label>
            <input 
              v-model="form.title"
              class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary px-4 py-3" 
              placeholder="例如：高级产品经理" 
              type="text"
            />
          </div>
          <div>
            <label class="block text-sm font-bold mb-2">职位类别 <span class="text-red-500">*</span></label>
            <select 
              v-model="form.category"
              class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary px-4 py-3"
            >
              <option value="">请选择类别</option>
              <option value="技术研发">技术研发</option>
              <option value="产品设计">产品设计</option>
              <option value="市场运营">市场运营</option>
              <option value="职能管理">职能管理</option>
              <option value="销售">销售</option>
              <option value="财务">财务</option>
              <option value="人力资源">人力资源</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-bold mb-2">招聘人数</label>
            <input 
              v-model="form.recruitCount"
              class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary px-4 py-3" 
              placeholder="1" 
              type="number"
              min="1"
            />
          </div>
          <div>
            <label class="block text-sm font-bold mb-2">工作地点 <span class="text-red-500">*</span></label>
            <input 
              v-model="form.location"
              class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary px-4 py-3" 
              placeholder="例如：上海市浦东新区" 
              type="text"
            />
          </div>
          <div>
            <label class="block text-sm font-bold mb-2">学历要求 <span class="text-red-500">*</span></label>
            <select 
              v-model="form.education"
              class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary px-4 py-3"
            >
              <option value="">请选择学历要求</option>
              <option value="大专">大专</option>
              <option value="本科">本科</option>
              <option value="硕士">硕士</option>
              <option value="博士">博士</option>
              <option value="不限">不限</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-bold mb-2">工作经验 <span class="text-red-500">*</span></label>
            <select 
              v-model="form.experience"
              class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary px-4 py-3"
            >
              <option value="">请选择工作经验</option>
              <option value="不限">不限</option>
              <option value="应届生">应届生</option>
              <option value="1-3年">1-3年</option>
              <option value="3-5年">3-5年</option>
              <option value="5-10年">5-10年</option>
              <option value="10年以上">10年以上</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-bold mb-2">薪资范围 (K/月)</label>
            <div class="flex items-center gap-2">
              <input 
                v-model="form.salaryMin"
                class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary px-4 py-3" 
                placeholder="最低" 
                type="number"
              />
              <span class="text-slate-400">-</span>
              <input 
                v-model="form.salaryMax"
                class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary px-4 py-3" 
                placeholder="最高" 
                type="number"
              />
              <span class="text-sm text-slate-500 whitespace-nowrap">K</span>
            </div>
          </div>
          <div>
            <label class="block text-sm font-bold mb-2">截止日期</label>
            <input 
              v-model="form.expireTime"
              class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary px-4 py-3" 
              type="date"
            />
          </div>
        </div>
      </section>
      
      <!-- Content Details -->
      <section class="bg-white dark:bg-slate-900 rounded-xl p-8 shadow-sm border border-slate-200 dark:border-slate-800">
        <h3 class="text-lg font-bold mb-6 flex items-center gap-2">
          <span class="size-2 rounded-full bg-primary"></span>
          职位描述与要求
        </h3>
        <div class="space-y-6">
          <div>
            <label class="block text-sm font-bold mb-2">工作职责 <span class="text-red-500">*</span></label>
            <textarea 
              v-model="form.description"
              class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary p-4" 
              placeholder="请详细描述职位的工作内容和职责范围..." 
              rows="4"
            ></textarea>
          </div>
          <div>
            <label class="block text-sm font-bold mb-2">任职要求 <span class="text-red-500">*</span></label>
            <textarea 
              v-model="form.requirements"
              class="w-full rounded-lg border-slate-200 dark:border-slate-700 bg-transparent focus:ring-primary focus:border-primary p-4" 
              placeholder="请列出必要的技能、经验和学历要求..." 
              rows="4"
            ></textarea>
          </div>
          <div>
            <label class="block text-sm font-bold mb-2">福利待遇</label>
            <div class="flex flex-wrap gap-2">
              <label 
                v-for="benefit in benefitOptions" 
                :key="benefit"
                :class="['inline-flex items-center px-3 py-1.5 rounded-full border text-sm cursor-pointer transition-colors', form.benefits.includes(benefit) ? 'border-primary bg-primary/10 text-primary' : 'border-slate-200 dark:border-slate-700 hover:border-primary']"
              >
                <input 
                  type="checkbox" 
                  :value="benefit" 
                  v-model="form.benefits" 
                  class="hidden" 
                />
                <span>{{ benefit }}</span>
              </label>
            </div>
          </div>
        </div>
      </section>
      
      <!-- Publish Settings -->
      <section class="bg-white dark:bg-slate-900 rounded-xl p-8 shadow-sm border border-slate-200 dark:border-slate-800">
        <h3 class="text-lg font-bold mb-6 flex items-center gap-2">
          <span class="size-2 rounded-full bg-primary"></span>
          发布设置
        </h3>
        <div class="flex items-center justify-between p-4 bg-slate-50 dark:bg-slate-800 rounded-lg">
          <div>
            <p class="font-bold text-sm">职位状态</p>
            <p class="text-xs text-slate-500 mt-1">上线后候选人即可在招聘页面搜索到该职位</p>
          </div>
          <div class="flex items-center gap-2">
            <span :class="['text-sm font-medium', form.isActive ? 'text-slate-400' : 'text-primary']">离线</span>
            <button 
              type="button"
              @click="form.isActive = !form.isActive"
              :class="['relative inline-flex h-6 w-11 items-center rounded-full transition-colors', form.isActive ? 'bg-primary' : 'bg-slate-300']"
            >
              <span :class="['inline-block h-4 w-4 transform rounded-full bg-white transition-transform', form.isActive ? 'translate-x-6' : 'translate-x-1']"></span>
            </button>
            <span :class="['text-sm font-medium', form.isActive ? 'text-primary' : 'text-slate-400']">在线</span>
          </div>
        </div>
      </section>
      
      <div class="flex items-center justify-end gap-4 pb-12">
        <button 
          type="button" 
          @click="$router.back()" 
          class="px-6 py-3 rounded-lg border border-slate-200 dark:border-slate-700 font-bold hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors"
        >
          取消
        </button>
        <button 
          type="button" 
          @click="saveDraft"
          class="px-6 py-3 rounded-lg border border-slate-200 dark:border-slate-700 font-bold hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors"
        >
          保存草稿
        </button>
        <button 
          type="submit" 
          :disabled="loading"
          class="px-10 py-3 rounded-lg bg-primary text-white font-bold hover:bg-primary/90 shadow-lg shadow-primary/20 transition-all disabled:opacity-50"
        >
          {{ loading ? '处理中...' : (isEditing ? '保存修改' : '立即发布职位') }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import api from '@/api'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const isEditing = ref(false)
const jobId = ref(null)

const form = ref({
  title: '',
  category: '',
  recruitCount: 1,
  location: '',
  education: '',
  experience: '',
  salaryMin: null,
  salaryMax: null,
  expireTime: '',
  description: '',
  requirements: '',
  benefits: [],
  isActive: false
})

const benefitOptions = [
  '五险一金', '带薪年假', '定期体检', '年终奖金', 
  '节日福利', '弹性工作', '扁平管理', '股票期权',
  '免费班车', '餐补', '房补', '团队氛围好'
]

onMounted(async () => {
  const id = route.params.id
  if (id) {
    isEditing.value = true
    jobId.value = id
    try {
      const job = await api.getCompanyJob(id)
      if (job) {
        const salaryParts = job.salaryRange ? job.salaryRange.replace(/[Kk]/g, '').split('-') : []
        form.value = {
          title: job.title || '',
          category: job.category || '',
          recruitCount: job.recruitCount || 1,
          location: job.location || '',
          education: job.education || '',
          experience: job.experience || '',
          salaryMin: salaryParts[0] ? parseInt(salaryParts[0]) : null,
          salaryMax: salaryParts[1] ? parseInt(salaryParts[1]) : null,
          expireTime: job.expireTime ? job.expireTime.slice(0, 10) : '',
          description: job.description || '',
          requirements: job.requirements || '',
          benefits: typeof job.benefits === 'string' ? job.benefits.split(',').filter(b => b) : (job.benefits || []),
          isActive: job.status === 'ACTIVE'
        }
      }
    } catch (error) {
      console.error('获取职位详情失败', error)
      alert('获取职位详情失败')
    }
  }
})

const saveDraft = async () => {
  loading.value = true
  try {
    const jobData = buildJobData('DRAFT')
    if (isEditing.value) {
      await api.updateCompanyJob(jobId.value, jobData)
    } else {
      await api.createCompanyJob(jobData)
    }
    alert('草稿保存成功')
    router.push('/company/jobs')
  } catch (error) {
    console.error('保存失败', error)
    alert('保存失败')
  } finally {
    loading.value = false
  }
}

const buildJobData = (status) => {
  const data = {
    title: form.value.title || '',
    category: form.value.category || '',
    recruitCount: form.value.recruitCount || 1,
    location: form.value.location || '',
    education: form.value.education || '',
    experience: form.value.experience || '',
    description: form.value.description || '',
    requirements: form.value.requirements || '',
    status: status || 'PUBLISHED',
    validityDays: 30
  }
  
  if (form.value.salaryMin && form.value.salaryMax) {
    data.salaryRange = `${form.value.salaryMin}K-${form.value.salaryMax}K`
  }
  
  if (form.value.expireTime) {
    const days = Math.ceil((new Date(form.value.expireTime) - new Date()) / (1000 * 60 * 60 * 24))
    if (days > 0) {
      data.validityDays = days
    }
  }
  
  if (Array.isArray(form.value.benefits) && form.value.benefits.length > 0) {
    data.benefits = form.value.benefits
  }
  
  return data
}

const handleSubmit = async () => {
  if (!form.value.title || !form.value.category || !form.value.location || 
      !form.value.education || !form.value.experience || !form.value.description) {
    alert('请填写所有必填信息')
    return
  }
  
  loading.value = true
  try {
    const jobData = buildJobData(form.value.isActive ? 'ACTIVE' : 'DRAFT')
    
    if (isEditing.value) {
      await api.updateCompanyJob(jobId.value, jobData)
    } else {
      await api.createCompanyJob(jobData)
    }
    
    alert(isEditing.value ? '职位修改成功' : '职位发布成功')
    router.push('/company/jobs')
  } catch (error) {
    console.error('发布失败', error)
    const errorMsg = error.response?.data?.message || error.message || '操作失败'
    alert(errorMsg)
  } finally {
    loading.value = false
  }
}
</script>
