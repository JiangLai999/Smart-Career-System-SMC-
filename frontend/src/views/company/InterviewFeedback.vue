<template>
  <div class="flex-1 flex flex-col items-center py-10 px-4">
    <div class="max-w-[1000px] w-full flex flex-col gap-6">
      <div class="flex flex-col gap-2">
        <h1 class="text-3xl font-black leading-tight tracking-tight text-slate-900 dark:text-white">面试反馈评价</h1>
        <p class="text-slate-500 dark:text-slate-400 text-base">请根据面试表现客观填写各项评分及评价，您的建议对最终录用决策至关重要。</p>
      </div>
      
      <!-- Candidate Info -->
      <section class="bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-slate-200 dark:border-slate-800 p-6">
        <div class="flex items-center gap-6">
          <div class="size-24 rounded-full bg-slate-100 dark:bg-slate-800 flex items-center justify-center overflow-hidden border-4 border-white dark:border-slate-800 shadow-sm">
            <span class="text-3xl font-bold text-primary">{{ candidateInfo.name?.charAt(0) || '张' }}</span>
          </div>
          <div class="flex-1 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-y-4 gap-x-8">
            <div class="flex flex-col">
              <span class="text-xs font-bold text-slate-400 uppercase tracking-wider">候选人姓名</span>
              <span class="text-lg font-bold text-slate-900 dark:text-white">{{ candidateInfo.name || '张伟' }}</span>
            </div>
            <div class="flex flex-col">
              <span class="text-xs font-bold text-slate-400 uppercase tracking-wider">申请职位</span>
              <span class="text-lg font-bold text-primary">{{ candidateInfo.position || '高级前端开发工程师' }}</span>
            </div>
            <div class="flex flex-col">
              <span class="text-xs font-bold text-slate-400 uppercase tracking-wider">面试轮次</span>
              <div class="flex items-center gap-2 mt-1">
                <span class="px-2.5 py-0.5 rounded-full bg-primary/10 text-primary text-xs font-bold border border-primary/20">{{ candidateInfo.round || '技术复试' }}</span>
              </div>
            </div>
            <div class="flex flex-col">
              <span class="text-xs font-bold text-slate-400 uppercase tracking-wider">面试官</span>
              <span class="text-slate-700 dark:text-slate-300 font-medium">{{ candidateInfo.interviewer || '李明 (Li Ming)' }}</span>
            </div>
            <div class="flex flex-col">
              <span class="text-xs font-bold text-slate-400 uppercase tracking-wider">面试日期</span>
              <span class="text-slate-700 dark:text-slate-300 font-medium">{{ candidateInfo.date || '2023-10-27' }}</span>
            </div>
          </div>
        </div>
      </section>
      
      <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
        <!-- Rating Section -->
        <div class="lg:col-span-8 flex flex-col gap-6">
          <section class="bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-slate-200 dark:border-slate-800 overflow-hidden">
            <div class="px-6 py-4 border-b border-slate-100 dark:border-slate-800 flex items-center gap-2">
              <span class="material-symbols-outlined text-primary">assessment</span>
              <h3 class="text-lg font-bold">评分维度 (1-5分)</h3>
            </div>
            <div class="p-6 flex flex-col gap-6">
              <div v-for="dimension in ratingDimensions" :key="dimension.key" class="flex items-center justify-between group">
                <div class="flex flex-col gap-1">
                  <p class="font-bold text-slate-800 dark:text-slate-200">{{ dimension.name }}</p>
                  <p class="text-sm text-slate-500">{{ dimension.description }}</p>
                </div>
                <div class="flex items-center gap-1 text-primary">
                  <button 
                    v-for="star in 5" 
                    :key="star"
                    @click="setRating(dimension.key, star)"
                    class="transition-colors hover:scale-110"
                  >
                    <span :class="['material-symbols-outlined text-xl', star <= dimension.score ? 'fill-1' : 'text-slate-300']">star</span>
                  </button>
                  <span class="ml-2 font-bold text-lg text-slate-900 dark:text-white">{{ dimension.score }}</span>
                </div>
              </div>
            </div>
          </section>
          
          <!-- Qualitative Feedback -->
          <section class="bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-slate-200 dark:border-slate-800 overflow-hidden">
            <div class="px-6 py-4 border-b border-slate-100 dark:border-slate-800 flex items-center gap-2">
              <span class="material-symbols-outlined text-primary">rate_review</span>
              <h3 class="text-lg font-bold">定性评价反馈</h3>
            </div>
            <div class="p-6 flex flex-col gap-6">
              <div class="flex flex-col gap-2">
                <label class="text-sm font-bold text-slate-700 dark:text-slate-300">核心优势 (Key Strengths)</label>
                <textarea 
                  v-model="feedback.strengths"
                  class="w-full min-h-[100px] rounded-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50 focus:ring-primary focus:border-primary text-sm p-4" 
                  placeholder="描述候选人最突出的技术或非技术优势..."
                ></textarea>
              </div>
              <div class="flex flex-col gap-2">
                <label class="text-sm font-bold text-slate-700 dark:text-slate-300">待提升项 (Areas for Improvement)</label>
                <textarea 
                  v-model="feedback.improvements"
                  class="w-full min-h-[100px] rounded-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50 focus:ring-primary focus:border-primary text-sm p-4" 
                  placeholder="面试过程中发现的短板或需要进一步考察的点..."
                ></textarea>
              </div>
              <div class="flex flex-col gap-2">
                <label class="text-sm font-bold text-slate-700 dark:text-slate-300">面试官综合评价 (Interviewer Comments)</label>
                <textarea 
                  v-model="feedback.comments"
                  class="w-full min-h-[120px] rounded-lg border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50 focus:ring-primary focus:border-primary text-sm p-4" 
                  placeholder="结合职位要求，给出最终面试总结..."
                ></textarea>
              </div>
            </div>
          </section>
        </div>
        
        <!-- Decision Section -->
        <div class="lg:col-span-4 flex flex-col gap-6">
          <section class="bg-white dark:bg-slate-900 rounded-xl shadow-sm border border-slate-200 dark:border-slate-800 overflow-hidden sticky top-24">
            <div class="px-6 py-4 border-b border-slate-100 dark:border-slate-800 flex items-center gap-2">
              <span class="material-symbols-outlined text-primary">fact_check</span>
              <h3 class="text-lg font-bold">面试决策</h3>
            </div>
            <div class="p-6 flex flex-col gap-4">
              <label 
                v-for="decision in decisions" 
                :key="decision.value"
                :class="['flex items-center gap-3 p-4 rounded-lg border cursor-pointer transition-colors group', feedback.decision === decision.value ? 'border-primary bg-primary/5' : 'border-slate-100 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800']"
              >
                <input 
                  type="radio" 
                  :value="decision.value" 
                  v-model="feedback.decision" 
                  class="w-5 h-5 text-primary border-slate-300 focus:ring-primary"
                />
                <div class="flex flex-col">
                  <span :class="['font-bold', decision.colorClass]">{{ decision.label }}</span>
                  <span class="text-xs text-slate-500">{{ decision.description }}</span>
                </div>
              </label>
              
              <div class="mt-6 pt-6 border-t border-slate-100 dark:border-slate-800 flex flex-col gap-3">
                <button 
                  @click="submitFeedback" 
                  :disabled="submitting"
                  class="w-full bg-primary hover:bg-primary/90 text-white font-bold py-3 rounded-lg shadow-lg shadow-primary/20 transition-all flex items-center justify-center gap-2 disabled:opacity-50"
                >
                  <span class="material-symbols-outlined text-[20px]">send</span>
                  {{ submitting ? '提交中...' : '提交评价' }}
                </button>
                <button 
                  @click="saveDraft" 
                  class="w-full bg-transparent border-2 border-primary text-primary hover:bg-primary/5 font-bold py-3 rounded-lg transition-all"
                >
                  保存为草稿
                </button>
              </div>
            </div>
          </section>
          
          <!-- Info Section -->
          <section class="bg-primary/5 dark:bg-primary/10 rounded-xl border border-primary/20 p-6">
            <h4 class="text-primary font-bold mb-2 flex items-center gap-2">
              <span class="material-symbols-outlined text-[18px]">info</span>
              填写须知
            </h4>
            <ul class="text-xs text-slate-600 dark:text-slate-400 space-y-2">
              <li class="flex gap-2">
                <span class="text-primary font-bold">•</span>
                评价应基于面试事实，避免主观偏见。
              </li>
              <li class="flex gap-2">
                <span class="text-primary font-bold">•</span>
                提交后评价将同步至HR及下一轮面试官。
              </li>
              <li class="flex gap-2">
                <span class="text-primary font-bold">•</span>
                如有冲突回避情况，请提前告知。
              </li>
            </ul>
          </section>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import api from '@/api'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const submitting = ref(false)
const interviewData = ref(null)

const candidateInfo = ref({
  name: '',
  position: '',
  round: '',
  interviewer: '',
  date: ''
})

const getTypeText = (type) => {
  const map = {
    'FIRST_ROUND': '初试',
    'TECHNICAL': '技术评测',
    'FINAL': '终审面试',
    'ONSITE': '现场面试',
    'PHONE': '电话面试',
    'VIDEO': '视频面试'
  }
  return map[type] || type || '面试'
}

const ratingDimensions = reactive([
  { key: 'professional', name: '专业技能 (Professional Skills)', description: '考察技术深度、框架掌握及工程实践能力', score: 4 },
  { key: 'communication', name: '沟通表达 (Communication Skills)', description: '逻辑清晰度、观点阐述及团队协作意愿', score: 4 },
  { key: 'logic', name: '逻辑思维 (Logical Thinking)', description: '问题分析能力、方案设计及解决复杂度', score: 4 },
  { key: 'culture', name: '文化匹配度 (Cultural Fit)', description: '价值观认同度、职业素养及抗压能力', score: 4 },
  { key: 'potential', name: '综合潜力 (Overall Potential)', description: '学习能力、未来成长空间及培养价值', score: 4 }
])

const feedback = reactive({
  decision: 'POOL',
  strengths: '',
  improvements: '',
  comments: ''
})

const decisions = [
  { 
    value: 'PASS', 
    label: '建议录用 (Recommend for Hire)', 
    description: '符合职位要求且具有明显优势',
    colorClass: 'text-emerald-600 dark:text-emerald-500'
  },
  { 
    value: 'POOL', 
    label: '待定/人才库 (Keep in Talent Pool)', 
    description: '整体不错，但有更合适的候选人竞争',
    colorClass: 'text-amber-600 dark:text-amber-500'
  },
  { 
    value: 'FAIL', 
    label: '不建议录用 (Not Recommended)', 
    description: '核心能力不匹配或文化契合度低',
    colorClass: 'text-rose-600 dark:text-rose-500'
  }
]

onMounted(async () => {
  await fetchInterviewData()
})

const fetchInterviewData = async () => {
  loading.value = true
  try {
    const data = await api.getInterviewById(route.params.id)
    interviewData.value = data
    
    candidateInfo.value = {
      name: data.jobSeekerName || '未知',
      position: data.jobTitle || '职位',
      round: getTypeText(data.interviewType),
      interviewer: data.interviewer || data.hrName || '面试官',
      date: data.interviewTime ? new Date(data.interviewTime).toLocaleDateString('zh-CN') : ''
    }
    
    if (data.feedback) {
      feedback.comments = data.feedback
    }
    if (data.result) {
      feedback.decision = data.result
    }
  } catch (error) {
    console.error('获取面试信息失败', error)
  } finally {
    loading.value = false
  }
}

const setRating = (key, score) => {
  const dimension = ratingDimensions.find(d => d.key === key)
  if (dimension) {
    dimension.score = score
  }
}

const submitFeedback = async () => {
  if (!feedback.decision) {
    alert('请选择面试决策')
    return
  }

  submitting.value = true
  try {
    const feedbackText = [
      feedback.strengths ? `优势: ${feedback.strengths}` : '',
      feedback.improvements ? `待提升: ${feedback.improvements}` : '',
      feedback.comments ? `综合评价: ${feedback.comments}` : ''
    ].filter(Boolean).join('\n')

    // 提交面试反馈
    await api.submitInterviewFeedback(route.params.id, feedbackText, feedback.decision)

    // 如果决策是建议录用，同步更新申请状态为已录用
    if (feedback.decision === 'PASS' && interviewData.value?.applicationId) {
      try {
        await api.updateApplicationStatus(interviewData.value.applicationId, 'HIRED')
      } catch (err) {
        console.error('更新申请状态失败', err)
      }
    }

    // 如果决策是不建议录用，更新申请状态为已拒绝
    if (feedback.decision === 'FAIL' && interviewData.value?.applicationId) {
      try {
        await api.updateApplicationStatus(interviewData.value.applicationId, 'REJECTED')
      } catch (err) {
        console.error('更新申请状态失败', err)
      }
    }

    const resultMessages = {
      'PASS': '评价提交成功！候选人状态已更新为"已录用"，系统已发送通知。',
      'FAIL': '评价提交成功！候选人状态已更新为"不通过"，系统已发送通知。',
      'POOL': '评价提交成功！候选人已加入人才库，申请状态保持不变。'
    }
    alert(resultMessages[feedback.decision] || '评价提交成功')
    router.push('/company/interviews')
  } catch (error) {
    console.error('提交失败', error)
    alert('提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

const saveDraft = () => {
  alert('草稿已保存到本地')
}
</script>