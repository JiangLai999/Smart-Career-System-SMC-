package com.example.smartcareer.controller;

import com.example.smartcareer.entity.*;
import com.example.smartcareer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/init")
public class DataInitController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private EnterpriseHRRepository enterpriseHRRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    @Qualifier("securityPasswordEncoder")
    private PasswordEncoder passwordEncoder;

    @PostMapping("/seed")
    public Map<String, Object> seedData() {
        Map<String, Object> result = new HashMap<>();

        try {
            createRoles();
            createAdminUser();
            createSeekerUsers();
            createCompanyUsers();
            createJobs();
            createApplications();

            result.put("success", true);
            result.put("message", "测试数据初始化成功");
            result.put("accounts", getAccountInfo());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "初始化失败: " + e.getMessage());
        }

        return result;
    }

    private void createRoles() {
        createRoleIfNotExists("ADMIN", "系统管理员", "系统管理员，拥有所有权限");
        createRoleIfNotExists("SEEKER", "求职者", "求职者角色");
        createRoleIfNotExists("COMPANY", "企业HR", "企业HR角色");
    }

    private void createRoleIfNotExists(String code, String name, String desc) {
        if (!roleRepository.existsByRoleName(name)) {
            Role role = new Role();
            role.setRoleCode(code);
            role.setRoleName(name);
            role.setDescription(desc);
            role.setStatus(1);
            role.setCreateTime(LocalDateTime.now());
            role.setUpdateTime(LocalDateTime.now());
            roleRepository.save(role);
        }
    }

    private void createAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            SystemAdmin admin = new SystemAdmin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@smartcareer.com");
            admin.setPhone("13800000001");
            admin.setUserType("SYSTEM_ADMIN");
            admin.setStatus(1);
            admin.setRealName("系统管理员");
            admin.setPermissionLevel("SUPER");
            userRepository.save(admin);
        }
    }

    private void createSeekerUsers() {
        String[][] seekers = {
            {"seeker", "seeker123", "张三", "男", "本科", "清华大学", "计算机科学与技术", "Java,Spring Boot,MySQL,Redis"},
            {"seeker2", "seeker123", "李四", "女", "硕士", "北京大学", "软件工程", "Python,Django,PostgreSQL,Docker"},
            {"seeker3", "seeker123", "王五", "男", "本科", "浙江大学", "数据科学", "Python,TensorFlow,机器学习,数据分析"},
            {"seeker4", "seeker123", "赵六", "女", "本科", "复旦大学", "信息安全", "网络安全,渗透测试,安全审计"},
            {"seeker5", "seeker123", "钱七", "男", "硕士", "上海交通大学", "人工智能", "深度学习,计算机视觉,NLP,PyTorch"}
        };

        for (String[] s : seekers) {
            if (!userRepository.existsByUsername(s[0])) {
                JobSeeker seeker = new JobSeeker();
                seeker.setUsername(s[0]);
                seeker.setPassword(passwordEncoder.encode(s[1]));
                seeker.setEmail(s[0] + "@test.com");
                seeker.setPhone("13800000" + String.format("%02d", Arrays.asList(seekers).indexOf(s) + 2));
                seeker.setUserType("JOB_SEEKER");
                seeker.setStatus(1);
                seeker.setRealName(s[2]);
                seeker.setGender(s[3]);
                seeker.setEducation(s[4]);
                seeker.setSchool(s[5]);
                seeker.setMajor(s[6]);
                seeker.setSkills(s[7]);
                seeker.setWorkYears(Arrays.asList(seekers).indexOf(s) + 1);
                seeker.setExpectedSalaryMin(15000 + Arrays.asList(seekers).indexOf(s) * 3000);
                seeker.setExpectedSalaryMax(25000 + Arrays.asList(seekers).indexOf(s) * 5000);
                seeker.setExpectedLocation("北京");
                seeker.setResume("本人具有" + (Arrays.asList(seekers).indexOf(s) + 1) + "年开发经验，熟悉" + s[7] + "等技术，具有良好的团队协作能力和问题解决能力。");
                userRepository.save(seeker);
            }
        }
    }

    private void createCompanyUsers() {
        String[][] companies = {
            {"示例科技有限公司", "91110100000000000X", "IT", "一家专注于人工智能和大数据的科技公司"},
            {"互联网创新公司", "91110101000000001Y", "互联网", "领先的互联网产品研发企业"},
            {"金融科技公司", "91110102000000002Z", "金融", "金融科技解决方案提供商"},
            {"电商平台公司", "91110103000000003A", "电商", "快速发展的电商平台"},
            {"智能硬件公司", "91110104000000004B", "硬件", "智能硬件产品研发与制造"}
        };

        for (String[] c : companies) {
            Enterprise enterprise = enterpriseRepository.findByEnterpriseName(c[0]).orElse(null);
            if (enterprise == null) {
                enterprise = new Enterprise();
                enterprise.setEnterpriseName(c[0]);
                enterprise.setLicenseNumber(c[1]);
                enterprise.setIndustry(c[2]);
                enterprise.setScale("MEDIUM_LARGE");
                enterprise.setDescription(c[3]);
                enterprise.setContactPerson("HR" + (Arrays.asList(companies).indexOf(c) + 1));
                enterprise.setContactPhone("1390000000" + Arrays.asList(companies).indexOf(c));
                enterprise.setContactEmail("hr@company" + (Arrays.asList(companies).indexOf(c) + 1) + ".com");
                enterprise.setAddress("北京市海淀区中关村大街" + (Arrays.asList(companies).indexOf(c) + 1) + "号");
                enterprise.setStatus(1);
                enterprise.setAuditStatus(1);
                enterprise = enterpriseRepository.save(enterprise);
            }

            String hrUsername = "company" + (Arrays.asList(companies).indexOf(c) == 0 ? "" : Arrays.asList(companies).indexOf(c));
            if (!userRepository.existsByUsername(hrUsername)) {
                EnterpriseHR hr = new EnterpriseHR();
                hr.setUsername(hrUsername);
                hr.setPassword(passwordEncoder.encode("company123"));
                hr.setEmail("hr" + Arrays.asList(companies).indexOf(c) + "@company.com");
                hr.setPhone("138000001" + String.format("%02d", Arrays.asList(companies).indexOf(c)));
                hr.setUserType("ENTERPRISE_HR");
                hr.setStatus(1);
                hr.setEnterprise(enterprise);
                hr.setRealName("HR" + Arrays.asList(companies).indexOf(c));
                hr.setPosition("人力资源经理");
                userRepository.save(hr);
            }
        }
    }

    private void createJobs() {
        List<Enterprise> enterprises = enterpriseRepository.findAll();
        
        String[][] jobs = {
            {"高级Java开发工程师", "技术", "北京", "本科", "5年", "25000", "45000", "负责公司核心业务系统的开发和维护，参与系统架构设计。", "1. 5年以上Java开发经验\n2. 熟悉Spring Boot、MyBatis等框架\n3. 熟悉MySQL、Redis等数据库\n4. 有分布式系统开发经验优先", "1. 参与需求分析和系统设计\n2. 负责核心模块开发\n3. 代码审查和技术分享", "五险一金,带薪年假,定期体检,节日福利"},
            {"前端开发工程师", "技术", "北京", "本科", "3年", "18000", "30000", "负责公司产品的前端开发，提升用户体验。", "1. 3年以上前端开发经验\n2. 精通Vue.js或React\n3. 熟悉TypeScript、ES6+\n4. 有移动端开发经验优先", "1. 负责前端架构设计和开发\n2. 优化前端性能\n3. 与后端协作完成功能开发", "五险一金,弹性工作,股票期权,技术培训"},
            {"产品经理", "产品", "北京", "本科", "3年", "20000", "35000", "负责产品的规划和设计，推动产品迭代优化。", "1. 3年以上产品经理经验\n2. 优秀的需求分析能力\n3. 熟悉产品设计工具\n4. 有B端产品经验优先", "1. 产品规划和路线图设计\n2. 需求调研和分析\n3. 产品原型设计\n4. 协调研发和设计团队", "五险一金,带薪年假,团建活动,生日礼物"},
            {"UI/UX设计师", "设计", "北京", "本科", "2年", "15000", "25000", "负责产品的UI设计和用户体验优化。", "1. 2年以上UI设计经验\n2. 精通Figma、Sketch等设计工具\n3. 有良好的审美和设计能力\n4. 了解前端开发流程", "1. 产品界面设计\n2. 交互原型设计\n3. 设计规范制定\n4. 与前端协作实现设计", "五险一金,弹性工作,设计大赛,学习基金"},
            {"数据分析师", "数据", "北京", "本科", "2年", "16000", "28000", "负责业务数据的分析和挖掘，提供数据支持。", "1. 2年以上数据分析经验\n2. 精通SQL、Excel\n3. 熟悉Python或R语言\n4. 有大数据平台经验优先", "1. 数据采集和清洗\n2. 数据分析和可视化\n3. 报告撰写和汇报\n4. 数据产品开发支持", "五险一金,带薪年假,技术分享,健身补贴"},
            {"Python开发工程师", "技术", "上海", "本科", "3年", "22000", "38000", "负责后端服务开发和数据处理。", "1. 3年以上Python开发经验\n2. 熟悉Django、Flask框架\n3. 熟悉PostgreSQL、MongoDB\n4. 有机器学习经验优先", "1. 后端API开发\n2. 数据处理和分析\n3. 系统性能优化", "五险一金,股票期权,远程办公,弹性工作"},
            {"算法工程师", "技术", "北京", "硕士", "2年", "30000", "50000", "负责机器学习算法的研发和优化。", "1. 硕士及以上学历\n2. 熟悉深度学习框架\n3. 有NLP或CV经验\n4. 发表过相关论文优先", "1. 算法模型设计\n2. 模型训练和优化\n3. 算法工程化落地", "五险一金,股票期权,学术会议,论文奖励"},
            {"测试工程师", "质量", "深圳", "本科", "2年", "14000", "24000", "负责产品质量保障和测试工作。", "1. 2年以上测试经验\n2. 熟悉自动化测试工具\n3. 有性能测试经验\n4. 熟悉Linux命令", "1. 测试用例设计\n2. 自动化测试开发\n3. 缺陷跟踪和管理", "五险一金,带薪年假,培训机会,弹性工作"},
            {"运维工程师", "运维", "北京", "本科", "3年", "18000", "30000", "负责系统运维和DevOps建设。", "1. 3年以上运维经验\n2. 熟悉Docker、Kubernetes\n3. 熟悉CI/CD流程\n4. 有云平台经验优先", "1. 系统部署和维护\n2. 监控告警配置\n3. 自动化运维建设", "五险一金,股票期权,技术培训,弹性工作"},
            {"HR经理", "人事", "北京", "本科", "5年", "20000", "35000", "负责人力资源管理和团队建设。", "1. 5年以上HR经验\n2. 熟悉人力资源各模块\n3. 有创业公司经验优先\n4. 良好的沟通能力", "1. 招聘管理\n2. 绩效考核\n3. 员工关系维护", "五险一金,带薪年假,团建活动,培训机会"}
        };

        for (String[] j : jobs) {
            if (jobRepository.count() < 50) {
                Enterprise enterprise = enterprises.get(new Random().nextInt(enterprises.size()));
                Job job = new Job();
                job.setEnterprise(enterprise);
                job.setTitle(j[0]);
                job.setCategory(j[1]);
                job.setLocation(j[2]);
                job.setEducation(j[3]);
                job.setExperience(j[4]);
                job.setSalaryMin(Integer.parseInt(j[5]));
                job.setSalaryMax(Integer.parseInt(j[6]));
                job.setRecruitNumber(new Random().nextInt(5) + 1);
                job.setDescription(j[7]);
                job.setRequirement(j[8]);
                job.setResponsibility(j[9]);
                job.setWelfare(j[10]);
                job.setWorkType("全职");
                job.setStatus("PUBLISHED");
                job.setPublishTime(LocalDateTime.now().minusDays(new Random().nextInt(30)));
                job.setViewCount(new Random().nextInt(500));
                job.setApplyCount(new Random().nextInt(50));
                jobRepository.save(job);
            }
        }
    }

    private void createApplications() {
        String[] statuses = {"APPLIED", "VIEWED", "SCREENING", "INTERVIEW", "OFFERED", "REJECTED"};
        
        List<Job> jobs = jobRepository.findAll();
        List<User> seekers = userRepository.findAll().stream()
            .filter(u -> "JOB_SEEKER".equals(u.getUserType()))
            .collect(java.util.stream.Collectors.toList());

        for (User seeker : seekers) {
            int applicationCount = new Random().nextInt(5) + 1;
            for (int i = 0; i < applicationCount && i < jobs.size(); i++) {
                Job job = jobs.get(new Random().nextInt(jobs.size()));
                
                if (applicationRepository.findByJobSeekerIdAndJobId(seeker.getId(), job.getId()).isEmpty()) {
                    Application application = new Application();
                    application.setJobSeeker((JobSeeker) seeker);
                    application.setJob(job);
                    application.setEnterprise(job.getEnterprise());
                    application.setStatus(statuses[new Random().nextInt(statuses.length)]);
                    application.setApplyTime(LocalDateTime.now().minusDays(new Random().nextInt(30)));
                    applicationRepository.save(application);
                }
            }
        }
    }

    private Map<String, Object> getAccountInfo() {
        Map<String, Object> accounts = new HashMap<>();
        
        Map<String, String> admin = new HashMap<>();
        admin.put("username", "admin");
        admin.put("password", "admin123");
        admin.put("role", "系统管理员");
        admin.put("login_url", "http://localhost:3002/admin/login");
        accounts.put("管理员账号", admin);

        Map<String, String> seeker = new HashMap<>();
        seeker.put("username", "seeker");
        seeker.put("password", "seeker123");
        seeker.put("role", "求职者");
        seeker.put("login_url", "http://localhost:3000/login");
        accounts.put("求职者账号", seeker);

        Map<String, String> company = new HashMap<>();
        company.put("username", "company");
        company.put("password", "company123");
        company.put("role", "企业HR");
        company.put("login_url", "http://localhost:3001/company/login");
        accounts.put("企业HR账号", company);

        return accounts;
    }
}
