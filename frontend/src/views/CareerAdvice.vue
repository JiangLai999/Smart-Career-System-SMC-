<template>
  <div>
    <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 mb-8">
      <div>
        <h2 class="text-3xl font-black text-slate-900 dark:text-white">职业规划建议</h2>
        <p class="text-slate-500 dark:text-slate-400 mt-1">基于您的测评结果和求职数据，为您提供个性化职业发展建议</p>
      </div>
    </div>
    
    <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 mb-6">
      <h3 class="text-lg font-bold mb-4 flex items-center gap-2">
        <span class="material-symbols-outlined text-primary">analytics</span>
        能力评估结果
      </h3>
      <div v-if="skills.length > 0" class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div v-for="skill in skills" :key="skill.name" class="text-center">
          <div class="relative w-20 h-20 mx-auto mb-2">
            <svg class="w-full h-full" viewBox="0 0 100 100">
              <circle cx="50" cy="50" r="45" fill="none" stroke="#e2e8f0" stroke-width="8"/>
              <circle cx="50" cy="50" r="45" fill="none" :stroke="skill.color" stroke-width="8" 
                :stroke-dasharray="283" :stroke-dashoffset="283 - (283 * skill.score / 100)" 
                transform="rotate(-90 50 50)" stroke-linecap="round"/>
            </svg>
            <div class="absolute inset-0 flex items-center justify-center">
              <span class="text-lg font-bold" :style="{ color: skill.color }">{{ skill.score }}</span>
            </div>
          </div>
          <p class="text-sm font-medium text-slate-700 dark:text-slate-300">{{ skill.name }}</p>
        </div>
      </div>
      <div v-else class="text-center py-8">
        <span class="material-symbols-outlined text-4xl text-slate-300 dark:text-slate-600 mb-2">psychology</span>
        <p class="text-slate-500 dark:text-slate-400">完成职业测评以获取能力评估</p>
        <button class="mt-4 px-6 py-2 bg-primary text-white rounded-xl font-bold text-sm" @click="$router.push('/app/assessment')">
          开始测评
        </button>
      </div>
    </div>
    
    <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 mb-6">
      <h3 class="text-lg font-bold mb-4 flex items-center gap-2">
        <span class="material-symbols-outlined text-primary">timeline</span>
        推荐职业路径
      </h3>
      <div v-if="careerPaths.length > 0" class="space-y-4">
        <div v-for="(path, index) in careerPaths" :key="index" class="p-4 rounded-xl border border-slate-100 dark:border-slate-800 hover:border-primary/50 transition-colors">
          <div class="flex justify-between items-start mb-2">
            <div>
              <h4 class="font-bold text-slate-900 dark:text-white">{{ path.title }}</h4>
              <p class="text-sm text-slate-500">{{ path.industry }}</p>
            </div>
            <span class="px-3 py-1 bg-primary/10 text-primary text-sm font-bold rounded-full">
              匹配度 {{ path.match }}%
            </span>
          </div>
          <p class="text-sm text-slate-600 dark:text-slate-400 mb-3">{{ path.description }}</p>
          <div class="flex flex-wrap gap-2">
            <span v-for="skill in path.requiredSkills" :key="skill" class="px-2 py-1 bg-slate-100 dark:bg-slate-800 rounded text-xs text-slate-600 dark:text-slate-400">
              {{ skill }}
            </span>
          </div>
        </div>
      </div>
      <div v-else class="text-center py-8">
        <span class="material-symbols-outlined text-4xl text-slate-300 dark:text-slate-600 mb-2">work</span>
        <p class="text-slate-500 dark:text-slate-400">完善简历信息以获取职业建议</p>
        <button class="mt-4 px-6 py-2 bg-primary text-white rounded-xl font-bold text-sm" @click="$router.push('/app/profile')">
          完善简历
        </button>
      </div>
    </div>
    
    <div v-if="skillImprovements.length > 0" class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6 mb-6">
      <h3 class="text-lg font-bold mb-4 flex items-center gap-2">
        <span class="material-symbols-outlined text-primary">trending_up</span>
        技能提升建议
      </h3>
      <div class="space-y-4">
        <div v-for="(item, index) in skillImprovements" :key="index" class="flex gap-4 items-start">
          <div class="w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center text-primary font-bold shrink-0">
            {{ index + 1 }}
          </div>
          <div class="flex-1">
            <h4 class="font-bold text-slate-900 dark:text-white mb-1">{{ item.skill }}</h4>
            <p class="text-sm text-slate-600 dark:text-slate-400 mb-2">{{ item.suggestion }}</p>
          </div>
        </div>
      </div>
    </div>
    
    <div class="bg-white dark:bg-slate-900 rounded-2xl border border-slate-200 dark:border-slate-800 p-6">
      <h3 class="text-lg font-bold mb-4 flex items-center gap-2">
        <span class="material-symbols-outlined text-primary">insights</span>
        市场趋势洞察
      </h3>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div class="p-4 bg-slate-50 dark:bg-slate-800 rounded-xl">
          <p class="text-sm text-slate-500 mb-1">热门技能需求</p>
          <p class="text-2xl font-bold text-primary">{{ marketInsights.topSkill }}</p>
          <p class="text-xs text-slate-400 mt-1">同比增长 {{ marketInsights.skillGrowth }}%</p>
        </div>
        <div class="p-4 bg-slate-50 dark:bg-slate-800 rounded-xl">
          <p class="text-sm text-slate-500 mb-1">平均薪资水平</p>
          <p class="text-2xl font-bold text-primary">{{ marketInsights.avgSalary }}</p>
          <p class="text-xs text-slate-400 mt-1">您期望的薪资范围</p>
        </div>
        <div class="p-4 bg-slate-50 dark:bg-slate-800 rounded-xl">
          <p class="text-sm text-slate-500 mb-1">推荐城市</p>
          <p class="text-2xl font-bold text-primary">{{ marketInsights.topCities }}</p>
          <p class="text-xs text-slate-400 mt-1">职位数量最多</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const profile = ref({})
const skills = ref([])
const careerPaths = ref([])
const skillImprovements = ref([])
const assessments = ref([])
const marketInsights = ref({
  topSkill: 'JavaScript',
  skillGrowth: 23,
  avgSalary: '25-35K',
  topCities: '北京、深圳'
})

const fetchData = async () => {
  try {
    const profileData = await api.getJobSeekerProfile()
    profile.value = profileData || {}
  } catch (error) {
    console.error('获取用户信息失败', error)
  }
  
  try {
    const assessmentData = await api.getMyAssessments()
    assessments.value = assessmentData || []
  } catch (error) {
    console.error('获取测评记录失败', error)
  }
  
  generateCareerAdvice()
}

const generateCareerAdvice = () => {
  if (assessments.value.length > 0) {
    generateSkillsFromAssessments()
  } else {
    generateSkillsFromProfile()
  }
  
  generateCareerPaths()
  generateSkillImprovements()
  
  if (profile.value.expectedSalaryMin && profile.value.expectedSalaryMax) {
    marketInsights.value.avgSalary = Math.round(profile.value.expectedSalaryMin / 1000) + '-' + Math.round(profile.value.expectedSalaryMax / 1000) + 'K'
  }
}

const generateSkillsFromAssessments = () => {
  const assessmentTypes = {}
  
  assessments.value.forEach(a => {
    const type = a.assessmentType || a.type
    if (!assessmentTypes[type] || (a.score || a.totalScore) > (assessmentTypes[type].score || 0)) {
      assessmentTypes[type] = a
    }
  })
  
  const skillMap = []
  
  if (assessmentTypes['职业倾向测评'] || assessmentTypes['CareerInterest']) {
    const score = assessmentTypes['职业倾向测评']?.score || assessmentTypes['CareerInterest']?.score || 80
    skillMap.push({ name: '职业倾向', score, color: '#137fec' })
  }
  
  if (assessmentTypes['技能水平测评'] || assessmentTypes['SKILL']) {
    const score = assessmentTypes['技能水平测评']?.score || assessmentTypes['SKILL']?.score || 80
    skillMap.push({ name: '技能水平', score, color: '#10b981' })
  }
  
  if (assessmentTypes['性格特质测评'] || assessmentTypes['PERSONALITY']) {
    const score = assessmentTypes['性格特质测评']?.score || assessmentTypes['PERSONALITY']?.score || 80
    skillMap.push({ name: '性格特质', score, color: '#f59e0b' })
  }
  
  if (skillMap.length < 3) {
    skillMap.push({ name: '综合能力', score: 75, color: '#8b5cf6' })
  }
  
  skills.value = skillMap.slice(0, 4)
}

const generateSkillsFromProfile = () => {
  if (!profile.value.skills) {
    skills.value = [
      { name: '技术能力', score: 70, color: '#137fec' },
      { name: '沟通能力', score: 75, color: '#10b981' },
      { name: '团队协作', score: 78, color: '#f59e0b' },
      { name: '学习能力', score: 80, color: '#8b5cf6' }
    ]
    return
  }
  
  const userSkills = profile.value.skills.split(',').map(s => s.trim().toLowerCase())
  skills.value = [
    { name: '技术能力', score: calculateSkillScore(userSkills, ['java', 'python', 'javascript', 'react', 'vue', 'php', 'go', 'c++']), color: '#137fec' },
    { name: '沟通能力', score: 75, color: '#10b981' },
    { name: '团队协作', score: 78, color: '#f59e0b' },
    { name: '学习能力', score: 80, color: '#8b5cf6' }
  ]
}

const calculateSkillScore = (userSkills, relatedSkills) => {
  const matchCount = userSkills.filter(s => relatedSkills.includes(s)).length
  return 60 + Math.min(matchCount * 10, 30)
}

const generateCareerPaths = () => {
  if (assessments.value.length >= 3) {
    const avgScore = assessments.value.reduce((sum, a) => sum + (a.score || a.totalScore || 80), 0) / assessments.value.length
    
    if (avgScore >= 85) {
      careerPaths.value = [
        { title: '技术专家/架构师', industry: '互联网/科技', match: 95, description: '恭喜！您已完成全部职业测评，综合得分优秀。建议深入技术领域，成为技术专家或架构师。', requiredSkills: ['系统设计', '分布式架构', '性能优化', '技术选型'] },
        { title: '技术经理', industry: '互联网/科技', match: 88, description: '您具备优秀的技术能力和管理潜质，适合向技术管理方向发展。', requiredSkills: ['团队管理', '项目管理', '沟通协调', '战略规划'] }
      ]
    } else if (avgScore >= 75) {
      careerPaths.value = [
        { title: '高级工程师', industry: '互联网/科技', match: 90, description: '您的测评结果显示具备成为高级工程师的潜力，建议深耕专业技能。', requiredSkills: ['核心技术', '问题解决', '代码质量', '知识分享'] },
        { title: '全栈工程师', industry: '互联网/科技', match: 82, description: '建议拓宽技术栈，向全栈工程师发展，提升综合竞争力。', requiredSkills: ['前端框架', '后端技术', '数据库', 'DevOps'] }
      ]
    } else {
      careerPaths.value = [
        { title: '初级工程师', industry: '互联网/科技', match: 85, description: '建议从基础岗位做起，持续学习提升专业能力。', requiredSkills: ['基础知识', '编码规范', '学习能力', '团队协作'] },
        { title: '产品经理', industry: '互联网/科技', match: 75, description: '如果您对技术不太感兴趣，可以考虑转岗产品经理。', requiredSkills: ['需求分析', '产品设计', '用户调研', '项目管理'] }
      ]
    }
  } else if (profile.value.skills) {
    const userSkills = profile.value.skills.split(',').map(s => s.trim().toLowerCase())
    
    if (userSkills.some(s => ['javascript', 'react', 'vue', '前端', 'html', 'css'].includes(s))) {
      careerPaths.value = [
        { title: '高级前端工程师', industry: '互联网/科技', match: 90, description: '基于您的前端技能，建议深耕React/Vue框架，向高级前端工程师发展。', requiredSkills: ['Vue/React', 'TypeScript', 'Node.js', '性能优化'] },
        { title: '全栈工程师', industry: '互联网/科技', match: 82, description: '建议学习后端技术，向全栈工程师发展。', requiredSkills: ['前端框架', 'Node.js/Python', 'MySQL/MongoDB', 'Docker'] }
      ]
    } else if (userSkills.some(s => ['java', 'python', 'php', 'go', '后端'].includes(s))) {
      careerPaths.value = [
        { title: '高级后端工程师', industry: '互联网/科技', match: 90, description: '基于您的后端技能，建议深入学习微服务架构和分布式系统。', requiredSkills: ['Spring Boot', '微服务', '分布式', '消息队列'] },
        { title: '架构师', industry: '互联网/科技', match: 80, description: '建议积累架构设计经验，向技术架构师方向发展。', requiredSkills: ['系统设计', '分布式架构', '高并发', '技术选型'] }
      ]
    } else {
      careerPaths.value = [
        { title: '技术专家', industry: '互联网/科技', match: 80, description: '建议深耕技术领域，成为特定领域的专家', requiredSkills: ['核心技术', '问题解决', '持续学习'] },
        { title: '技术管理', industry: '互联网/科技', match: 70, description: '适合有一定技术积累后，向技术管理方向发展', requiredSkills: ['团队管理', '项目管理', '沟通协调'] }
      ]
    }
  } else {
    careerPaths.value = [
      { title: '技术专家', industry: '互联网/科技', match: 80, description: '建议深耕技术领域，成为特定领域的专家', requiredSkills: ['核心技术', '问题解决', '持续学习'] },
      { title: '技术管理', industry: '互联网/科技', match: 70, description: '适合有一定技术积累后，向技术管理方向发展', requiredSkills: ['团队管理', '项目管理', '沟通协调'] }
    ]
  }
}

const generateSkillImprovements = () => {
  const improvements = []
  
  if (profile.value.skills) {
    const userSkills = profile.value.skills.split(',').map(s => s.trim().toLowerCase())
    
    if (!userSkills.some(s => ['typescript', 'ts'].includes(s))) {
      improvements.push({ skill: 'TypeScript', suggestion: 'TypeScript已成为前端开发主流，建议系统学习并应用到项目中', resources: ['官方文档', '在线教程', '实战项目'] })
    }
    
    if (!userSkills.some(s => ['docker', 'kubernetes', 'k8s'].includes(s))) {
      improvements.push({ skill: '容器化技术', suggestion: 'Docker和Kubernetes是现代开发的必备技能，建议从Docker基础开始学习', resources: ['Docker官方教程', 'Kubernetes入门', '实战练习'] })
    }
    
    if (!userSkills.some(s => ['mysql', 'postgresql', 'mongodb', 'redis'].includes(s))) {
      improvements.push({ skill: '数据库优化', suggestion: '数据库优化是后端开发的重要技能，建议深入学习SQL优化和索引设计', resources: ['MySQL优化指南', '索引设计', '性能调优'] })
    }
  }
  
  if (improvements.length === 0 || improvements.length < 2) {
    improvements.push({ skill: '持续学习', suggestion: '建议关注行业最新趋势和技术发展，不断提升自己', resources: ['技术博客', '开源项目', '技术社区', '技术大会'] })
  }
  
  skillImprovements.value = improvements.slice(0, 4)
}

onMounted(() => {
  fetchData()
})
</script>
