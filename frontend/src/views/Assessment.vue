<template>
  <div>
    <!-- Header -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 mb-8">
      <div>
        <h2 class="text-3xl font-black text-slate-900 dark:text-slate-100 tracking-tight">职业评估</h2>
        <p class="text-slate-500 dark:text-slate-400 mt-1">了解您的职业能力与发展方向</p>
      </div>
      <div v-if="hasUnfinishedAssessment" class="flex items-center gap-2 px-4 py-2 bg-amber-50 dark:bg-amber-900/20 border border-amber-200 dark:border-amber-800 rounded-lg">
        <span class="material-symbols-outlined text-amber-500">pending</span>
        <span class="text-sm text-amber-700 dark:text-amber-300">有未完成的测评</span>
        <button class="text-amber-600 dark:text-amber-400 font-bold text-sm hover:underline" @click="continueAssessment">继续</button>
      </div>
    </div>
    
    <!-- Assessment Types -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      <div 
        v-for="type in assessmentTypes" 
        :key="type.id"
        class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 hover:border-primary/50 transition-colors cursor-pointer"
        @click="showTypeDetail(type)"
      >
        <div class="w-14 h-14 rounded-xl flex items-center justify-center mb-4" :style="{ backgroundColor: type.color + '20' }">
          <span class="material-symbols-outlined text-3xl" :style="{ color: type.color }">{{ type.icon }}</span>
        </div>
        <h3 class="text-lg font-bold text-slate-900 dark:text-white mb-2">{{ type.title }}</h3>
        <p class="text-sm text-slate-500 dark:text-slate-400 mb-3">{{ type.description }}</p>
        <div class="flex items-center gap-2 text-xs text-slate-400 mb-4">
          <span class="material-symbols-outlined text-sm">timer</span>
          <span>约{{ type.questionCount || 10 }}题 · {{ type.duration || 5 }}分钟</span>
        </div>
        <button class="w-full py-2 bg-primary/10 text-primary rounded-lg font-bold text-sm hover:bg-primary hover:text-white transition-colors">
          开始测评
        </button>
      </div>
    </div>
    
    <!-- Assessment History -->
    <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 overflow-hidden">
      <div class="p-4 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center">
        <h3 class="font-bold text-slate-900 dark:text-white">测评记录</h3>
        <div class="flex items-center gap-2">
          <button 
            v-if="history.length > 0"
            class="text-sm text-slate-500 hover:text-primary"
            @click="showStatistics = !showStatistics"
          >
            {{ showStatistics ? '隐藏统计' : '查看统计' }}
          </button>
        </div>
      </div>
      
      <!-- Statistics -->
      <div v-if="showStatistics && history.length > 0" class="p-4 bg-slate-50 dark:bg-slate-800 border-b border-slate-200 dark:border-slate-700">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div class="text-center">
            <p class="text-2xl font-bold text-primary">{{ statistics.total }}</p>
            <p class="text-xs text-slate-500">总测评次数</p>
          </div>
          <div class="text-center">
            <p class="text-2xl font-bold text-emerald-500">{{ statistics.avgScore }}</p>
            <p class="text-xs text-slate-500">平均得分</p>
          </div>
          <div class="text-center">
            <p class="text-2xl font-bold text-amber-500">{{ statistics.highestScore }}</p>
            <p class="text-xs text-slate-500">最高得分</p>
          </div>
          <div class="text-center">
            <p class="text-2xl font-bold text-cyan-500">{{ statistics.latestType || '-' }}</p>
            <p class="text-xs text-slate-500">最近测评</p>
          </div>
        </div>
        
        <!-- Score Chart -->
        <div class="mt-4">
          <h4 class="text-sm font-bold text-slate-700 dark:text-slate-300 mb-2">得分趋势</h4>
          <div class="h-20 flex items-end gap-1">
            <div 
              v-for="(item, index) in scoreChart" 
              :key="index"
              class="flex-1 bg-primary rounded-t transition-all hover:bg-primary/80"
              :style="{ height: item.percent + '%' }"
              :title="`${item.score}分 - ${item.type}`"
            ></div>
          </div>
          <div class="flex justify-between text-xs text-slate-400 mt-1">
            <span>最早</span>
            <span>最近</span>
          </div>
        </div>
      </div>
      
      <div class="p-4">
        <div v-if="loading" class="text-center py-8">
          <div class="animate-spin rounded-full h-12 w-12 border-4 border-primary border-t-transparent mx-auto"></div>
          <p class="text-slate-500 dark:text-slate-400 mt-4">加载中...</p>
        </div>
        
        <div v-else-if="history.length > 0" class="space-y-3">
          <div 
            v-for="item in history" 
            :key="item.id"
            class="flex items-center justify-between p-4 bg-slate-50 dark:bg-slate-800 rounded-xl"
          >
            <div class="flex items-center gap-4">
              <div class="w-10 h-10 rounded-lg flex items-center justify-center" :class="getScoreClass(item)">
                <span class="material-symbols-outlined">school</span>
              </div>
              <div>
                <p class="font-bold text-slate-900 dark:text-white">{{ item.assessmentType || item.type || '职业测评' }}</p>
                <p class="text-xs text-slate-500">{{ formatDate(item.createTime || item.date) }}</p>
              </div>
            </div>
            <div class="flex items-center gap-4">
              <div class="text-right">
                <p class="font-bold text-lg" :class="getScoreTextClass(item)">{{ getScore(item) }}分</p>
                <p class="text-xs text-slate-400">{{ getScoreLevel(item) }}</p>
              </div>
              <button 
                class="text-primary text-sm font-bold hover:underline"
                @click="viewResult(item)"
              >
                查看详情
              </button>
              <button 
                class="text-red-500 text-sm font-bold hover:underline"
                @click="deleteAssessment(item)"
              >
                删除
              </button>
            </div>
          </div>
        </div>
        <div v-else class="text-center py-8">
          <span class="material-symbols-outlined text-4xl text-slate-300 dark:text-slate-600 mb-2">history</span>
          <p class="text-slate-500 dark:text-slate-400">暂无测评记录</p>
        </div>
      </div>
    </div>
    
    <!-- Assessment Type Detail Modal -->
    <div v-if="showTypeModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-white dark:bg-slate-900 rounded-2xl w-full max-w-lg overflow-hidden">
        <div class="p-6 border-b border-slate-200 dark:border-slate-800">
          <h3 class="text-xl font-bold text-slate-900 dark:text-white">{{ selectedType?.title }}</h3>
        </div>
        <div class="p-6">
          <div class="flex items-center gap-4 mb-6">
            <div class="w-16 h-16 rounded-xl flex items-center justify-center" :style="{ backgroundColor: selectedType?.color + '20' }">
              <span class="material-symbols-outlined text-4xl" :style="{ color: selectedType?.color }">{{ selectedType?.icon }}</span>
            </div>
            <div>
              <p class="text-slate-600 dark:text-slate-400">{{ selectedType?.description }}</p>
              <p class="text-sm text-slate-400 mt-1">共{{ selectedType?.questionCount || 10 }}题，预计用时{{ selectedType?.duration || 5 }}分钟</p>
            </div>
          </div>
          
          <div class="space-y-4 mb-6">
            <h4 class="font-bold text-slate-900 dark:text-white">测评说明</h4>
            <ul class="space-y-2 text-sm text-slate-600 dark:text-slate-400">
              <li class="flex items-start gap-2">
                <span class="material-symbols-outlined text-primary text-base mt-0.5">check_circle</span>
                <span>每题限时30秒，超时自动跳转下一题</span>
              </li>
              <li class="flex items-start gap-2">
                <span class="material-symbols-outlined text-primary text-base mt-0.5">check_circle</span>
                <span>测评过程中可暂停，下次继续作答</span>
              </li>
              <li class="flex items-start gap-2">
                <span class="material-symbols-outlined text-primary text-base mt-0.5">check_circle</span>
                <span>完成后将生成详细的评估报告</span>
              </li>
              <li class="flex items-start gap-2">
                <span class="material-symbols-outlined text-primary text-base mt-0.5">check_circle</span>
                <span>请认真作答，确保结果准确</span>
              </li>
            </ul>
          </div>
          
          <div class="p-4 bg-slate-50 dark:bg-slate-800 rounded-xl mb-6">
            <p class="text-sm text-slate-600 dark:text-slate-400">
              <span class="font-bold text-slate-900 dark:text-white">提示：</span>
              测评结果将保存在您的个人档案中，可作为求职参考。
            </p>
          </div>
        </div>
        <div class="p-6 border-t border-slate-200 dark:border-slate-800 flex gap-3">
          <button 
            class="flex-1 py-2 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 rounded-xl font-bold"
            @click="showTypeModal = false"
          >
            取消
          </button>
          <button 
            class="flex-1 py-2 bg-primary text-white rounded-xl font-bold hover:bg-primary/90"
            @click="confirmStartAssessment"
          >
            开始测评
          </button>
        </div>
      </div>
    </div>
    
    <!-- Assessment Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-white dark:bg-slate-900 rounded-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <div class="p-6 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center">
          <div>
            <h3 class="text-xl font-bold text-slate-900 dark:text-white">{{ currentType?.title }}</h3>
            <p class="text-sm text-slate-500 mt-1">请认真作答，确保结果准确</p>
          </div>
          <div class="flex items-center gap-2">
            <button 
              class="px-3 py-1 bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 rounded-lg text-sm"
              @click="pauseAssessment"
            >
              暂停
            </button>
            <button class="text-slate-400 hover:text-slate-600" @click="confirmExit">
              <span class="material-symbols-outlined">close</span>
            </button>
          </div>
        </div>
        
        <!-- Timer and Progress -->
        <div class="px-6 py-4 bg-slate-50 dark:bg-slate-800">
          <div class="flex justify-between items-center mb-2">
            <span class="text-sm text-slate-600 dark:text-slate-400">问题 {{ currentQuestion + 1 }} / {{ questions.length }}</span>
            <div class="flex items-center gap-4">
              <div v-if="questionTimer !== null" class="flex items-center gap-1" :class="{ 'text-red-500': questionTimer <= 5 }">
                <span class="material-symbols-outlined text-sm">timer</span>
                <span class="font-bold">{{ questionTimer }}s</span>
              </div>
              <span class="text-primary font-bold">{{ Math.round((currentQuestion + 1) / questions.length * 100) }}%</span>
            </div>
          </div>
          <div class="h-2 bg-slate-200 dark:bg-slate-700 rounded-full overflow-hidden">
            <div class="h-full bg-primary rounded-full transition-all" :style="{ width: ((currentQuestion + 1) / questions.length * 100) + '%' }"></div>
          </div>
        </div>
        
        <!-- Question -->
        <div class="p-6">
          <p class="text-lg font-bold text-slate-900 dark:text-white mb-6">{{ questions[currentQuestion]?.text }}</p>
          <div class="space-y-3">
            <label 
              v-for="(option, index) in questions[currentQuestion]?.options" 
              :key="index"
              :class="[
                'flex items-center gap-3 p-4 rounded-xl border-2 cursor-pointer transition-all',
                answers[currentQuestion] === option.value 
                  ? 'border-primary bg-primary/5' 
                  : 'border-slate-200 dark:border-slate-700 hover:border-primary/50'
              ]"
            >
              <input 
                v-model="answers[currentQuestion]" 
                :value="option.value" 
                type="radio" 
                class="hidden" 
                name="question"
                @change="onAnswerSelect"
              />
              <span class="material-symbols-outlined text-slate-400">{{ answers[currentQuestion] === option.value ? 'radio_button_checked' : 'radio_button_unchecked' }}</span>
              <span class="text-slate-700 dark:text-slate-300">{{ option.label }}</span>
            </label>
          </div>
        </div>
        
        <!-- Actions -->
        <div class="p-6 border-t border-slate-200 dark:border-slate-800 flex justify-between">
          <button 
            v-if="currentQuestion > 0"
            class="px-6 py-2 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 rounded-xl font-bold text-sm"
            @click="prevQuestion"
          >
            上一题
          </button>
          <div class="flex-1"></div>
          <button 
            v-if="currentQuestion < questions.length - 1"
            class="px-6 py-2 bg-primary text-white rounded-xl font-bold text-sm hover:bg-primary/90"
            @click="nextQuestion"
          >
            下一题
          </button>
          <button 
            v-else
            class="px-6 py-2 bg-emerald-500 text-white rounded-xl font-bold text-sm hover:bg-emerald-600"
            :disabled="submitting"
            @click="submitAssessment"
          >
            {{ submitting ? '提交中...' : '提交测评' }}
          </button>
        </div>
      </div>
    </div>

    <!-- Detail Modal -->
    <div v-if="showDetailModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
      <div class="bg-white dark:bg-slate-900 rounded-2xl w-full max-w-3xl max-h-[90vh] overflow-y-auto">
        <div class="p-6 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center">
          <h3 class="text-xl font-bold text-slate-900 dark:text-white">测评报告</h3>
          <button class="text-slate-400 hover:text-slate-600" @click="showDetailModal = false">
            <span class="material-symbols-outlined">close</span>
          </button>
        </div>
        <div class="p-6" v-if="detailLoading">
          <div class="text-center py-8">
            <div class="animate-spin rounded-full h-12 w-12 border-4 border-primary border-t-transparent mx-auto"></div>
          </div>
        </div>
        <div class="p-6" v-else-if="currentDetail">
          <!-- Score Section -->
          <div class="text-center mb-8">
            <div class="relative inline-block">
              <svg class="w-32 h-32" viewBox="0 0 100 100">
                <circle cx="50" cy="50" r="45" fill="none" stroke="#e5e7eb" stroke-width="8"/>
                <circle 
                  cx="50" cy="50" r="45" fill="none" 
                  :stroke="getScoreColor(currentDetail)" 
                  stroke-width="8"
                  stroke-linecap="round"
                  :stroke-dasharray="getScoreDashArray(currentDetail)"
                  transform="rotate(-90 50 50)"
                />
              </svg>
              <div class="absolute inset-0 flex items-center justify-center">
                <div class="text-center">
                  <p class="text-3xl font-black" :class="getScoreTextClass(currentDetail)">{{ getScore(currentDetail) }}</p>
                  <p class="text-xs text-slate-400">分</p>
                </div>
              </div>
            </div>
            <p class="mt-4 text-lg font-bold text-slate-900 dark:text-white">{{ currentDetail.assessmentType || currentDetail.type }}</p>
            <p class="text-sm text-slate-500">{{ getScoreDescription(currentDetail) }}</p>
          </div>
          
          <!-- Score Level -->
          <div class="grid grid-cols-4 gap-2 mb-8">
            <div class="text-center p-3 rounded-lg" :class="getScore(currentDetail) >= 90 ? 'bg-emerald-100 dark:bg-emerald-900/30' : 'bg-slate-100 dark:bg-slate-800'">
              <p class="text-xs text-slate-500">优秀</p>
              <p class="text-xs text-slate-400">90+</p>
            </div>
            <div class="text-center p-3 rounded-lg" :class="getScore(currentDetail) >= 70 && getScore(currentDetail) < 90 ? 'bg-blue-100 dark:bg-blue-900/30' : 'bg-slate-100 dark:bg-slate-800'">
              <p class="text-xs text-slate-500">良好</p>
              <p class="text-xs text-slate-400">70-89</p>
            </div>
            <div class="text-center p-3 rounded-lg" :class="getScore(currentDetail) >= 60 && getScore(currentDetail) < 70 ? 'bg-amber-100 dark:bg-amber-900/30' : 'bg-slate-100 dark:bg-slate-800'">
              <p class="text-xs text-slate-500">合格</p>
              <p class="text-xs text-slate-400">60-69</p>
            </div>
            <div class="text-center p-3 rounded-lg" :class="getScore(currentDetail) < 60 ? 'bg-red-100 dark:bg-red-900/30' : 'bg-slate-100 dark:bg-slate-800'">
              <p class="text-xs text-slate-500">待提升</p>
              <p class="text-xs text-slate-400">&lt;60</p>
            </div>
          </div>
          
          <!-- Result Section -->
          <div v-if="currentDetail.result" class="mb-6">
            <h4 class="font-bold text-slate-900 dark:text-white mb-3 flex items-center gap-2">
              <span class="material-symbols-outlined text-primary">assignment_turned_in</span>
              测评结果
            </h4>
            <div class="p-4 bg-slate-50 dark:bg-slate-800 rounded-xl">
              <p class="text-slate-600 dark:text-slate-300">{{ currentDetail.result }}</p>
            </div>
          </div>
          
          <!-- Skill Tags -->
          <div v-if="currentDetail.skillTags" class="mb-6">
            <h4 class="font-bold text-slate-900 dark:text-white mb-3 flex items-center gap-2">
              <span class="material-symbols-outlined text-primary">local_offer</span>
              能力标签
            </h4>
            <div class="flex flex-wrap gap-2">
              <span 
                v-for="(tag, index) in currentDetail.skillTags.split(',')" 
                :key="index"
                class="px-4 py-2 bg-primary/10 text-primary rounded-full text-sm font-medium"
              >
                {{ tag }}
              </span>
            </div>
          </div>
          
          <!-- Career Advice -->
          <div v-if="currentDetail.careerAdvice" class="mb-6">
            <h4 class="font-bold text-slate-900 dark:text-white mb-3 flex items-center gap-2">
              <span class="material-symbols-outlined text-primary">lightbulb</span>
              职业建议
            </h4>
            <div class="p-4 bg-emerald-50 dark:bg-emerald-900/20 border border-emerald-200 dark:border-emerald-800 rounded-xl">
              <p class="text-slate-600 dark:text-slate-300">{{ currentDetail.careerAdvice }}</p>
            </div>
          </div>
          
          <!-- Assessment Time -->
          <div class="flex items-center justify-between text-sm text-slate-500 pt-4 border-t border-slate-200 dark:border-slate-700">
            <span>测评时间：{{ formatDate(currentDetail.createTime) }}</span>
            <span>报告编号：#{{ currentDetail.id }}</span>
          </div>
        </div>
        <div class="p-6 border-t border-slate-200 dark:border-slate-800 flex gap-3">
          <button 
            class="flex-1 py-2 bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 rounded-xl font-bold"
            @click="showDetailModal = false"
          >
            关闭
          </button>
          <button 
            class="flex-1 py-2 bg-primary text-white rounded-xl font-bold hover:bg-primary/90"
            @click="exportReport"
          >
            导出报告
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import api from '@/api'
import { useToast } from '@/composables/useToast'

const toast = useToast()

const loading = ref(false)
const submitting = ref(false)
const detailLoading = ref(false)
const showStatistics = ref(false)

const assessmentTypes = ref([
  { 
    id: 1, 
    title: '职业倾向测评', 
    description: '了解您的职业兴趣和倾向，找到最适合您的职业方向',
    icon: 'psychology',
    color: '#137fec',
    questionCount: 10,
    duration: 5
  },
  { 
    id: 2, 
    title: '技能水平测评', 
    description: '评估您的专业技能水平，了解自己的能力优势',
    icon: 'code',
    color: '#67C23A',
    questionCount: 10,
    duration: 5
  },
  { 
    id: 3, 
    title: '性格特质测评', 
    description: '分析您的性格特点，了解适合的工作环境',
    icon: 'person',
    color: '#E6A23C',
    questionCount: 10,
    duration: 5
  }
])

const history = ref([])

const showTypeModal = ref(false)
const showModal = ref(false)
const showDetailModal = ref(false)
const selectedType = ref(null)
const currentType = ref(null)
const currentQuestion = ref(0)
const answers = ref([])
const currentDetail = ref(null)
const questionTimer = ref(null)
const timerInterval = ref(null)

const careerInterestQuestions = [
  { text: '您更喜欢哪种工作方式？', options: [{ label: '独立完成工作', value: 'A' }, { label: '与团队协作', value: 'B' }, { label: '带领团队工作', value: 'C' }] },
  { text: '面对压力时，您通常会？', options: [{ label: '保持冷静，理性分析', value: 'A' }, { label: '寻求他人帮助', value: 'B' }, { label: '暂时回避', value: 'C' }] },
  { text: '您更倾向于从事哪种类型的工作？', options: [{ label: '技术研发类', value: 'A' }, { label: '创意设计类', value: 'B' }, { label: '人际沟通类', value: 'C' }] },
  { text: '在做决策时，您更依赖？', options: [{ label: '逻辑和数据', value: 'A' }, { label: '直觉和经验', value: 'B' }, { label: '他人的建议', value: 'C' }] },
  { text: '您更喜欢的工作环境是？', options: [{ label: '安静独立的空间', value: 'A' }, { label: '开放协作的空间', value: 'B' }, { label: '灵活多变的环境', value: 'C' }] },
  { text: '对于新任务，您更倾向于？', options: [{ label: '先制定详细计划', value: 'A' }, { label: '边做边调整', value: 'B' }, { label: '寻求指导', value: 'C' }] },
  { text: '您认为成功的关键是？', options: [{ label: '专业能力', value: 'A' }, { label: '创新能力', value: 'B' }, { label: '人际关系', value: 'C' }] },
  { text: '遇到困难时，您会？', options: [{ label: '自己研究解决', value: 'A' }, { label: '尝试不同方法', value: 'B' }, { label: '请教他人', value: 'C' }] },
  { text: '您更喜欢哪种学习方式？', options: [{ label: '阅读技术文档', value: 'A' }, { label: '观看视频教程', value: 'B' }, { label: '参加培训课程', value: 'C' }] },
  { text: '对于职业发展，您更看重？', options: [{ label: '技术深度', value: 'A' }, { label: '创新能力', value: 'B' }, { label: '团队管理', value: 'C' }] }
]

const skillLevelQuestions = [
  { text: '您对编程语言的掌握程度？', options: [{ label: '精通多种语言', value: 'A' }, { label: '熟练使用一两种', value: 'B' }, { label: '了解基础知识', value: 'C' }] },
  { text: '您使用Git等版本控制工具的频率？', options: [{ label: '每天使用', value: 'A' }, { label: '每周使用', value: 'B' }, { label: '偶尔使用', value: 'C' }] },
  { text: '您参与过的项目规模？', options: [{ label: '大型企业级项目', value: 'A' }, { label: '中型项目', value: 'B' }, { label: '小型项目或个人项目', value: 'C' }] },
  { text: '您对数据结构和算法的理解？', options: [{ label: '深入理解并能应用', value: 'A' }, { label: '基本理解', value: 'B' }, { label: '了解概念', value: 'C' }] },
  { text: '您对数据库设计的经验？', options: [{ label: '有丰富的设计经验', value: 'A' }, { label: '能进行基本设计', value: 'B' }, { label: '了解基础知识', value: 'C' }] },
  { text: '您解决复杂问题的能力？', options: [{ label: '能独立解决复杂问题', value: 'A' }, { label: '在指导下能解决', value: 'B' }, { label: '需要较多帮助', value: 'C' }] },
  { text: '您对系统架构的了解？', options: [{ label: '有实际架构经验', value: 'A' }, { label: '了解基本概念', value: 'B' }, { label: '刚开始学习', value: 'C' }] },
  { text: '您编写测试代码的习惯？', options: [{ label: '坚持写单元测试', value: 'A' }, { label: '偶尔编写测试', value: 'B' }, { label: '很少写测试', value: 'C' }] },
  { text: '您参与代码评审的经验？', options: [{ label: '经常参与评审', value: 'A' }, { label: '偶尔参与', value: 'B' }, { label: '很少参与', value: 'C' }] },
  { text: '您对持续集成/持续部署的了解？', options: [{ label: '有实际配置经验', value: 'A' }, { label: '了解基本概念', value: 'B' }, { label: '听说过但不熟悉', value: 'C' }] }
]

const personalityQuestions = [
  { text: '在团队中，您通常扮演什么角色？', options: [{ label: '技术专家', value: 'A' }, { label: '创意发起者', value: 'B' }, { label: '团队协调者', value: 'C' }] },
  { text: '您如何处理与同事的分歧？', options: [{ label: '用数据和事实说服', value: 'A' }, { label: '寻找创新解决方案', value: 'B' }, { label: '寻求妥协', value: 'C' }] },
  { text: '面对失败，您会？', options: [{ label: '分析原因并改进', value: 'A' }, { label: '尝试新方法', value: 'B' }, { label: '寻求他人支持', value: 'C' }] },
  { text: '您更喜欢的工作节奏？', options: [{ label: '稳定有序', value: 'A' }, { label: '快节奏多变', value: 'B' }, { label: '灵活调整', value: 'C' }] },
  { text: '您对加班的态度？', options: [{ label: '必要时可以接受', value: 'A' }, { label: '看项目兴趣', value: 'B' }, { label: '注重工作生活平衡', value: 'C' }] },
  { text: '您如何表达自己的观点？', options: [{ label: '直接明确', value: 'A' }, { label: '生动有趣', value: 'B' }, { label: '委婉温和', value: 'C' }] },
  { text: '面对新任务，您的第一反应是？', options: [{ label: '分析可行性', value: 'A' }, { label: '思考创新点', value: 'B' }, { label: '了解他人看法', value: 'C' }] },
  { text: '您更看重工作的哪个方面？', options: [{ label: '技术挑战', value: 'A' }, { label: '创新空间', value: 'B' }, { label: '团队氛围', value: 'C' }] },
  { text: '您如何应对变化？', options: [{ label: '提前规划', value: 'A' }, { label: '灵活适应', value: 'B' }, { label: '寻求支持', value: 'C' }] },
  { text: '您认为理想的工作环境是？', options: [{ label: '专注安静', value: 'A' }, { label: '开放自由', value: 'B' }, { label: '和谐友好', value: 'C' }] }
]

const questions = ref([])

const statistics = computed(() => {
  const total = history.value.length
  if (total === 0) return { total: 0, avgScore: 0, highestScore: 0, latestType: '-' }
  
  const scores = history.value.map(h => getScore(h))
  const avgScore = Math.round(scores.reduce((a, b) => a + b, 0) / total)
  const highestScore = Math.max(...scores)
  const latestType = history.value[0]?.assessmentType || '-'
  
  return { total, avgScore, highestScore, latestType }
})

const scoreChart = computed(() => {
  const maxScore = 100
  return history.value.slice(0, 10).reverse().map(h => ({
    score: getScore(h),
    percent: (getScore(h) / maxScore) * 100,
    type: h.assessmentType
  }))
})

const hasUnfinishedAssessment = computed(() => {
  const saved = localStorage.getItem('unfinishedAssessment')
  return saved !== null
})

const getScore = (item) => {
  if (item && item.score !== null && item.score !== undefined) {
    return item.score
  }
  if (item && item.totalScore !== null && item.totalScore !== undefined) {
    return item.totalScore
  }
  return 0
}

const getScoreLevel = (item) => {
  const score = getScore(item)
  if (score >= 90) return '优秀'
  if (score >= 70) return '良好'
  if (score >= 60) return '合格'
  return '待提升'
}

const getScoreClass = (item) => {
  const score = getScore(item)
  if (score >= 90) return 'bg-emerald-100 dark:bg-emerald-900/30 text-emerald-500'
  if (score >= 70) return 'bg-blue-100 dark:bg-blue-900/30 text-blue-500'
  if (score >= 60) return 'bg-amber-100 dark:bg-amber-900/30 text-amber-500'
  return 'bg-red-100 dark:bg-red-900/30 text-red-500'
}

const getScoreTextClass = (item) => {
  const score = getScore(item)
  if (score >= 90) return 'text-emerald-500'
  if (score >= 70) return 'text-blue-500'
  if (score >= 60) return 'text-amber-500'
  return 'text-red-500'
}

const getScoreColor = (item) => {
  const score = getScore(item)
  if (score >= 90) return '#10b981'
  if (score >= 70) return '#3b82f6'
  if (score >= 60) return '#f59e0b'
  return '#ef4444'
}

const getScoreDashArray = (item) => {
  const score = getScore(item)
  const circumference = 2 * Math.PI * 45
  return `${(score / 100) * circumference} ${circumference}`
}

const getScoreDescription = (item) => {
  const score = getScore(item)
  if (score >= 90) return '表现优秀，具备很强的职业能力'
  if (score >= 70) return '表现良好，有较好的职业素养'
  if (score >= 60) return '表现合格，仍有提升空间'
  return '需要继续努力提升相关能力'
}

const fetchHistory = async () => {
  loading.value = true
  try {
    const data = await api.getMyAssessments()
    history.value = data?.content || data || []
  } catch (error) {
    console.error('获取测评记录失败', error)
    history.value = []
  } finally {
    loading.value = false
  }
}

const showTypeDetail = (type) => {
  selectedType.value = type
  showTypeModal.value = true
}

const confirmStartAssessment = () => {
  showTypeModal.value = false
  startAssessment(selectedType.value)
}

const startAssessment = (type) => {
  currentType.value = type
  showModal.value = true
  currentQuestion.value = 0
  answers.value = []
  
  if (type.id === 1) {
    questions.value = careerInterestQuestions
  } else if (type.id === 2) {
    questions.value = skillLevelQuestions
  } else {
    questions.value = personalityQuestions
  }
  
  answers.value = new Array(questions.value.length).fill(null)
  startQuestionTimer()
}

const startQuestionTimer = () => {
  if (timerInterval.value) {
    clearInterval(timerInterval.value)
  }
  questionTimer.value = 30
  
  timerInterval.value = setInterval(() => {
    questionTimer.value--
    if (questionTimer.value <= 0) {
      clearInterval(timerInterval.value)
      if (currentQuestion.value < questions.value.length - 1) {
        nextQuestion()
      } else {
        toast.warning('答题时间已到，请提交测评')
      }
    }
  }, 1000)
}

const onAnswerSelect = () => {
  if (timerInterval.value) {
    clearInterval(timerInterval.value)
  }
}

const prevQuestion = () => {
  if (currentQuestion.value > 0) {
    currentQuestion.value--
    startQuestionTimer()
  }
}

const nextQuestion = () => {
  if (answers.value[currentQuestion.value] === null) {
    toast.warning('请选择一个选项')
    return
  }
  if (currentQuestion.value < questions.value.length - 1) {
    currentQuestion.value++
    startQuestionTimer()
  }
}

const pauseAssessment = () => {
  if (timerInterval.value) {
    clearInterval(timerInterval.value)
  }
  
  const assessmentData = {
    type: currentType.value,
    currentQuestion: currentQuestion.value,
    answers: answers.value,
    questions: questions.value,
    timestamp: Date.now()
  }
  localStorage.setItem('unfinishedAssessment', JSON.stringify(assessmentData))
  
  showModal.value = false
  toast.success('测评已暂停，可稍后继续')
}

const continueAssessment = () => {
  const saved = localStorage.getItem('unfinishedAssessment')
  if (!saved) return
  
  try {
    const data = JSON.parse(saved)
    currentType.value = data.type
    currentQuestion.value = data.currentQuestion
    answers.value = data.answers
    questions.value = data.questions
    showModal.value = true
    startQuestionTimer()
    localStorage.removeItem('unfinishedAssessment')
  } catch (e) {
    console.error('恢复测评失败', e)
    toast.error('恢复测评失败')
  }
}

const confirmExit = () => {
  if (confirm('确定要退出测评吗？当前进度将不会保存。')) {
    if (timerInterval.value) {
      clearInterval(timerInterval.value)
    }
    showModal.value = false
  }
}

const submitAssessment = async () => {
  if (answers.value.includes(null)) {
    toast.warning('请完成所有题目')
    return
  }
  
  if (timerInterval.value) {
    clearInterval(timerInterval.value)
  }
  
  submitting.value = true
  const score = calculateScore()
  
  try {
    const answerItems = answers.value.map((answer, index) => ({
      questionId: index + 1,
      answer: answer,
      score: scoreMap[answer] || 80
    }))
    
    await api.submitAssessment({
      assessmentType: currentType.value.title,
      answers: answerItems,
      totalScore: score
    })
    
    toast.success(`测评完成！得分：${score}分`)
    showModal.value = false
    
    await fetchHistory()
  } catch (error) {
    console.error('提交测评失败', error)
    
    toast.success(`测评完成！得分：${score}分`)
    showModal.value = false
    
    history.value.unshift({
      id: Date.now(),
      assessmentType: currentType.value.title,
      type: currentType.value.title,
      score: score,
      totalScore: score,
      createTime: new Date().toISOString(),
      date: new Date().toISOString().split('T')[0]
    })
  } finally {
    submitting.value = false
  }
}

const scoreMap = { A: 90, B: 85, C: 80 }

const calculateScore = () => {
  const total = answers.value.reduce((sum, answer) => sum + (scoreMap[answer] || 80), 0)
  return Math.round(total / answers.value.length)
}

const viewResult = async (item) => {
  showDetailModal.value = true
  detailLoading.value = true
  currentDetail.value = null
  
  try {
    const data = await api.getAssessmentById(item.id)
    currentDetail.value = data
  } catch (error) {
    console.error('获取测评详情失败', error)
    currentDetail.value = item
    toast.error('获取详情失败，显示基本信息')
  } finally {
    detailLoading.value = false
  }
}

const deleteAssessment = async (item) => {
  if (!confirm('确定要删除这条测评记录吗？')) {
    return
  }
  
  try {
    await api.deleteAssessment(item.id)
    toast.success('删除成功')
    history.value = history.value.filter(h => h.id !== item.id)
  } catch (error) {
    console.error('删除测评记录失败', error)
    toast.error('删除失败')
  }
}

const exportReport = () => {
  if (!currentDetail.value) return
  
  const reportContent = `
职业测评报告
==================

测评类型：${currentDetail.value.assessmentType || currentDetail.value.type}
测评得分：${getScore(currentDetail.value)}分
测评等级：${getScoreLevel(currentDetail.value)}
测评时间：${formatDate(currentDetail.value.createTime)}

${currentDetail.value.result ? '测评结果：\n' + currentDetail.value.result + '\n' : ''}
${currentDetail.value.skillTags ? '能力标签：\n' + currentDetail.value.skillTags + '\n' : ''}
${currentDetail.value.careerAdvice ? '职业建议：\n' + currentDetail.value.careerAdvice : ''}
  `.trim()
  
  const blob = new Blob([reportContent], { type: 'text/plain;charset=utf-8' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `测评报告_${currentDetail.value.assessmentType || '职业测评'}_${formatDate(currentDetail.value.createTime)}.txt`
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
  
  toast.success('报告已导出')
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(() => {
  fetchHistory()
})

onUnmounted(() => {
  if (timerInterval.value) {
    clearInterval(timerInterval.value)
  }
})
</script>
