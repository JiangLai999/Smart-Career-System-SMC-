package com.example.smartcareer.controller;

import com.example.smartcareer.dto.request.CompanyJobRequest;
import com.example.smartcareer.dto.request.CompanyLoginRequest;
import com.example.smartcareer.dto.request.CompanyRegisterRequest;
import com.example.smartcareer.dto.request.ResumeFilterRequest;
import com.example.smartcareer.dto.response.*;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.entity.EnterpriseHR;
import com.example.smartcareer.repository.EnterpriseHRRepository;
import com.example.smartcareer.service.CompanyService;
import com.example.smartcareer.service.VerificationCodeService;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EnterpriseHRRepository enterpriseHRRepository;
    
    // ==================== 企业注册登录 ====================
    
    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> register(@Valid @RequestBody CompanyRegisterRequest request) {
        Map<String, Object> result = companyService.register(request);
        return ApiResponse.success("注册申请已提交", result);
    }
    
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody CompanyLoginRequest request) {
        Map<String, Object> result = companyService.login(request.getUsername(), request.getPassword());
        return ApiResponse.success("登录成功", result);
    }
    
    @GetMapping("/info")
    public ApiResponse<CompanyResponse> getCompanyInfo(@RequestHeader("Authorization") String token) {
        Long enterpriseId = extractEnterpriseId(token);
        CompanyResponse company = companyService.getCompanyInfo(enterpriseId);
        return ApiResponse.success(company);
    }
    
    @PutMapping("/info")
    public ApiResponse<CompanyResponse> updateCompanyInfo(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> request) {
        Long enterpriseId = extractEnterpriseId(token);
        CompanyResponse company = companyService.updateCompanyInfo(enterpriseId, request);
        return ApiResponse.success("企业信息更新成功", company);
    }
    
    @GetMapping("/register-status")
    public ApiResponse<Map<String, Object>> getRegisterStatus(@RequestHeader("Authorization") String token) {
        Long enterpriseId = extractEnterpriseId(token);
        Map<String, Object> status = companyService.getRegisterStatus(enterpriseId);
        return ApiResponse.success(status);
    }
    
    // ==================== 职位管理 ====================
    
    @GetMapping("/jobs")
    public ApiResponse<Page<CompanyJobResponse>> getCompanyJobs(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        
        Long enterpriseId = extractEnterpriseId(token);
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageSize", pageSize);
        params.put("status", status);
        params.put("keyword", keyword);
        
        Page<CompanyJobResponse> jobs = companyService.getCompanyJobs(enterpriseId, params);
        return ApiResponse.success(jobs);
    }
    
    @GetMapping("/job/{id}")
    public ApiResponse<CompanyJobResponse> getCompanyJob(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        Long enterpriseId = extractEnterpriseId(token);
        CompanyJobResponse job = companyService.getCompanyJob(enterpriseId, id);
        return ApiResponse.success(job);
    }
    
    @PostMapping("/job")
    public ApiResponse<CompanyJobResponse> createJob(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CompanyJobRequest request) {
        
        Long enterpriseId = extractEnterpriseId(token);
        Long hrId = extractUserId(token);
        CompanyJobResponse job = companyService.createJob(enterpriseId, hrId, request);
        return ApiResponse.success("职位发布成功", job);
    }
    
    @PutMapping("/job/{id}")
    public ApiResponse<CompanyJobResponse> updateJob(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @Valid @RequestBody CompanyJobRequest request) {
        
        Long enterpriseId = extractEnterpriseId(token);
        CompanyJobResponse job = companyService.updateJob(enterpriseId, id, request);
        return ApiResponse.success("职位更新成功", job);
    }
    
    @DeleteMapping("/job/{id}")
    public ApiResponse<Void> deleteJob(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        Long enterpriseId = extractEnterpriseId(token);
        companyService.deleteJob(enterpriseId, id);
        return ApiResponse.success("职位删除成功", null);
    }
    
    @PutMapping("/job/{id}/status")
    public ApiResponse<Void> toggleJobStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam String status) {
        
        Long enterpriseId = extractEnterpriseId(token);
        companyService.toggleJobStatus(enterpriseId, id, status);
        return ApiResponse.success("状态更新成功", null);
    }
    
    @PutMapping("/job/{id}/extend")
    public ApiResponse<Void> extendJobValidity(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam Integer days) {
        
        Long enterpriseId = extractEnterpriseId(token);
        companyService.extendJobValidity(enterpriseId, id, days);
        return ApiResponse.success("有效期已延长", null);
    }
    
    // ==================== 简历管理 ====================
    
    @GetMapping("/applications")
    public ApiResponse<Page<ResumeListResponse>> getCompanyApplications(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) String status) {
        
        Long enterpriseId = extractEnterpriseId(token);
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageSize", pageSize);
        if (jobId != null) {
            params.put("jobId", jobId);
        }
        if (status != null && !status.isEmpty()) {
            params.put("status", status);
        }
        
        Page<ResumeListResponse> applications = companyService.getCompanyApplications(enterpriseId, params);
        return ApiResponse.success(applications);
    }
    
    @GetMapping("/job/{jobId}/resumes")
    public ApiResponse<Page<ResumeListResponse>> getResumesByJob(
            @RequestHeader("Authorization") String token,
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        
        Long enterpriseId = extractEnterpriseId(token);
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("pageSize", pageSize);
        
        Page<ResumeListResponse> resumes = companyService.getResumesByJob(enterpriseId, jobId, params);
        return ApiResponse.success(resumes);
    }
    
    @GetMapping("/resumes/filter")
    public ApiResponse<Page<ResumeListResponse>> filterResumes(
            @RequestHeader("Authorization") String token,
            ResumeFilterRequest request) {
        
        Long enterpriseId = extractEnterpriseId(token);
        Page<ResumeListResponse> resumes = companyService.filterResumes(enterpriseId, request);
        return ApiResponse.success(resumes);
    }
    
    @GetMapping("/resume/{id}")
    public ApiResponse<ResumeDetailResponse> getResumeById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        Long enterpriseId = extractEnterpriseId(token);
        ResumeDetailResponse resume = companyService.getResumeById(enterpriseId, id);
        return ApiResponse.success(resume);
    }
    
    @PutMapping("/application/{id}/status")
    public ApiResponse<Void> updateApplicationStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam String status) {
        
        Long enterpriseId = extractEnterpriseId(token);
        companyService.updateApplicationStatus(enterpriseId, id, status);
        return ApiResponse.success("状态更新成功", null);
    }
    
    @PutMapping("/resume/{id}/mark")
    public ApiResponse<Void> markResume(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Map<String, Object> tags) {
        
        Long enterpriseId = extractEnterpriseId(token);
        String tag = tags.get("tag") != null ? tags.get("tag").toString() : "FAVORITE";
        companyService.markResume(enterpriseId, id, tag);
        return ApiResponse.success("标记成功", null);
    }
    
    @PostMapping("/filter/save")
    public ApiResponse<Void> saveFilterCondition(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> filterData) {
        
        Long enterpriseId = extractEnterpriseId(token);
        companyService.saveFilterCondition(enterpriseId, filterData);
        return ApiResponse.success("保存成功", null);
    }
    
    @GetMapping("/filter/list")
    public ApiResponse<Page<Map<String, Object>>> getSavedFilters(
            @RequestHeader("Authorization") String token) {
        
        Long enterpriseId = extractEnterpriseId(token);
        Page<Map<String, Object>> filters = companyService.getSavedFilters(enterpriseId);
        return ApiResponse.success(filters);
    }
    
    // ==================== 统计数据 ====================
    
    @GetMapping("/stats")
    public ApiResponse<CompanyStatsResponse> getCompanyStats(@RequestHeader("Authorization") String token) {
        Long enterpriseId = extractEnterpriseId(token);
        CompanyStatsResponse stats = companyService.getCompanyStats(enterpriseId);
        return ApiResponse.success(stats);
    }
    
    // ==================== 忘记密码 ====================
    
    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody Map<String, String> request) {
        String account = request.get("account");
        String code = request.get("code");
        String newPassword = request.get("newPassword");
        
        if (account == null || account.isEmpty()) {
            return ApiResponse.error("账号不能为空");
        }
        
        if (newPassword == null || newPassword.length() < 6) {
            return ApiResponse.error("密码长度至少6位");
        }
        
        if (code != null && !code.isEmpty()) {
            boolean verified = verificationCodeService.verifyCode(account, code, "RESET_PASSWORD");
            if (!verified) {
                return ApiResponse.error("验证码错误或已过期");
            }
        }
        
        companyService.resetPassword(account, newPassword);
        
        return ApiResponse.success("密码重置成功");
    }
    
    // ==================== 工具方法 ====================
    
    private Long extractUserId(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "未授权");
        }
        String jwtToken = token.substring(7);
        return jwtUtil.extractUserId(jwtToken);
    }
    
private Long extractEnterpriseId(String token) {
    Long userId = extractUserId(token);
    Optional<EnterpriseHR> hrOpt = enterpriseHRRepository.findById(userId);
    if (hrOpt.isPresent() && hrOpt.get().getEnterprise() != null) {
        return hrOpt.get().getEnterprise().getId();
    }
    throw new BusinessException(ErrorCode.FORBIDDEN, "无法获取企业信息，请检查账号权限");
}
}
