package com.example.smartcareer.service;

import com.example.smartcareer.dto.request.CompanyJobRequest;
import com.example.smartcareer.dto.request.CompanyRegisterRequest;
import com.example.smartcareer.dto.request.ResumeFilterRequest;
import com.example.smartcareer.dto.response.*;
import com.example.smartcareer.entity.Enterprise;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CompanyService {
    
    // 企业注册
    Map<String, Object> register(CompanyRegisterRequest request);
    
    // 企业登录
    Map<String, Object> login(String username, String password);
    
    // 获取企业信息
    CompanyResponse getCompanyInfo(Long enterpriseId);
    
    // 更新企业信息
    CompanyResponse updateCompanyInfo(Long enterpriseId, Map<String, Object> request);
    
    // 获取企业审核状态
    Map<String, Object> getRegisterStatus(Long enterpriseId);
    
    // 职位管理
    Page<CompanyJobResponse> getCompanyJobs(Long enterpriseId, Map<String, Object> params);
    
    CompanyJobResponse getCompanyJob(Long enterpriseId, Long jobId);
    
    CompanyJobResponse createJob(Long enterpriseId, Long hrId, CompanyJobRequest request);
    
    CompanyJobResponse updateJob(Long enterpriseId, Long jobId, CompanyJobRequest request);
    
    void deleteJob(Long enterpriseId, Long jobId);
    
    void toggleJobStatus(Long enterpriseId, Long jobId, String status);
    
    void extendJobValidity(Long enterpriseId, Long jobId, Integer days);
    
    // 简历管理
    Page<ResumeListResponse> getCompanyApplications(Long enterpriseId, Map<String, Object> params);
    
    Page<ResumeListResponse> getResumesByJob(Long enterpriseId, Long jobId, Map<String, Object> params);
    
    Page<ResumeListResponse> filterResumes(Long enterpriseId, ResumeFilterRequest request);
    
    ResumeDetailResponse getResumeById(Long enterpriseId, Long resumeId);
    
    void updateApplicationStatus(Long enterpriseId, Long applicationId, String status);
    
    void markResume(Long enterpriseId, Long resumeId, String tag);
    
    void saveFilterCondition(Long enterpriseId, Map<String, Object> filterData);
    
    Page<Map<String, Object>> getSavedFilters(Long enterpriseId);
    
    // 统计数据
    CompanyStatsResponse getCompanyStats(Long enterpriseId);
    
    // 重置密码
    void resetPassword(String account, String newPassword);
    
    // 工具方法
    Enterprise getEnterpriseById(Long enterpriseId);
}
