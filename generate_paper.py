from docx import Document
from docx.shared import Pt, Inches, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.style import WD_STYLE_TYPE
from docx.oxml.ns import qn

doc = Document()

title = doc.add_heading('智能职业规划与就业系统', 0)
title.alignment = WD_ALIGN_PARAGRAPH.CENTER

doc.add_paragraph('论文核心代码示例片段及文字描述')
doc.add_paragraph()

h1 = doc.add_heading('1. 数据模型层（Entity）', level=1)
doc.add_paragraph('代码示例：')

p = doc.add_paragraph()
run = p.add_run('''// 用户实体类 - 采用JPA注解定义数据库映射
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", length = 20)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(length = 20)
    private String phone;
    
    @Column(length = 100)
    private String email;
    
    private String userType;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer status = 1;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }
}''')
run.font.name = 'Consolas'
run.font.size = Pt(9)

doc.add_paragraph()
doc.add_paragraph('文字描述：系统采用JPA ORM框架进行数据持久化，通过@Entity注解定义实体类与数据库表的映射关系。使用@Inheritance注解实现用户类型的单表继承策略，通过@DiscriminatorColumn区分求职者、企业和管理员三种角色。@PrePersist注解用于自动记录创建时间和更新时间。')

doc.add_heading('2. 控制层（Controller）', level=1)
doc.add_paragraph('代码示例：')

p = doc.add_paragraph()
run = p.add_run('''// 职位管理RESTful API控制器
@RestController
@RequestMapping("/api/job")
public class JobController {
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/publish")
    public ApiResponse<JobResponse> publishJob(
            @Valid @RequestBody JobPublishRequest request,
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Long enterpriseId) {
        
        String jwtToken = token.substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);
        
        if (enterpriseId == null) {
            enterpriseId = userId;
        }
        
        Job job = jobService.publishJob(request, enterpriseId, userId);
        return ApiResponse.success("职位发布成功", jobService.convertToResponse(job));
    }
    
    @GetMapping("/filter")
    public ApiResponse<Page<JobResponse>> filterJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer salaryMin,
            @RequestParam(required = false) Integer salaryMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        JobFilterRequest request = new JobFilterRequest();
        request.setKeyword(keyword);
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Job> jobs = jobService.filterJobs(request, pageable);
        
        return ApiResponse.success(jobs.map(jobService::convertToResponse));
    }
}''')
run.font.name = 'Consolas'
run.font.size = Pt(9)

doc.add_paragraph()
doc.add_paragraph('文字描述：系统采用Spring MVC框架构建RESTful风格API接口，通过@RestController注解定义控制器。接口设计遵循REST规范，使用@PostMapping、@GetMapping、@PutMapping、@DeleteMapping注解对应CRUD操作。职位筛选功能支持多条件组合查询，包括关键词、职位类别、工作地点、薪资范围等，并通过Spring Data的Pageable实现分页查询。')

doc.add_heading('3. 安全认证层（JWT）', level=1)
doc.add_paragraph('代码示例：')

p = doc.add_paragraph()
run = p.add_run('''// JWT令牌生成与验证工具类
@Component
public class JwtUtil {

    private static final String SECRET_STRING = "smart-career-system-jwt-secret-key-2024-very-long-secure-key";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    @Value("${jwt.expiration:86400000}")
    private Long expiration;
    
    // 生成令牌
    public String generateToken(Long userId, String username, String userType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("userType", userType);
        return createToken(claims, username);
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    // 解析用户ID
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }
    
    // 验证令牌有效性
    public Boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}''')
run.font.name = 'Consolas'
run.font.size = Pt(9)

doc.add_paragraph()
doc.add_paragraph('文字描述：系统采用JSON Web Token（JWT）实现无状态身份认证机制。JwtUtil工具类基于jjwt库实现令牌的生成、解析和验证功能。生成的令牌包含用户ID、用户名和用户类型等信息，通过HS256算法进行签名加密。令牌默认有效期为24小时，服务端通过拦截器验证令牌有效性来实现接口访问控制。')

doc.add_heading('4. 业务逻辑层（Service）', level=1)
doc.add_paragraph('代码示例：')

p = doc.add_paragraph()
run = p.add_run('''// 职位发布业务逻辑实现
@Service
public class JobServiceImpl implements JobService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    
    @Override
    @Transactional
    public Job publishJob(JobPublishRequest request, Long enterpriseId, Long hrId) {
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTERPRISE_NOT_FOUND));
        
        Job job = new Job();
        job.setEnterprise(enterprise);
        job.setTitle(request.getTitle());
        job.setCategory(request.getCategory());
        job.setLocation(request.getLocation());
        job.setEducation(request.getEducation());
        job.setExperience(request.getExperience());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setRecruitNumber(request.getRecruitNumber());
        job.setDescription(request.getDescription());
        job.setRequirement(request.getRequirement());
        job.setWelfare(request.getWelfare());
        job.setWorkType(request.getWorkType());
        job.setStatus("PUBLISHED");
        
        return jobRepository.save(job);
    }
}''')
run.font.name = 'Consolas'
run.font.size = Pt(9)

doc.add_paragraph()
doc.add_paragraph('文字描述：业务逻辑层采用Spring Service组件实现，@Service注解标记服务类。@Transactional注解保证业务操作的原子性，确保职位发布过程中数据一致性。业务逻辑与数据访问层解耦，通过EntityManager实现Criteria API动态查询，使用JobRepository继承JpaRepository获取标准CRUD能力。异常处理通过自定义BusinessException封装业务错误。')

doc.add_heading('5. 前端API封装（Axios）', level=1)
doc.add_paragraph('代码示例：')

p = doc.add_paragraph()
run = p.add_run('''// Axios请求拦截器配置
const api = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: { 'Content-Type': 'application/json' }
});

// 请求拦截 - 自动注入Token
api.interceptors.request.use(config => {
  const path = window.location.pathname;
  let token = path.startsWith('/admin') ? localStorage.getItem('adminToken')
              : path.startsWith('/company') ? localStorage.getItem('companyToken')
              : localStorage.getItem('token');
  
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 响应拦截 - 统一错误处理
api.interceptors.response.use(
  response => response.data.code === 200 ? response.data : Promise.reject(response.data),
  error => {
    if (error.response?.status === 401) {
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// API方法封装
export default {
  getJobList(params) { return api.get('/job/list', { params }) },
  publishJob(data) { return api.post('/job/publish', data) },
  filterJobs(params) { return api.post('/job/filter', params) },
  applyForJob(data) { return api.post('/application/apply', data) }
};''')
run.font.name = 'Consolas'
run.font.size = Pt(9)

doc.add_paragraph()
doc.add_paragraph('文字描述：前端采用Axios作为HTTP客户端，封装统一的请求和响应拦截器。请求拦截器根据当前路由路径自动选择对应端的认证令牌（求职者/企业/管理员），并通过Authorization请求头携带Bearer Token。响应拦截器统一处理接口返回数据，识别业务状态码，并对401未授权错误自动跳转登录页面。API模块按功能模块化封装，便于各组件调用。')

doc.add_heading('6. 系统架构描述', level=1)

p = doc.add_paragraph()
p.add_run('''┌─────────────────────────────────────────────────────────┐
│                     前端 (Vue 3 + Vite)                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │ 求职者端  │  │  企业端   │  │  管理端   │             │
│  │  :3000   │  │  :3001   │  │  :3002   │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
└───────┼─────────────┼─────────────┼────────────────────┘
        │             │             │
        └─────────────┴─────────────┘
                        │ HTTP + JWT
        ┌───────────────┴───────────────┐
        │     后端 (Spring Boot 2.7)    │
        │     ┌─────────────────────┐   │
        │     │   Controller Layer  │   │
        │     │   Service Layer     │   │
        │     │   Repository Layer │   │
        │     └─────────────────────┘   │
        └───────────────┬───────────────┘
                        │
        ┌───────────────┴───────────────┐
        │        MySQL Database         │
        └───────────────────────────────┘''')

doc.add_paragraph()
doc.add_paragraph('文字描述：系统采用前后端分离架构设计，后端基于Spring Boot 2.7框架构建，提供RESTful API接口。前端使用Vue 3配合Vite构建工具，通过三个独立入口页面分别服务于求职者、企业HR和系统管理员三个角色。数据交互采用JSON格式，通过JWT令牌实现无状态身份认证。数据库使用MySQL 8.0，通过Spring Data JPA实现对象关系映射。')

doc.save('C:\\projects\\smart-career-system\\论文核心代码示例.docx')
print('Word文档已生成: 论文核心代码示例.docx')