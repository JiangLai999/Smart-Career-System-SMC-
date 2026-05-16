<template>
  <div class="applications-page">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="navbar-container">
        <h1 class="logo" @click="$router.push('/')">智能职业规划系统</h1>
        <div class="navbar-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32">{{ userInfo?.username?.charAt(0) }}</el-avatar>
              <span>{{ userInfo?.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="applications">我的申请</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 申请列表 -->
    <div class="applications-container">
      <div class="page-header">
        <h1>我的申请</h1>
        <p>查看和管理您的求职进度</p>
      </div>

      <!-- 状态筛选 -->
      <div class="filter-tabs">
        <el-radio-group v-model="statusFilter" @change="handleFilterChange">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="APPLIED">已投递</el-radio-button>
          <el-radio-button label="VIEWED">已查看</el-radio-button>
          <el-radio-button label="SCREENING">筛选中</el-radio-button>
          <el-radio-button label="INTERVIEW">面试中</el-radio-button>
          <el-radio-button label="OFFERED">已录用</el-radio-button>
          <el-radio-button label="REJECTED">已拒绝</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 申请列表 -->
      <div v-loading="loading" class="applications-list">
        <el-empty v-if="!loading && applications.length === 0" description="暂无申请记录" />
        
        <el-card v-for="app in applications" :key="app.id" class="application-item">
          <div class="application-content">
            <div class="application-info">
              <h3 class="job-title" @click="$router.push(`/job/${app.job?.id}`)">
                {{ app.job?.title }}
              </h3>
              <div class="job-company">{{ app.enterpriseName }}</div>
              <div class="job-tags">
                <el-tag size="small">{{ app.job?.location }}</el-tag>
                <el-tag size="small" type="info">{{ app.job?.salaryMin }}-{{ app.job?.salaryMax }}K</el-tag>
              </div>
              <div class="apply-time">投递时间：{{ formatTime(app.applyTime) }}</div>
            </div>
            <div class="application-status">
              <el-tag :type="getStatusType(app.status)" size="large">
                {{ getStatusText(app.status) }}
              </el-tag>
              <div v-if="app.status === 'INTERVIEW'" class="interview-info">
                <p>面试时间：{{ formatTime(app.interviewTime) }}</p>
                <el-button type="primary" size="small">查看详情</el-button>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import api from '@/api'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const applications = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const statusFilter = ref('')

const userInfo = computed(() => userStore.userInfo)

const fetchApplications = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value
    }
    
    const data = await api.getMyApplications(params)
    applications.value = data.content || []
    total.value = data.totalElements || 0
  } catch (error) {
    console.error('获取申请列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleFilterChange = () => {
  currentPage.value = 1
  fetchApplications()
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchApplications()
}

const getStatusType = (status) => {
  const types = {
    'APPLIED': 'primary',
    'VIEWED': 'warning',
    'SCREENING': 'warning',
    'INTERVIEW': 'success',
    'OFFERED': 'success',
    'REJECTED': 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    'APPLIED': '已投递',
    'VIEWED': '已查看',
    'SCREENING': '筛选中',
    'INTERVIEW': '面试中',
    'OFFERED': '已录用',
    'REJECTED': '已拒绝'
  }
  return texts[status] || status
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'applications':
      router.push('/applications')
      break
    case 'logout':
      userStore.logout()
      router.push('/')
      break
  }
}

onMounted(() => {
  fetchApplications()
})
</script>

<style scoped>
.applications-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.navbar {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #667eea;
  cursor: pointer;
  margin: 0;
}

.navbar-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.applications-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 30px 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  margin-bottom: 10px;
}

.page-header p {
  color: #666;
}

.filter-tabs {
  margin-bottom: 20px;
}

.applications-list {
  min-height: 300px;
}

.application-item {
  margin-bottom: 15px;
}

.application-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.application-info {
  flex: 1;
}

.job-title {
  font-size: 18px;
  margin: 0 0 10px 0;
  cursor: pointer;
  color: #333;
}

.job-title:hover {
  color: #409EFF;
}

.job-company {
  color: #666;
  margin-bottom: 10px;
}

.job-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
}

.apply-time {
  color: #999;
  font-size: 12px;
}

.application-status {
  text-align: right;
  min-width: 120px;
}

.interview-info {
  margin-top: 10px;
}

.interview-info p {
  color: #666;
  font-size: 12px;
  margin-bottom: 5px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}
</style>
