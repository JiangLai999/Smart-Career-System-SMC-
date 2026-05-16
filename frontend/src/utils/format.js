/**
 * 格式化工具函数
 * 用于数据格式化处理
 */

/**
 * 手机号脱敏
 * 格式: 138****1234
 */
export function maskPhone(phone) {
  if (!phone || phone.length !== 11) return phone
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

/**
 * 身份证号脱敏
 * 格式: 110101********1234
 */
export function maskIdCard(idCard) {
  if (!idCard || (idCard.length !== 15 && idCard.length !== 18)) return idCard
  return idCard.replace(/(\d{6})\d{8}(\d{4})/, '$1********$2')
}

/**
 * 邮箱脱敏
 * 格式: ab***@example.com
 */
export function maskEmail(email) {
  if (!email || !email.includes('@')) return email
  return email.replace(/(\w{2})\w+(@\w+\.\w+)/, '$1***$2')
}

/**
 * 姓名脱敏
 * 格式: 张*、张**
 */
export function maskName(name) {
  if (!name) return name
  if (name.length === 2) return name[0] + '*'
  if (name.length >= 3) return name[0] + '*'.repeat(name.length - 1)
  return name
}

/**
 * 银行卡号脱敏
 * 格式: 6222****1234
 */
export function maskBankCard(bankCard) {
  if (!bankCard || bankCard.length < 8) return bankCard
  const length = bankCard.length
  return bankCard.substring(0, 4) + '****' + bankCard.substring(length - 4)
}

/**
 * 格式化金额
 * 格式: 1,000.00
 */
export function formatMoney(amount, decimals = 2) {
  if (amount === null || amount === undefined) return '0.00'
  const num = Number(amount)
  return num.toFixed(decimals).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

/**
 * 格式化薪资范围
 * 格式: 10K-20K
 */
export function formatSalary(min, max) {
  if (!min && !max) return '面议'
  if (!min) return `${formatK(max)}K以下`
  if (!max) return `${formatK(min)}K以上`
  if (min === max) return `${formatK(min)}K`
  return `${formatK(min)}K-${formatK(max)}K`
}

/**
 * 格式化为K
 */
function formatK(num) {
  if (!num) return 0
  const k = num / 1000
  return k >= 1 ? Math.round(k) : (k).toFixed(1)
}

/**
 * 格式化日期
 */
export function formatDate(date, format = 'YYYY-MM-DD') {
  if (!date) return ''
  
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

/**
 * 格式化相对时间
 * 例如: 刚刚、5分钟前、3小时前等
 */
export function formatRelativeTime(date) {
  if (!date) return ''
  
  const now = new Date()
  const d = new Date(date)
  const diff = now - d
  
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  const week = 7 * day
  const month = 30 * day
  const year = 365 * day
  
  if (diff < minute) return '刚刚'
  if (diff < hour) return `${Math.floor(diff / minute)}分钟前`
  if (diff < day) return `${Math.floor(diff / hour)}小时前`
  if (diff < week) return `${Math.floor(diff / day)}天前`
  if (diff < month) return `${Math.floor(diff / week)}周前`
  if (diff < year) return `${Math.floor(diff / month)}个月前`
  return `${Math.floor(diff / year)}年前`
}

/**
 * 格式化文件大小
 */
export function formatFileSize(bytes) {
  if (bytes === 0) return '0 B'
  
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  
  return (bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i]
}

/**
 * 格式化百分比
 */
export function formatPercent(value, decimals = 2) {
  if (value === null || value === undefined) return '0%'
  return (Number(value) * 100).toFixed(decimals) + '%'
}

/**
 * 格式化工作经验
 */
export function formatWorkExperience(years) {
  if (!years || years === 0) return '无经验'
  if (years < 1) return '1年以下'
  if (years >= 10) return '10年以上'
  return `${years}年经验`
}

/**
 * 格式化学历
 */
export function formatEducation(education) {
  const educationMap = {
    'HIGH_SCHOOL': '高中',
    'COLLEGE': '大专',
    'BACHELOR': '本科',
    'MASTER': '硕士',
    'DOCTOR': '博士',
    '高中': '高中',
    '大专': '大专',
    '本科': '本科',
    '硕士': '硕士',
    '博士': '博士'
  }
  return educationMap[education] || education
}

/**
 * 格式化职位状态
 */
export function formatJobStatus(status) {
  const statusMap = {
    'DRAFT': '草稿',
    'PUBLISHED': '已发布',
    'PAUSED': '已暂停',
    'CLOSED': '已关闭'
  }
  return statusMap[status] || status
}

/**
 * 格式化申请状态
 */
export function formatApplicationStatus(status) {
  const statusMap = {
    'APPLIED': '已申请',
    'VIEWED': '已查看',
    'SCREENING_PASSED': '通过初筛',
    'INTERVIEWING': '面试中',
    'HIRED': '已录用',
    'REJECTED': '已拒绝'
  }
  return statusMap[status] || status
}

/**
 * 格式化企业规模
 */
export function formatCompanyScale(scale) {
  const scaleMap = {
    '0-20': '0-20人',
    '20-99': '20-99人',
    '100-499': '100-499人',
    '500-999': '500-999人',
    '1000-9999': '1000-9999人',
    '10000+': '10000人以上'
  }
  return scaleMap[scale] || scale
}

/**
 * 格式化融资阶段
 */
export function formatFinancingStage(stage) {
  const stageMap = {
    'ANGEL': '天使轮',
    'A': 'A轮',
    'B': 'B轮',
    'C': 'C轮',
    'D': 'D轮',
    'IPO': '已上市',
    'UNFINANCED': '未融资'
  }
  return stageMap[stage] || stage
}

/**
 * 格式化性别
 */
export function formatGender(gender) {
  const genderMap = {
    'MALE': '男',
    'FEMALE': '女',
    '男': '男',
    '女': '女'
  }
  return genderMap[gender] || gender
}

/**
 * 截断字符串
 */
export function truncate(str, length, suffix = '...') {
  if (!str) return ''
  if (str.length <= length) return str
  return str.substring(0, length) + suffix
}

/**
 * 首字母大写
 */
export function capitalize(str) {
  if (!str) return ''
  return str.charAt(0).toUpperCase() + str.slice(1)
}

/**
 * 驼峰转下划线
 */
export function camelToSnake(str) {
  return str.replace(/([A-Z])/g, '_$1').toLowerCase()
}

/**
 * 下划线转驼峰
 */
export function snakeToCamel(str) {
  return str.replace(/_([a-z])/g, (match, letter) => letter.toUpperCase())
}

export default {
  maskPhone,
  maskIdCard,
  maskEmail,
  maskName,
  maskBankCard,
  formatMoney,
  formatSalary,
  formatDate,
  formatRelativeTime,
  formatFileSize,
  formatPercent,
  formatWorkExperience,
  formatEducation,
  formatJobStatus,
  formatApplicationStatus,
  formatCompanyScale,
  formatFinancingStage,
  formatGender,
  truncate,
  capitalize,
  camelToSnake,
  snakeToCamel
}
