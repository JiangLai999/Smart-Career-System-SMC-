package com.example.smartcareer.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 数据验证工具类
 * 用于各种数据格式的验证
 */
@Component
public class ValidationUtil {
    
    // 正则表达式模式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{4,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$");
    private static final Pattern CHINESE_NAME_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5]{2,20}$");
    private static final Pattern COMPANY_NAME_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9()（）]{2,100}$");
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    
    /**
     * 验证手机号
     */
    public boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * 验证邮箱
     */
    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 验证身份证号
     */
    public boolean isValidIdCard(String idCard) {
        if (idCard == null || !ID_CARD_PATTERN.matcher(idCard).matches()) {
            return false;
        }
        
        // 校验码验证
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idCard.charAt(i) - '0') * weights[i];
        }
        
        char checkCode = checkCodes[sum % 11];
        return idCard.charAt(17) == checkCode || 
               (idCard.charAt(17) == 'x' && checkCode == 'X');
    }
    
    /**
     * 验证用户名
     * 4-20位字母、数字、下划线
     */
    public boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * 验证密码强度
     * 至少8位，包含大小写字母和数字
     */
    public boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * 验证中文姓名
     */
    public boolean isValidChineseName(String name) {
        return name != null && CHINESE_NAME_PATTERN.matcher(name).matches();
    }
    
    /**
     * 验证企业名称
     */
    public boolean isValidCompanyName(String companyName) {
        return companyName != null && COMPANY_NAME_PATTERN.matcher(companyName).matches();
    }
    
    /**
     * 验证URL
     */
    public boolean isValidUrl(String url) {
        return url != null && URL_PATTERN.matcher(url).matches();
    }
    
    /**
     * 验证字符串长度
     */
    public boolean isValidLength(String str, int min, int max) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= min && length <= max;
    }
    
    /**
     * 验证数字范围
     */
    public boolean isValidRange(Integer num, int min, int max) {
        return num != null && num >= min && num <= max;
    }
    
    /**
     * 验证薪资范围
     */
    public boolean isValidSalaryRange(Integer min, Integer max) {
        if (min == null || max == null) {
            return false;
        }
        return min > 0 && max > 0 && min <= max;
    }
    
    /**
     * 验证工作经验年限
     */
    public boolean isValidWorkYears(Integer years) {
        return years != null && years >= 0 && years <= 50;
    }
    
    /**
     * 验证年龄范围
     */
    public boolean isValidAge(Integer age) {
        return age != null && age >= 18 && age <= 65;
    }
    
    /**
     * 验证性别
     */
    public boolean isValidGender(String gender) {
        return "男".equals(gender) || "女".equals(gender);
    }
    
    /**
     * 验证学历
     */
    public boolean isValidEducation(String education) {
        String[] validEducations = {"高中", "大专", "本科", "硕士", "博士", "其他"};
        for (String valid : validEducations) {
            if (valid.equals(education)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 验证职位状态
     */
    public boolean isValidJobStatus(String status) {
        String[] validStatuses = {"DRAFT", "PUBLISHED", "PAUSED", "CLOSED"};
        for (String valid : validStatuses) {
            if (valid.equals(status)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 验证申请状态
     */
    public boolean isValidApplicationStatus(String status) {
        String[] validStatuses = {"APPLIED", "VIEWED", "SCREENING_PASSED", 
                                  "INTERVIEWING", "HIRED", "REJECTED"};
        for (String valid : validStatuses) {
            if (valid.equals(status)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 验证非空字符串
     */
    public boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * 验证非空对象
     */
    public boolean isNotNull(Object obj) {
        return obj != null;
    }
    
    /**
     * 验证数组非空
     */
    public boolean isNotEmpty(Object[] array) {
        return array != null && array.length > 0;
    }
    
    /**
     * 验证集合非空
     */
    public boolean isNotEmpty(java.util.Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
    
    /**
     * 验证Map非空
     */
    public boolean isNotEmpty(java.util.Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }
}
