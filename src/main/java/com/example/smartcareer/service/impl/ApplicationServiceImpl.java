package com.example.smartcareer.service.impl;

import com.example.smartcareer.dto.request.ApplicationRequest;
import com.example.smartcareer.dto.response.ApplicationResponse;
import com.example.smartcareer.entity.Application;
import com.example.smartcareer.entity.Enterprise;
import com.example.smartcareer.entity.Job;
import com.example.smartcareer.entity.JobSeeker;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.repository.ApplicationRepository;
import com.example.smartcareer.repository.EnterpriseRepository;
import com.example.smartcareer.repository.JobRepository;
import com.example.smartcareer.repository.JobSeekerRepository;
import com.example.smartcareer.service.ApplicationService;
import com.example.smartcareer.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    
    @Autowired
    private JobService jobService;
    
    @Override
    @Transactional
    public Application applyForJob(ApplicationRequest request, Long jobSeekerId) {
        // Use the method that fetches enterprise eagerly
        Job job = jobRepository.findByIdWithEnterprise(request.getJobId())
                .orElseThrow(() -> new BusinessException(ErrorCode.JOB_NOT_FOUND));
        
        if (hasApplied(jobSeekerId, request.getJobId())) {
            throw new BusinessException(ErrorCode.APPLICATION_ALREADY_EXISTS);
        }
        
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        Enterprise enterprise = job.getEnterprise();
        
        Application application = new Application();
        application.setJobSeeker(jobSeeker);
        application.setJob(job);
        application.setEnterprise(enterprise);
        application.setCoverLetter(request.getCoverLetter());
        application.setResumeSnapshot(request.getResumeData());
        application.setStatus("APPLIED");
        
        jobService.incrementApplyCount(job.getId());
        
        return applicationRepository.save(application);
    }
    
    @Override
    @Transactional
    public Application updateApplicationStatus(Long id, String status) {
        Application application = getApplicationById(id);
        application.setStatus(status);
        application.setUpdateTime(LocalDateTime.now());
        
        if ("VIEWED".equals(status)) {
            application.setViewTime(LocalDateTime.now());
        }
        
        return applicationRepository.save(application);
    }
    
    @Override
    @Transactional
    public void deleteApplication(Long id) {
        Application application = getApplicationById(id);
        applicationRepository.delete(application);
    }
    
    @Override
    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.APPLICATION_NOT_FOUND));
    }
    
    @Override
    public Page<Application> getApplicationsByJobSeeker(Long jobSeekerId, Pageable pageable) {
        return applicationRepository.findByJobSeekerId(jobSeekerId, pageable);
    }
    
    @Override
    public Page<Application> getApplicationsByJob(Long jobId, Pageable pageable) {
        return applicationRepository.findByJobId(jobId, pageable);
    }
    
    @Override
    public Page<Application> getApplicationsByEnterprise(Long enterpriseId, Pageable pageable) {
        return applicationRepository.findByEnterpriseId(enterpriseId, pageable);
    }
    
    @Override
    public boolean hasApplied(Long jobSeekerId, Long jobId) {
        return !applicationRepository.findByJobSeekerIdAndJobId(jobSeekerId, jobId).isEmpty();
    }

    @Override
    public java.util.List<Application> findByJobSeekerAndJob(Long jobSeekerId, Long jobId) {
        return applicationRepository.findByJobSeekerIdAndJobId(jobSeekerId, jobId);
    }
    
    @Override
    public ApplicationResponse convertToResponse(Application application) {
        ApplicationResponse response = new ApplicationResponse();
        response.setId(application.getId());
        response.setStatus(application.getStatus());
        response.setApplyTime(application.getApplyTime());
        response.setViewTime(application.getViewTime());
        response.setCoverLetter(application.getCoverLetter());
        response.setHrComment(application.getHrComment());
        response.setRejectReason(application.getRejectReason());
        
        try {
            if (application.getJob() != null) {
                response.setJob(jobService.convertToResponse(application.getJob()));
            }
        } catch (Exception e) {
            // 忽略懒加载错误
        }
        
        try {
            if (application.getEnterprise() != null) {
                response.setEnterpriseName(application.getEnterprise().getEnterpriseName());
            }
        } catch (Exception e) {
            // 忽略懒加载错误
        }
        
        return response;
    }
}
