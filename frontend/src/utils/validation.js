/**
 * 数据验证工具函数
 * 用于前端数据格式验证
 */

/**
 * 验证手机号
 */
export function isValidPhone(phone) {
  return /^1[3-9]\d{9}$/.test(phone)
}

/**
 * 验证邮箱
 */
export function isValidEmail(email) {
  return /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(email)
}

/**
 * 验证身份证号
 */
export function isValidIdCard(idCard) {
  // 15位或18位身份证号
  if (!/^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[0-9Xx]$/.test(idCard)) {
    return false
  }
  
  // 校验码验证
  const weights = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2]
  const checkCodes = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2']
  
  let sum = 0
  for (let i = 0; i < 17; i++) {
    sum += parseInt(idCard[i]) * weights[i]
  }
  
  const checkCode = checkCodes[sum % 11]
  return idCard[17].toUpperCase() === checkCode
}

/**
 * 验证用户名
 * 4-20位字母、数字、下划线
 */
export function isValidUsername(username) {
  return /^[a-zA-Z0-9_]{4,20}$/.test(username)
}

/**
 * 验证密码强度
 * 至少8位，包含大小写字母和数字
 */
export function isValidPassword(password) {
  return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/.test(password)
}

/**
 * 检查密码强度
 * 返回: 1-弱, 2-中, 3-强
 */
export function checkPasswordStrength(password) {
  if (!password) return 0
  
  let strength = 0
  
  // 长度检查
  if (password.length >= 8) strength++
  
  // 包含数字
  if (/\d/.test(password)) strength++
  
  // 包含小写字母
  if (/[a-z]/.test(password)) strength++
  
  // 包含大写字母
  if (/[A-Z]/.test(password)) strength++
  
  // 包含特殊字符
  if (/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>/?]/.test(password)) strength++
  
  if (strength <= 2) return 1 // 弱
  if (strength <= 4) return 2 // 中
  return 3 // 强
}

/**
 * 验证中文姓名
 */
export function isValidChineseName(name) {
  return /^[\u4e00-\u9fa5]{2,20}$/.test(name)
}

/**
 * 验证企业名称
 */
export function isValidCompanyName(companyName) {
  return /^[\u4e00-\u9fa5a-zA-Z0-9()（）]{2,100}$/.test(companyName)
}

/**
 * 验证URL
 */
export function isValidUrl(url) {
  return /^(https?|ftp|file):\/\/[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]/.test(url)
}

/**
 * 验证字符串长度
 */
export function isValidLength(str, min, max) {
  if (!str) return false
  const length = str.length
  return length >= min && length <= max
}

/**
 * 验证数字范围
 */
export function isValidRange(num, min, max) {
  return num !== null && num !== undefined && num >= min && num <= max
}

/**
 * 验证薪资范围
 */
export function isValidSalaryRange(min, max) {
  if (min === null || max === null) return false
  return min > 0 && max > 0 && min <= max
}

/**
 * 验证工作经验年限
 */
export function isValidWorkYears(years) {
  return years !== null && years >= 0 && years <= 50
}

/**
 * 验证年龄范围
 */
export function isValidAge(age) {
  return age !== null && age >= 18 && age <= 65
}

/**
 * 验证性别
 */
export function isValidGender(gender) {
  return gender === '男' || gender === '女'
}

/**
 * 验证学历
 */
export function isValidEducation(education) {
  const validEducations = ['高中', '大专', '本科', '硕士', '博士', '其他']
  return validEducations.includes(education)
}

/**
 * 验证职位状态
 */
export function isValidJobStatus(status) {
  const validStatuses = ['DRAFT', 'PUBLISHED', 'PAUSED', 'CLOSED']
  return validStatuses.includes(status)
}

/**
 * 验证申请状态
 */
export function isValidApplicationStatus(status) {
  const validStatuses = ['APPLIED', 'VIEWED', 'SCREENING_PASSED', 
                        'INTERVIEWING', 'HIRED', 'REJECTED']
  return validStatuses.includes(status)
}

/**
 * 非空验证
 */
export function isNotEmpty(value) {
  if (value === null || value === undefined) return false
  if (typeof value === 'string') return value.trim().length > 0
  if (Array.isArray(value)) return value.length > 0
  if (typeof value === 'object') return Object.keys(value).length > 0
  return true
}

/**
 * 表单验证器
 */
export const validators = {
  required: (value, message = '此字段不能为空') => {
    return isNotEmpty(value) ? '' : message
  },
  
  phone: (value, message = '请输入正确的手机号') => {
    return !value || isValidPhone(value) ? '' : message
  },
  
  email: (value, message = '请输入正确的邮箱地址') => {
    return !value || isValidEmail(value) ? '' : message
  },
  
  idCard: (value, message = '请输入正确的身份证号') => {
    return !value || isValidIdCard(value) ? '' : message
  },
  
  username: (value, message = '用户名为4-20位字母、数字、下划线') => {
    return !value || isValidUsername(value) ? '' : message
  },
  
  password: (value, message = '密码至少8位，包含大小写字母和数字') => {
    return !value || isValidPassword(value) ? '' : message
  },
  
  minLength: (value, min, message) => {
    message = message || `长度不能少于${min}个字符`
    return !value || value.length >= min ? '' : message
  },
  
  maxLength: (value, max, message) => {
    message = message || `长度不能超过${max}个字符`
    return !value || value.length <= max ? '' : message
  },
  
  range: (value, min, max, message) => {
    message = message || `数值必须在${min}到${max}之间`
    const num = Number(value)
    return !value || (num >= min && num <= max) ? '' : message
  },
  
  custom: (value, validator, message = '验证失败') => {
    return validator(value) ? '' : message
  }
}

export default {
  isValidPhone,
  isValidEmail,
  isValidIdCard,
  isValidUsername,
  isValidPassword,
  checkPasswordStrength,
  isValidChineseName,
  isValidCompanyName,
  isValidUrl,
  isValidLength,
  isValidRange,
  isValidSalaryRange,
  isValidWorkYears,
  isValidAge,
  isValidGender,
  isValidEducation,
  isValidJobStatus,
  isValidApplicationStatus,
  isNotEmpty,
  validators
}
