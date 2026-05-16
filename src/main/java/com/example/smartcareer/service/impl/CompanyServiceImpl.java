package com.example.smartcareer.service.impl;

import com.example.smartcareer.dto.request.CompanyJobRequest;
import com.example.smartcareer.dto.request.CompanyRegisterRequest;
import com.example.smartcareer.dto.request.ResumeFilterRequest;
import com.example.smartcareer.dto.response.*;
import com.example.smartcareer.entity.*;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.repository.*;
import com.example.smartcareer.entity.ResumeFilter;
import com.example.smartcareer.service.CompanyService;
import com.example.smartcareer.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private EnterpriseHRRepository enterpriseHRRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResumeFilterRepository resumeFilterRepository;
    
    @Autowired
    private EnterpriseAuditRepository enterpriseAuditRepository;
    
    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Map<String, Object> register(CompanyRegisterRequest request) {
        if (enterpriseHRRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME, "用户名已存在");
        }

        if (enterpriseRepository.existsByLicenseNumber(request.getBusinessLicense())) {
            throw new BusinessException(ErrorCode.DUPLICATE_LICENSE, "营业执照号已存在");
        }

        Enterprise enterprise = new Enterprise();
        enterprise.setEnterpriseName(request.getCompanyName());
        enterprise.setLicenseNumber(request.getBusinessLicense());
        enterprise.setIndustry(request.getIndustry());
        enterprise.setScale(request.getScale());
        enterprise.setDescription(request.getDescription());
        enterprise.setContactPerson(request.getContactName());
        enterprise.setContactPhone(request.getContactPhone());
        enterprise.setContactEmail(request.getContactEmail());
        enterprise.setAuditStatus(0);
        enterprise = enterpriseRepository.save(enterprise);

        EnterpriseHR hr = new EnterpriseHR();
        hr.setUsername(request.getUsername());
        hr.setPassword(passwordEncoder.encode(request.getPassword()));
        hr.setRealName(request.getContactName());
        hr.setPosition(request.getContactPosition());
        hr.setPhone(request.getContactPhone());
        hr.setEmail(request.getContactEmail());
        hr.setEnterprise(enterprise);
        hr.setAuditStatus(0);
        hr.setStatus(1);
        enterpriseHRRepository.save(hr);

        // 创建审核记录
        EnterpriseAudit audit = new EnterpriseAudit();
        audit.setEnterprise(enterprise);
        audit.setAuditStatus("PENDING");
        enterpriseAuditRepository.save(audit);

        Map<String, Object> result = new HashMap<>();
        result.put("enterpriseId", enterprise.getId());
        result.put("username", request.getUsername());
        result.put("status", "PENDING");
        return result;
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        EnterpriseHR hr = enterpriseHRRepository.findByUsername(username)
                .or(() -> enterpriseHRRepository.findByPhone(username))
                .or(() -> enterpriseHRRepository.findByEmail(username))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在"));

        if (hr.getStatus() != null && hr.getStatus() == 0) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED, "账号已被禁用");
        }

        // 检查企业审核状态
        if (hr.getEnterprise() != null) {
            Integer auditStatus = hr.getEnterprise().getAuditStatus();
            if (auditStatus == null || auditStatus == 0) {
                throw new BusinessException(ErrorCode.ACCOUNT_PENDING, "企业账号正在审核中，请等待审核通过后再登录");
            }
            if (auditStatus == 2) {
                throw new BusinessException(ErrorCode.ACCOUNT_REJECTED, "企业审核未通过，请修改资料后重新提交");
            }
        }

        if (!passwordEncoder.matches(password, hr.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD, "密码错误");
        }

        String token = jwtUtil.generateToken(hr.getId(), hr.getUsername(), "ENTERPRISE_HR");

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", hr.getId());
        result.put("username", hr.getUsername());
        result.put("userType", "ENTERPRISE_HR");

        if (hr.getEnterprise() != null) {
            result.put("enterpriseId", hr.getEnterprise().getId());
            result.put("enterpriseName", hr.getEnterprise().getEnterpriseName());
            result.put("auditStatus", hr.getEnterprise().getAuditStatus());
        }

        return result;
    }

@Override
@Transactional(readOnly = true)
public CompanyResponse getCompanyInfo(Long enterpriseId) {
    Enterprise enterprise = getEnterpriseById(enterpriseId);
    return convertToCompanyResponse(enterprise);
}

    @Override
    @Transactional
    public CompanyResponse updateCompanyInfo(Long enterpriseId, Map<String, Object> request) {
        Enterprise enterprise = getEnterpriseById(enterpriseId);

        if (request.containsKey("companyName")) {
            enterprise.setEnterpriseName((String) request.get("companyName"));
        }
        if (request.containsKey("industry")) {
            enterprise.setIndustry((String) request.get("industry"));
        }
        if (request.containsKey("scale")) {
            enterprise.setScale((String) request.get("scale"));
        }
        if (request.containsKey("description")) {
            enterprise.setDescription((String) request.get("description"));
        }
        if (request.containsKey("contactPerson")) {
            enterprise.setContactPerson((String) request.get("contactPerson"));
        }
        if (request.containsKey("contactPhone")) {
            enterprise.setContactPhone((String) request.get("contactPhone"));
        }
        if (request.containsKey("contactEmail")) {
            enterprise.setContactEmail((String) request.get("contactEmail"));
        }
        if (request.containsKey("address")) {
            enterprise.setAddress((String) request.get("address"));
        }
        if (request.containsKey("website")) {
            enterprise.setWebsite((String) request.get("website"));
        }

        enterprise = enterpriseRepository.save(enterprise);
        return convertToCompanyResponse(enterprise);
    }

@Override
@Transactional(readOnly = true)
public Map<String, Object> getRegisterStatus(Long enterpriseId) {
        Enterprise enterprise = getEnterpriseById(enterpriseId);
        Map<String, Object> result = new HashMap<>();
        result.put("auditStatus", enterprise.getAuditStatus());
        result.put("auditComment", enterprise.getAuditComment());
        result.put("auditTime", enterprise.getAuditTime());
        return result;
    }

@Override
@Transactional(readOnly = true)
public Page<CompanyJobResponse> getCompanyJobs(Long enterpriseId, Map<String, Object> params) {
        Enterprise enterprise = getEnterpriseById(enterpriseId);

        int page = params.containsKey("page") ? (Integer) params.get("page") : 0;
        int pageSize = params.containsKey("pageSize") ? (Integer) params.get("pageSize") : 10;
        String status = (String) params.get("status");
        String keyword = (String) params.get("keyword");

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Job> jobs;
        if (status != null && !status.isEmpty()) {
            jobs = jobRepository.findByEnterpriseAndStatus(enterprise, status, pageable);
        } else {
            jobs = jobRepository.findByEnterprise(enterprise, pageable);
        }

        List<CompanyJobResponse> responses = new ArrayList<>();
        for (Job job : jobs.getContent()) {
            // 过滤掉已删除的职位
            if ("DELETED".equals(job.getStatus())) {
                continue;
            }
            if (keyword != null && !keyword.isEmpty()) {
                if (!job.getTitle().contains(keyword)) {
                    continue;
                }
            }
            responses.add(convertToCompanyJobResponse(job));
        }

        return new PageImpl<>(responses, pageable, jobs.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyJobResponse getCompanyJob(Long enterpriseId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));

        if (!job.getEnterprise().getId().equals(enterpriseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限查看此职位");
        }

        return convertToCompanyJobResponse(job);
    }

    @Override
    @Transactional
    public CompanyJobResponse createJob(Long enterpriseId, Long hrId, CompanyJobRequest request) {
        Enterprise enterprise = getEnterpriseById(enterpriseId);

        Job job = new Job();
        job.setEnterprise(enterprise);
        job.setTitle(request.getTitle());
        job.setCategory(request.getCategory());
        job.setLocation(request.getLocation());
        job.setEducation(request.getEducation());
        job.setExperience(request.getExperience());
        job.setRecruitNumber(request.getRecruitCount());
        job.setDescription(request.getDescription());
        job.setRequirement(request.getRequirements());
        job.setWorkType(request.getType());
        job.setStatus(request.getStatus() != null ? request.getStatus() : "PUBLISHED");
        job.setPublishTime(LocalDateTime.now());

        if (request.getValidityDays() != null && request.getValidityDays() > 0) {
            job.setExpiryTime(LocalDateTime.now().plusDays(request.getValidityDays()));
        }

        if (request.getSalaryRange() != null) {
            parseSalaryRange(job, request.getSalaryRange());
        }

        if (hrId != null) {
            enterpriseHRRepository.findById(hrId).ifPresent(job::setHr);
        }

        job = jobRepository.save(job);
        return convertToCompanyJobResponse(job);
    }

    @Override
    @Transactional
    public CompanyJobResponse updateJob(Long enterpriseId, Long jobId, CompanyJobRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));

        if (!job.getEnterprise().getId().equals(enterpriseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限操作此职位");
        }

        job.setTitle(request.getTitle());
        job.setCategory(request.getCategory());
        job.setLocation(request.getLocation());
        job.setEducation(request.getEducation());
        job.setExperience(request.getExperience());
        job.setRecruitNumber(request.getRecruitCount());
        job.setDescription(request.getDescription());
        job.setRequirement(request.getRequirements());
        job.setWorkType(request.getType());

        if (request.getSalaryRange() != null) {
            parseSalaryRange(job, request.getSalaryRange());
        }

        job = jobRepository.save(job);
        return convertToCompanyJobResponse(job);
    }

    @Override
    @Transactional
    public void deleteJob(Long enterpriseId, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));

        if (!job.getEnterprise().getId().equals(enterpriseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限操作此职位");
        }

        job.setStatus("DELETED");
        jobRepository.save(job);
    }

    @Override
    @Transactional
    public void toggleJobStatus(Long enterpriseId, Long jobId, String status) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));

        if (!job.getEnterprise().getId().equals(enterpriseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限操作此职位");
        }

        job.setStatus(status);
        jobRepository.save(job);
    }

    @Override
    @Transactional
    public void extendJobValidity(Long enterpriseId, Long jobId, Integer days) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));

        if (!job.getEnterprise().getId().equals(enterpriseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限操作此职位");
        }

        LocalDateTime expiryTime = job.getExpiryTime();
        if (expiryTime == null) {
            expiryTime = LocalDateTime.now();
        }
        job.setExpiryTime(expiryTime.plusDays(days));
        jobRepository.save(job);
    }

@Override
@Transactional(readOnly = true)
public Page<ResumeListResponse> getCompanyApplications(Long enterpriseId, Map<String, Object> params) {
        Enterprise enterprise = getEnterpriseById(enterpriseId);

        int page = params.containsKey("page") ? ((Number) params.get("page")).intValue() : 0;
        int pageSize = params.containsKey("pageSize") ? ((Number) params.get("pageSize")).intValue() : 10;
        Long jobId = params.containsKey("jobId") && params.get("jobId") != null 
                ? Long.valueOf(params.get("jobId").toString()) : null;
        String status = params.containsKey("status") && params.get("status") != null 
                ? (String) params.get("status") : null;

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Application> applications;
        if (status != null && !status.isEmpty()) {
            applications = applicationRepository.findByEnterpriseAndStatus(enterpriseId, status, pageable);
        } else {
            applications = applicationRepository.findByEnterprise(enterprise, pageable);
        }

        List<ResumeListResponse> responses = new ArrayList<>();
        for (Application app : applications.getContent()) {
            if (jobId != null && !app.getJob().getId().equals(jobId)) {
                continue;
            }
            responses.add(convertToResumeListResponse(app));
        }

        return new PageImpl<>(responses, pageable, applications.getTotalElements());
    }
    
@Override
@Transactional(readOnly = true)
public Page<ResumeListResponse> getResumesByJob(Long enterpriseId, Long jobId, Map<String, Object> params) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND, "职位不存在"));

        if (!job.getEnterprise().getId().equals(enterpriseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限查看此职位的简历");
        }

        int page = params.containsKey("page") ? (Integer) params.get("page") : 0;
        int pageSize = params.containsKey("pageSize") ? (Integer) params.get("pageSize") : 10;

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Application> applications = applicationRepository.findByJob(job, pageable);

        List<ResumeListResponse> responses = new ArrayList<>();
        for (Application app : applications.getContent()) {
            responses.add(convertToResumeListResponse(app));
        }

        return new PageImpl<>(responses, pageable, applications.getTotalElements());
    }

@Override
@Transactional(readOnly = true)
public Page<ResumeListResponse> filterResumes(Long enterpriseId, ResumeFilterRequest request) {
        getEnterpriseById(enterpriseId);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Application> query = cb.createQuery(Application.class);
        Root<Application> root = query.from(Application.class);

        Join<Application, JobSeeker> jobSeekerJoin = root.join("jobSeeker");
        Join<Application, Job> jobJoin = root.join("job");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("enterprise").get("id"), enterpriseId));

        if (request.getJobId() != null) {
            predicates.add(cb.equal(jobJoin.get("id"), request.getJobId()));
        }
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            predicates.add(cb.equal(root.get("status"), request.getStatus()));
        }
        if (request.getEducation() != null && !request.getEducation().isEmpty()) {
            predicates.add(cb.equal(jobSeekerJoin.get("education"), request.getEducation()));
        }
        if (request.getExperience() != null && !request.getExperience().isEmpty()) {
            Integer expYears = parseExperience(request.getExperience());
            if (expYears != null) {
                predicates.add(cb.equal(jobSeekerJoin.get("workYears"), expYears));
            }
        }
        if (request.getSkills() != null && !request.getSkills().isEmpty()) {
            predicates.add(cb.like(jobSeekerJoin.get("skills"), "%" + request.getSkills() + "%"));
        }
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            Predicate namePredicate = cb.like(jobSeekerJoin.get("realName"), keyword);
            Predicate jobPredicate = cb.like(jobJoin.get("title"), keyword);
            predicates.add(cb.or(namePredicate, jobPredicate));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("applyTime")));

        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 10;

        List<Application> applications = entityManager.createQuery(query)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Application> countRoot = countQuery.from(Application.class);
        Join<Application, JobSeeker> countJobSeekerJoin = countRoot.join("jobSeeker");
        Join<Application, Job> countJobJoin = countRoot.join("job");
        countQuery.select(cb.count(countRoot));
        
        List<Predicate> countPredicates = new ArrayList<>();
        countPredicates.add(cb.equal(countRoot.get("enterprise").get("id"), enterpriseId));
        if (request.getJobId() != null) {
            countPredicates.add(cb.equal(countRoot.get("job").get("id"), request.getJobId()));
        }
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            countPredicates.add(cb.equal(countRoot.get("status"), request.getStatus()));
        }
        if (request.getEducation() != null && !request.getEducation().isEmpty()) {
            countPredicates.add(cb.equal(countJobSeekerJoin.get("education"), request.getEducation()));
        }
        if (request.getExperience() != null && !request.getExperience().isEmpty()) {
            Integer expYears = parseExperience(request.getExperience());
            if (expYears != null) {
                countPredicates.add(cb.equal(countJobSeekerJoin.get("workYears"), expYears));
            }
        }
        if (request.getSkills() != null && !request.getSkills().isEmpty()) {
            countPredicates.add(cb.like(countJobSeekerJoin.get("skills"), "%" + request.getSkills() + "%"));
        }
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            String keyword = "%" + request.getKeyword() + "%";
            Predicate namePredicate = cb.like(countJobSeekerJoin.get("realName"), keyword);
            Predicate jobPredicate = cb.like(countJobJoin.get("title"), keyword);
            countPredicates.add(cb.or(namePredicate, jobPredicate));
        }
        
        countQuery.where(countPredicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        List<ResumeListResponse> responses = new ArrayList<>();
        for (Application app : applications) {
            responses.add(convertToResumeListResponse(app));
        }

        return new PageImpl<>(responses, PageRequest.of(page, pageSize), total);
    }

@Override
@Transactional(readOnly = true)
public ResumeDetailResponse getResumeById(Long enterpriseId, Long resumeId) {
        Application application = applicationRepository.findById(resumeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "简历不存在"));

        if (!application.getEnterprise().getId().equals(enterpriseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限查看此简历");
        }

        return convertToResumeDetailResponse(application);
    }

    @Override
    @Transactional
    public void updateApplicationStatus(Long enterpriseId, Long applicationId, String status) {
        // 使用JOIN FETCH查询以加载关联实体
        Application application = applicationRepository.findByIdWithRelations(applicationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "申请不存在"));

        if (!application.getEnterprise().getId().equals(enterpriseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限操作此申请");
        }

        String oldStatus = application.getStatus();
        application.setStatus(status);
        applicationRepository.save(application);
        
        // 发送通知给求职者
        try {
            Long userId = application.getJobSeeker().getId();
            String jobTitle = application.getJob().getTitle();
            String companyName = application.getEnterprise().getName();
            System.out.println("=== 发送通知 ===");
            System.out.println("userId: " + userId + ", jobTitle: " + jobTitle + ", companyName: " + companyName + ", status: " + status);
            
            if ("OFFERED".equals(status)) {
                notificationService.sendHireNotification(userId, applicationId, jobTitle, companyName);
                System.out.println("录用通知已发送");
            } else if ("REJECTED".equals(status)) {
                notificationService.sendRejectNotification(userId, applicationId, jobTitle, companyName);
                System.out.println("拒绝通知已发送");
            }
        } catch (Exception e) {
            System.out.println("通知发送失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void markResume(Long enterpriseId, Long resumeId, String tag) {
        Application application = applicationRepository.findById(resumeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "简历不存在"));

        if (!application.getEnterprise().getId().equals(enterpriseId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无权限操作此简历");
        }

        application.setTag(tag);
        application.setMarkTime(LocalDateTime.now());
        applicationRepository.save(application);
    }

    @Override
    @Transactional
    public void saveFilterCondition(Long enterpriseId, Map<String, Object> filterData) {
        ResumeFilter filter = new ResumeFilter();
        filter.setEnterpriseId(enterpriseId);
        
        String filterName = filterData.containsKey("name") ? (String) filterData.get("name") : "筛选条件";
        filter.setFilterName(filterName);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            filter.setFilterJson(mapper.writeValueAsString(filterData));
        } catch (Exception e) {
            filter.setFilterJson(filterData.toString());
        }
        
        resumeFilterRepository.save(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> getSavedFilters(Long enterpriseId) {
        Pageable pageable = PageRequest.of(0, 20);
        Page<ResumeFilter> filters = resumeFilterRepository.findByEnterpriseIdOrderByCreateTimeDesc(enterpriseId, pageable);
        
        ObjectMapper mapper = new ObjectMapper();
        Page<Map<String, Object>> result = filters.map(filter -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", filter.getId());
            map.put("name", filter.getFilterName());
            map.put("createTime", filter.getCreateTime());
            
            try {
                if (filter.getFilterJson() != null) {
                    map.put("filter", mapper.readValue(filter.getFilterJson(), Map.class));
                }
            } catch (Exception e) {
                map.put("filter", filter.getFilterJson());
            }
            
            return map;
        });
        
        return result;
    }

@Override
@Transactional(readOnly = true)
public CompanyStatsResponse getCompanyStats(Long enterpriseId) {
        Enterprise enterprise = getEnterpriseById(enterpriseId);

        CompanyStatsResponse stats = new CompanyStatsResponse();

        long activeJobs = jobRepository.countByEnterpriseAndStatus(enterprise, "PUBLISHED");
        stats.setActiveJobs((int) activeJobs);

        long totalApplications = applicationRepository.countByEnterprise(enterprise);
        stats.setTotalApplications((int) totalApplications);

        long pendingCount = applicationRepository.countByEnterpriseAndStatus(enterprise, "APPLIED");
        stats.setPendingCount((int) pendingCount);

        long interviewCount = applicationRepository.countByEnterpriseAndStatus(enterprise, "INTERVIEW");
        stats.setInterviewCount((int) interviewCount);

        long hiredCount = applicationRepository.countByEnterpriseAndStatus(enterprise, "HIRED");
        stats.setHiredCount((int) hiredCount);

        long rejectedCount = applicationRepository.countByEnterpriseAndStatus(enterprise, "REJECTED");
        stats.setRejectedCount((int) rejectedCount);

        return stats;
    }

    @Override
    @Transactional
    public void resetPassword(String account, String newPassword) {
        EnterpriseHR hr = enterpriseHRRepository.findByUsername(account)
                .or(() -> enterpriseHRRepository.findByPhone(account))
                .or(() -> enterpriseHRRepository.findByEmail(account))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "账号不存在"));

        hr.setPassword(passwordEncoder.encode(newPassword));
        enterpriseHRRepository.save(hr);
    }

    @Override
    public Enterprise getEnterpriseById(Long enterpriseId) {
        return enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTERPRISE_NOT_FOUND, "企业不存在"));
    }

    private CompanyResponse convertToCompanyResponse(Enterprise enterprise) {
        CompanyResponse response = new CompanyResponse();
        response.setId(enterprise.getId());
        response.setCompanyName(enterprise.getEnterpriseName());
        response.setBusinessLicense(enterprise.getLicenseNumber());
        response.setIndustry(enterprise.getIndustry());
        response.setScale(enterprise.getScale());
        response.setDescription(enterprise.getDescription());
        response.setContactName(enterprise.getContactPerson());
        response.setContactPhone(enterprise.getContactPhone());
        response.setContactEmail(enterprise.getContactEmail());
        response.setStatus(enterprise.getAuditStatus() != null ? enterprise.getAuditStatus().toString() : "0");
        response.setCreateTime(enterprise.getCreateTime());
        return response;
    }

    private CompanyJobResponse convertToCompanyJobResponse(Job job) {
        CompanyJobResponse response = new CompanyJobResponse();
        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setCategory(job.getCategory());
        response.setRecruitCount(job.getRecruitNumber());
        response.setLocation(job.getLocation());
        response.setType(job.getWorkType());
        response.setEducation(job.getEducation());
        response.setExperience(job.getExperience());
        response.setDescription(job.getDescription());
        response.setRequirements(job.getRequirement());
        response.setStatus(job.getStatus());
        response.setViews(job.getViewCount());
        response.setApplicants(job.getApplyCount());
        response.setCreateTime(job.getCreateTime());
        response.setExpireTime(job.getExpiryTime());

        if (job.getSalaryMin() != null && job.getSalaryMax() != null) {
            response.setSalaryRange(job.getSalaryMin() / 1000 + "K-" + job.getSalaryMax() / 1000 + "K");
        }

        return response;
    }

    private ResumeListResponse convertToResumeListResponse(Application application) {
        ResumeListResponse response = new ResumeListResponse();
        try {
            JobSeeker jobSeeker = application.getJobSeeker();
            if (jobSeeker != null) {
                response.setId(jobSeeker.getId());
                response.setUserName(jobSeeker.getRealName() != null ? jobSeeker.getRealName() : "求职者");
                response.setSchool(jobSeeker.getSchool() != null ? jobSeeker.getSchool() : "");
                response.setEducation(jobSeeker.getEducation() != null ? jobSeeker.getEducation() : "");
                Integer workYears = jobSeeker.getWorkYears();
                if (workYears == null || workYears == 0) {
                    response.setExperience("应届");
                } else {
                    response.setExperience(workYears + "年");
                }
                response.setLocation(jobSeeker.getExpectedLocation() != null ? jobSeeker.getExpectedLocation() : "");

                if (jobSeeker.getSkills() != null && !jobSeeker.getSkills().isEmpty()) {
                    response.setSkills(Arrays.asList(jobSeeker.getSkills().split(",")));
                }
            } else {
                response.setId(0L);
                response.setUserName("未知用户");
            }
            
            response.setApplicationId(application.getId());
            response.setStatus(application.getStatus() != null ? application.getStatus() : "APPLIED");
            response.setApplyTime(application.getApplyTime());

            Job job = application.getJob();
            if (job != null) {
                response.setJobTitle(job.getTitle() != null ? job.getTitle() : "未知职位");
            }

            response.setMatchScore(75);
        } catch (Exception e) {
            response.setId(0L);
            response.setUserName("用户信息加载失败");
            response.setApplicationId(application.getId());
            response.setStatus(application.getStatus() != null ? application.getStatus() : "APPLIED");
            response.setApplyTime(application.getApplyTime());
        }

        return response;
    }

    private ResumeDetailResponse convertToResumeDetailResponse(Application application) {
        ResumeDetailResponse response = new ResumeDetailResponse();
        response.setId(application.getJobSeeker().getId());
        response.setApplicationId(application.getId());
        response.setStatus(application.getStatus() != null ? application.getStatus() : "APPLIED");
        response.setApplyTime(application.getApplyTime());

        Job job = application.getJob();
        if (job != null) {
            response.setJobTitle(job.getTitle() != null ? job.getTitle() : "未知职位");
        }

        JobSeeker jobSeeker = application.getJobSeeker();
        if (jobSeeker != null) {
            response.setUserName(jobSeeker.getRealName());
            response.setEducation(jobSeeker.getEducation());
            response.setExperience(jobSeeker.getWorkYears() != null ? jobSeeker.getWorkYears() + "年" : "");
            response.setLocation(jobSeeker.getExpectedLocation());
            response.setPhone(jobSeeker.getPhone());
            response.setEmail(jobSeeker.getEmail());

            if (jobSeeker.getSkills() != null) {
                response.setSkills(Arrays.asList(jobSeeker.getSkills().split(",")));
            }
            
            if (jobSeeker.getResume() != null) {
                response.setDescription(jobSeeker.getResume());
            }
        }

        return response;
    }

    private void parseSalaryRange(Job job, String salaryRange) {
        try {
            if (salaryRange.contains("-")) {
                String[] parts = salaryRange.replace("K", "").split("-");
                if (parts.length == 2) {
                    job.setSalaryMin(Integer.parseInt(parts[0].trim()) * 1000);
                    String maxPart = parts[1].trim();
                    job.setSalaryMax(Integer.parseInt(maxPart) * 1000);
                }
            }
        } catch (NumberFormatException e) {
        }
    }
    
    private Integer parseExperience(String experience) {
        if (experience == null || experience.isEmpty()) {
            return null;
        }
        try {
            if (experience.contains("1-3")) {
                return 2;
            } else if (experience.contains("3-5")) {
                return 4;
            } else if (experience.contains("5-10")) {
                return 7;
            } else if (experience.contains("10以上") || experience.contains("10年以上")) {
                return 10;
            }
            return Integer.parseInt(experience.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
