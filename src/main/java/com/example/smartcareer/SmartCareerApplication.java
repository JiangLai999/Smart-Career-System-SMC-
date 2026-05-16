package com.example.smartcareer;

import com.example.smartcareer.entity.SystemAdmin;
import com.example.smartcareer.entity.SystemSetting;
import com.example.smartcareer.repository.UserRepository;
import com.example.smartcareer.repository.SystemSettingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class SmartCareerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCareerApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminPassword = "admin123";
            userRepository.findByUsername("admin").ifPresentOrElse(
                user -> {
                    user.setPassword(passwordEncoder.encode(adminPassword));
                    user.setUserType("SYSTEM_ADMIN");
                    user.setStatus(1);
                    userRepository.save(user);
                    System.out.println("=== 管理员密码已重置: admin / " + adminPassword + " ===");
                },
                () -> {
                    SystemAdmin admin = new SystemAdmin();
                    admin.setUsername("admin");
                    admin.setPassword(passwordEncoder.encode(adminPassword));
                    admin.setEmail("admin@smartcareer.com");
                    admin.setPhone("13800000001");
                    admin.setUserType("SYSTEM_ADMIN");
                    admin.setStatus(1);
                    admin.setRealName("系统管理员");
                    admin.setPermissionLevel("SUPER");
                    userRepository.save(admin);
                    System.out.println("=== 管理员账号已创建: admin / " + adminPassword + " ===");
                }
            );
        };
    }

    @Bean
    public CommandLineRunner initSystemSettings(SystemSettingRepository settingRepository) {
        return args -> {
            if (settingRepository.count() == 0) {
                List<SystemSetting> defaultSettings = Arrays.asList(
                    createSetting("site.name", "智能职业规划系统", "网站名称", "STRING", "SYSTEM"),
                    createSetting("site.description", "专业的职业规划与求职平台", "网站描述", "STRING", "SYSTEM"),
                    createSetting("site.keywords", "职业规划,求职,招聘,简历", "网站关键词", "STRING", "SYSTEM"),
                    createSetting("site.icp", "", "ICP备案号", "STRING", "SYSTEM"),
                    createSetting("site.contact.email", "contact@example.com", "联系邮箱", "STRING", "SYSTEM"),
                    createSetting("site.contact.phone", "400-000-0000", "联系电话", "STRING", "SYSTEM"),
                    createSetting("upload.max.size", "10485760", "上传文件最大大小(字节)", "NUMBER", "SYSTEM"),
                    createSetting("upload.allowed.types", "jpg,jpeg,png,pdf,doc,docx", "允许上传的文件类型", "STRING", "SYSTEM"),
                    createSetting("job.max.active", "10", "企业最大活跃职位数", "NUMBER", "BUSINESS"),
                    createSetting("job.expire.days", "30", "职位默认过期天数", "NUMBER", "BUSINESS"),
                    createSetting("resume.max.count", "5", "求职者最大简历数", "NUMBER", "BUSINESS"),
                    createSetting("application.max.per.day", "50", "每日最大投递数", "NUMBER", "BUSINESS"),
                    createSetting("password.min.length", "6", "密码最小长度", "NUMBER", "SECURITY"),
                    createSetting("password.require.special", "false", "密码是否需要特殊字符", "BOOLEAN", "SECURITY"),
                    createSetting("session.timeout", "86400", "会话超时时间(秒)", "NUMBER", "SECURITY"),
                    createSetting("login.max.attempts", "5", "最大登录尝试次数", "NUMBER", "SECURITY"),
                    createSetting("email.enabled", "false", "是否启用邮件通知", "BOOLEAN", "OTHER"),
                    createSetting("sms.enabled", "false", "是否启用短信通知", "BOOLEAN", "OTHER"),
                    createSetting("audit.timeout.hours", "48", "企业审核超时时间(小时)", "NUMBER", "OTHER")
                );
                settingRepository.saveAll(defaultSettings);
                System.out.println("=== 系统设置初始化完成 ===");
            }
        };
    }

    private SystemSetting createSetting(String key, String value, String description, String type, String category) {
        SystemSetting setting = new SystemSetting();
        setting.setSettingKey(key);
        setting.setSettingValue(value);
        setting.setDescription(description);
        setting.setSettingType(type);
        setting.setCategory(category);
        return setting;
    }
}
