package com.example.smartcareer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据加密工具类
 * 用于敏感信息的加密、解密和脱敏处理
 */
@Component
public class DataEncryptionUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(DataEncryptionUtil.class);
    
    // 加密算法
    private static final String ALGORITHM = "AES";
    
    // 加密密钥 (生产环境应从配置文件读取)
    private static final String SECRET_KEY = "SmartCareer2026!";
    
    // 手机号正则
    private static final Pattern PHONE_PATTERN = Pattern.compile("(\\d{3})\\d{4}(\\d{4})");
    
    // 身份证号正则
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("(\\d{6})\\d{8}(\\d{4})");
    
    // 邮箱正则
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(\\w{2})\\w+(@\\w+\\.\\w+)");
    
    /**
     * AES加密
     */
    public String encrypt(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            logger.error("加密失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * AES解密
     */
    public String decrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return null;
        }
        
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("解密失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 手机号脱敏
     * 格式: 138****1234
     */
    public String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        
        Matcher matcher = PHONE_PATTERN.matcher(phone);
        if (matcher.matches()) {
            return matcher.group(1) + "****" + matcher.group(2);
        }
        
        return phone;
    }
    
    /**
     * 身份证号脱敏
     * 格式: 110101********1234
     */
    public String maskIdCard(String idCard) {
        if (idCard == null || (idCard.length() != 15 && idCard.length() != 18)) {
            return idCard;
        }
        
        Matcher matcher = ID_CARD_PATTERN.matcher(idCard);
        if (matcher.matches()) {
            return matcher.group(1) + "********" + matcher.group(2);
        }
        
        return idCard;
    }
    
    /**
     * 邮箱脱敏
     * 格式: ab***@example.com
     */
    public String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (matcher.matches()) {
            return matcher.group(1) + "***" + matcher.group(2);
        }
        
        return email;
    }
    
    /**
     * 姓名脱敏
     * 格式: 张*、张**
     */
    public String maskName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        } else if (name.length() >= 3) {
            return name.charAt(0) + "*".repeat(name.length() - 1);
        }
        
        return name;
    }
    
    /**
     * 银行卡号脱敏
     * 格式: 6222****1234
     */
    public String maskBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 8) {
            return bankCard;
        }
        
        int length = bankCard.length();
        return bankCard.substring(0, 4) + "****" + bankCard.substring(length - 4);
    }
    
    /**
     * 密码强度检查
     * 返回: 1-弱, 2-中, 3-强
     */
    public int checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }
        
        int strength = 0;
        
        // 长度检查
        if (password.length() >= 8) {
            strength++;
        }
        
        // 包含数字
        if (password.matches(".*\\d.*")) {
            strength++;
        }
        
        // 包含小写字母
        if (password.matches(".*[a-z].*")) {
            strength++;
        }
        
        // 包含大写字母
        if (password.matches(".*[A-Z].*")) {
            strength++;
        }
        
        // 包含特殊字符
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            strength++;
        }
        
        if (strength <= 2) {
            return 1; // 弱
        } else if (strength <= 4) {
            return 2; // 中
        } else {
            return 3; // 强
        }
    }
    
    /**
     * 生成随机验证码
     */
    public String generateVerifyCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append((int) (Math.random() * 10));
        }
        return code.toString();
    }
    
    /**
     * 生成随机密码
     */
    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
}
