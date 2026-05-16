import axios from 'axios'

// API基础URL配置
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 根据当前路径选择正确的token
    const path = window.location.pathname
    let token = ''
    
    if (path.startsWith('/admin')) {
      token = localStorage.getItem('adminToken')
    } else if (path.startsWith('/company')) {
      token = localStorage.getItem('companyToken')
    } else {
      token = localStorage.getItem('token')
    }
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    const res = response.data
    // 根据后端返回格式处理
    if (res.code === 200 || res.code === undefined) {
      return res.data !== undefined ? res.data : res
    } else {
      console.error('API Error:', res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    console.error('Response error:', error)
    if (error.response) {
      switch (error.response.status) {
        case 401: {
          // 清除对应端的 token，并跳回登录页
          const path = window.location.pathname
          if (path.startsWith('/admin')) {
            localStorage.removeItem('adminToken')
            window.location.href = '/admin/login'
          } else if (path.startsWith('/company')) {
            localStorage.removeItem('companyToken')
            localStorage.removeItem('companyInfo')
            window.location.href = '/company/login'
          } else {
            localStorage.removeItem('token')
            window.location.href = '/login'
          }
          break
        }
        case 403:
          console.error('权限不足')
          break
        case 404:
          console.error('资源不存在')
          break
        case 500:
          console.error('服务器错误')
          break
        default:
          console.error(error.response.data?.message || '请求失败')
      }
    } else if (error.request) {
      console.error('网络错误，请检查网络连接')
    } else {
      console.error('请求配置错误:', error.message)
    }
    return Promise.reject(error)
  }
)

// API基础信息
export const getApiBaseUrl = () => API_BASE_URL

// 导出 axios 实例供直接使用
export { api as axiosInstance }

export default {
  // 直接访问 axios 实例
  $post: api.post.bind(api),
  $get: api.get.bind(api),
  $put: api.put.bind(api),
  $delete: api.delete.bind(api),

  // 用户相关
  register(data) {
    return api.post('/user/register', data)
  },
  login(data) {
    return api.post('/user/login', data)
  },
  adminLogin(data) {
    return api.post('/admin/login', data)
  },
  getUserInfo() {
    return api.get('/user/info')
  },
  getUserById(id) {
    return api.get(`/user/${id}`)
  },
  
  // 职位相关
  getJobList(params) {
    return api.get('/job/list', { params })
  },
  getJobById(id) {
    return api.get(`/job/${id}`)
  },
  searchJobs(params) {
    return api.get('/job/search', { params })
  },
  publishJob(data) {
    return api.post('/job/publish', data)
  },
  updateJob(id, data) {
    return api.put(`/job/${id}`, data)
  },
  deleteJob(id) {
    return api.delete(`/job/${id}`)
  },
  getJobsByEnterprise(enterpriseId) {
    return api.get(`/job/enterprise/${enterpriseId}`)
  },
  
  // 申请相关
  applyForJob(data) {
    return api.post('/application/apply', data)
  },
  checkApplicationStatus(jobId) {
    return api.get(`/application/check/${jobId}`)
  },
  getMyApplications(params) {
    return api.get('/application/my', { params })
  },
  getApplicationsByJob(params) {
    return api.get('/application/job', { params })
  },
  getApplicationById(id) {
    return api.get(`/application/${id}`)
  },
  updateApplicationStatus(id, status) {
    return api.put(`/application/${id}/status`, null, { params: { status } })
  },
  cancelApplication(id) {
    return api.delete(`/application/${id}`)
  },
  
  // 推荐相关
  getRecommendedJobs(limit = 10) {
    return api.get('/recommendation/jobs', { params: { limit } })
  },
  
  // 求职者相关
  getJobSeekerProfile() {
    return api.get('/jobseeker/profile')
  },
  updateJobSeekerProfile(data) {
    return api.put('/jobseeker/profile', data)
  },
  updateAvatar(avatarUrl) {
    return api.put('/jobseeker/avatar', { avatarUrl })
  },
  getJobSeekerById(id) {
    return api.get(`/jobseeker/${id}`)
  },
  
  // 统计数据
  getUserStats() {
    return api.get('/application/stats')
  },
  
  // 职业测评
  submitAssessment(data) {
    return api.post('/assessment/submit', data)
  },
  getMyAssessments() {
    return api.get('/assessment/my')
  },
  getAssessmentById(id) {
    return api.get(`/assessment/${id}`)
  },
  getAssessmentStatistics() {
    return api.get('/assessment/statistics')
  },

  // 企业HR相关
  // 企业注册
  companyRegister(data) {
    return api.post('/company/register', data)
  },
  companyLogin(data) {
    return api.post('/company/login', data)
  },
  getCompanyInfo() {
    return api.get('/company/info')
  },
  getCompanyRegisterStatus() {
    return api.get('/company/register-status')
  },
  // 企业端重置密码
  companyResetPassword(account, code, newPassword) {
    return api.post('/company/reset-password', { account, code, newPassword })
  },

  // 职位管理
  getCompanyJobs(params) {
    return api.get('/company/jobs', { params })
  },
  getCompanyJob(id) {
    return api.get(`/company/job/${id}`)
  },
  createCompanyJob(data) {
    return api.post('/company/job', data)
  },
  updateCompanyJob(id, data) {
    return api.put(`/company/job/${id}`, data)
  },
  deleteCompanyJob(id) {
    return api.delete(`/company/job/${id}`)
  },
  toggleJobStatus(id, status) {
    return api.put(`/company/job/${id}/status`, null, { params: { status } })
  },
  extendJobValidity(id, days) {
    return api.put(`/company/job/${id}/extend`, null, { params: { days } })
  },

  // 简历管理
  getCompanyApplications(params) {
    return api.get('/company/applications', { params })
  },
  getResumesByJob(jobId, params) {
    return api.get(`/company/job/${jobId}/resumes`, { params })
  },
  filterResumes(params) {
    return api.get('/company/resumes/filter', { params })
  },
  getResumeById(id) {
    return api.get(`/company/resume/${id}`)
  },
  updateApplicationStatus(id, status, data = {}) {
    return api.put(`/company/application/${id}/status`, data, { params: { status } })
  },
  markResume(id, tags) {
    return api.put(`/company/resume/${id}/mark`, tags)
  },
  saveFilterCondition(data) {
    return api.post('/company/filter/save', data)
  },
  getSavedFilters() {
    return api.get('/company/filter/list')
  },

  // 统计数据
  getCompanyStats() {
    return api.get('/company/stats')
  },

  // 企业通知
  getCompanyNotifications(params) {
    return api.get('/company/notification/page', { params })
  },
  getCompanyUnreadCount() {
    return api.get('/company/notification/unread-count')
  },
  markCompanyNotificationAsRead(id) {
    return api.put(`/company/notification/${id}/read`)
  },
  markAllCompanyNotificationsAsRead() {
    return api.put('/company/notification/read-all')
  },

  // 获取测评分类
  getAssessmentCategories() {
    return api.get('/assessment/categories')
  },
  // 获取测评题目
  getQuestionsByCategory(category) {
    return api.get(`/assessment/questions/${category}`)
  },
  // 按类型获取测评
  getAssessmentsByType(type) {
    return api.get(`/assessment/my/type/${type}`)
  },
  // 删除测评记录
  deleteAssessment(id) {
    return api.delete(`/assessment/${id}`)
  },

  // ==================== 面试管理 ====================
  // 创建面试邀请
  createInterview(data) {
    return api.post('/interview/create', data)
  },
  // 更新面试信息
  updateInterview(id, data) {
    return api.put(`/interview/${id}`, data)
  },
  // 获取面试详情
  getInterviewById(id) {
    return api.get(`/interview/${id}`)
  },
  // 删除/取消面试
  deleteInterview(id) {
    return api.delete(`/interview/${id}`)
  },
  // 企业获取面试列表
  getEnterpriseInterviews(params) {
    return api.get('/interview/enterprise', { params })
  },
  // 企业按状态获取面试
  getEnterpriseInterviewsByStatus(status, params) {
    return api.get(`/interview/enterprise/status/${status}`, { params })
  },
  // 求职者获取面试列表
  getJobSeekerInterviews(params) {
    return api.get('/interview/jobseeker', { params })
  },
  // 求职者按状态获取面试
  getJobSeekerInterviewsByStatus(status) {
    return api.get(`/interview/jobseeker/status/${status}`)
  },
  // 确认面试
  confirmInterview(id) {
    return api.post(`/interview/${id}/confirm`)
  },
  // 拒绝面试
  rejectInterview(id, reason) {
    return api.post(`/interview/${id}/reject`, { reason })
  },
  // 提交面试反馈
  submitInterviewFeedback(id, feedback, result) {
    return api.post(`/interview/${id}/feedback`, { feedback, result })
  },
  // 取消面试
  cancelInterview(id, reason) {
    return api.post(`/interview/${id}/cancel`, { reason })
  },
  // 获取面试统计
  getInterviewStatistics() {
    return api.get('/interview/statistics')
  },
  // 获取即将到来的面试
  getUpcomingInterviews() {
    return api.get('/interview/upcoming')
  },

  // ==================== 通知管理 ====================
  // 获取通知列表
  getNotifications() {
    return api.get('/notification/list')
  },
  // 分页获取通知
  getNotificationsPage(params) {
    return api.get('/notification/page', { params })
  },
  // 获取未读通知
  getUnreadNotifications() {
    return api.get('/notification/unread')
  },
  // 获取未读数量
  getUnreadCount() {
    return api.get('/notification/unread-count')
  },
  // 获取通知统计
  getNotificationStatistics() {
    return api.get('/notification/statistics')
  },
  // 获取通知详情
  getNotificationById(id) {
    return api.get(`/notification/${id}`)
  },
  // 标记已读
  markNotificationAsRead(id) {
    return api.put(`/notification/${id}/read`)
  },
  // 全部标记已读
  markAllNotificationsAsRead() {
    return api.put('/notification/read-all')
  },
  // 删除通知
  deleteNotification(id) {
    return api.delete(`/notification/${id}`)
  },

  // ==================== 系统公告 ====================
  // 创建公告（管理端）
  createAnnouncement(data) {
    return api.post('/announcement/admin/create', data)
  },
  // 更新公告（管理端）
  updateAnnouncement(id, data) {
    return api.put(`/announcement/admin/${id}`, data)
  },
  // 删除公告（管理端）
  deleteAnnouncement(id) {
    return api.delete(`/announcement/admin/${id}`)
  },
  // 发布公告（管理端）
  publishAnnouncement(id) {
    return api.post(`/announcement/admin/${id}/publish`)
  },
  // 取消发布公告（管理端）
  unpublishAnnouncement(id) {
    return api.post(`/announcement/admin/${id}/unpublish`)
  },
  // 获取公告列表（管理端）
  getAnnouncementList(params) {
    return api.get('/announcement/admin/list', { params })
  },
  // 获取公告统计（管理端）
  getAnnouncementStatistics() {
    return api.get('/announcement/admin/statistics')
  },
  // 获取公告详情
  getAnnouncementById(id) {
    return api.get(`/announcement/${id}`)
  },
  // 获取有效公告
  getActiveAnnouncements() {
    return api.get('/announcement/active')
  },
  // 分页获取有效公告
  getActiveAnnouncementsPage(params) {
    return api.get('/announcement/active/page', { params })
  },

  // ==================== 验证码 ====================
  // 发送验证码
  sendVerificationCode(phone, purpose) {
    return api.post('/verification/send', { phone, purpose })
  },
  // 验证验证码
  verifyCode(phone, code, purpose) {
    return api.post('/verification/verify', { phone, code, purpose })
  },

  // ==================== 密码管理 ====================
  // 重置密码
  resetPassword(phone, code, newPassword) {
    return api.post('/user/reset-password', { phone, code, newPassword })
  },
  // 修改密码
  changePassword(oldPassword, newPassword) {
    return api.post('/user/change-password', { oldPassword, newPassword })
  },

  // ==================== 职位筛选 ====================
  // 多条件筛选职位 (使用POST避免中文编码问题)
  filterJobs(params) {
    const { page, size, ...body } = params
    const queryParams = new URLSearchParams()
    if (page !== undefined) queryParams.append('page', page)
    if (size !== undefined) queryParams.append('size', size)
    const url = '/job/filter' + (queryParams.toString() ? '?' + queryParams.toString() : '')
    return api.post(url, body)
  },
  // POST方式筛选职位
  filterJobsPost(data) {
    return api.post('/job/filter', data)
  },

  // ==================== 企业信息管理 ====================
  // 更新企业信息
  updateCompanyInfo(data) {
    return api.put('/company/info', data)
  },

  // ==================== 管理员 - 权限管理 ====================
  // 获取所有权限
  getAllPermissions() {
    return api.get('/admin/permission/list')
  },
  // 按模块获取权限
  getPermissionsByModule(module) {
    return api.get(`/admin/permission/module/${module}`)
  },
  // 获取所有模块
  getAllModules() {
    return api.get('/admin/permission/modules')
  },
  // 获取所有角色
  getAllRoles() {
    return api.get('/admin/permission/role/list')
  },
  // 按角色码获取角色（推荐使用）
  getRoleByCode(roleCode) {
    return api.get(`/admin/permission/role/code/${roleCode}`)
  },
  // 获取用户权限
  getUserPermissions(userId) {
    return api.get(`/admin/permission/user/${userId}`)
  },
  // 分配角色
  assignRoleToUser(userId, roleId) {
    return api.post(`/admin/permission/user/${userId}/role/${roleId}`)
  },
  // 移除角色
  removeRoleFromUser(userId, roleId) {
    return api.delete(`/admin/permission/user/${userId}/role/${roleId}`)
  },
  // 更新权限
  updatePermissions(data) {
    return api.put('/admin/permission/update', data)
  },
  // 创建角色
  createRole(data) {
    return api.post('/admin/permission/role/create', data)
  },
  // 更新角色
  updateRole(roleId, data) {
    return api.put(`/admin/permission/role/${roleId}`, data)
  },
  // 删除角色
  deleteRole(roleId) {
    return api.delete(`/admin/permission/role/${roleId}`)
  },
  // 创建权限
  createPermission(data) {
    return api.post('/admin/permission/create', data)
  },
  // 删除权限
  deletePermission(permissionId) {
    return api.delete(`/admin/permission/${permissionId}`)
  },
  // 检查权限
  hasPermission(userId, permissionCode) {
    return api.get(`/admin/permission/check/${userId}/${permissionCode}`)
  },
  // 检查角色
  hasRole(userId, roleCode) {
    return api.get(`/admin/permission/check-role/${userId}/${roleCode}`)
  },
  // 获取角色权限
  getRolePermissions(roleId) {
    return api.get(`/admin/permission/role/${roleId}/permissions`)
  },

  // ==================== 管理端 ====================
  // 获取仪表盘统计数据
  getAdminDashboard() {
    return api.get('/admin/dashboard')
  },
  // 获取用户列表
  getAdminUsers(params) {
    return api.get('/admin/users', { params })
  },
  // 获取用户详情
  getAdminUserDetail(userId) {
    return api.get(`/admin/users/${userId}`)
  },
  // 更新用户信息
  updateAdminUser(userId, data) {
    return api.put(`/admin/users/${userId}`, data)
  },
  // 禁用用户
  disableUser(userId) {
    return api.post(`/admin/users/${userId}/disable`)
  },
  // 启用用户
  enableUser(userId) {
    return api.post(`/admin/users/${userId}/enable`)
  },
  // 删除用户
  deleteAdminUser(userId) {
    return api.delete(`/admin/users/${userId}`)
  },
  // 警告用户
  warnUser(userId, reason) {
    return api.post(`/admin/users/${userId}/warn`, { reason })
  },
  // 获取企业审核列表
  getAdminAudits(params) {
    return api.get('/admin/audits', { params })
  },
  // 获取待审核企业
  getPendingAudits(params) {
    return api.get('/admin/audits/pending', { params })
  },
  // 获取审核详情
  getAuditDetail(auditId) {
    return api.get(`/admin/audits/${auditId}`)
  },
  // 审核通过
  approveAudit(auditId, comment) {
    return api.post(`/admin/audits/${auditId}/approve`, { comment })
  },
  // 审核拒绝
  rejectAudit(auditId, comment) {
    return api.post(`/admin/audits/${auditId}/reject`, { comment })
  },
  // 要求补充信息
  requestAuditInfo(auditId, content) {
    return api.post(`/admin/audits/${auditId}/request-info`, { content })
  },
  // 获取系统日志
  getAdminLogs(params) {
    return api.get('/admin/logs', { params })
  },
  // 获取系统设置
  getAdminSettings() {
    return api.get('/admin/settings')
  },
  // 获取分类设置
  getAdminSettingsByCategory(category, params) {
    return api.get(`/admin/settings/category/${category}`, { params })
  },
  // 更新设置
  updateAdminSetting(settingId, data) {
    return api.put(`/admin/settings/${settingId}`, data)
  },
  // 获取设置变更日志
  getSettingChangeLogs(params) {
    return api.get('/admin/settings/logs/all', { params })
  }
}
