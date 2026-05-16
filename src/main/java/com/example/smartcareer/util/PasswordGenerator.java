package com.example.smartcareer.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具
 * 用于生成测试账号的加密密码
 */
public class PasswordGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 生成测试密码
        String adminPassword = "admin123";
        String seekerPassword = "seeker123";
        String companyPassword = "company123";
        
        String encodedAdmin = encoder.encode(adminPassword);
        String encodedSeeker = encoder.encode(seekerPassword);
        String encodedCompany = encoder.encode(companyPassword);
        
        System.out.println("========================================");
        System.out.println("测试账号密码加密结果:");
        System.out.println("========================================");
        System.out.println("管理员账号: admin");
        System.out.println("原始密码: " + adminPassword);
        System.out.println("加密密码: " + encodedAdmin);
        System.out.println();
        System.out.println("求职者账号: seeker");
        System.out.println("原始密码: " + seekerPassword);
        System.out.println("加密密码: " + encodedSeeker);
        System.out.println();
        System.out.println("企业HR账号: company");
        System.out.println("原始密码: " + companyPassword);
        System.out.println("加密密码: " + encodedCompany);
        System.out.println("========================================");
        
        // 验证密码是否匹配
        System.out.println("\n验证密码匹配:");
        System.out.println("admin123 匹配: " + encoder.matches(adminPassword, encodedAdmin));
        System.out.println("seeker123 匹配: " + encoder.matches(seekerPassword, encodedSeeker));
        System.out.println("company123 匹配: " + encoder.matches(companyPassword, encodedCompany));
    }
}
