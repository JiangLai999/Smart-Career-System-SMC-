package com.example.smartcareer.service;

import com.example.smartcareer.dto.request.JobFilterRequest;
import com.example.smartcareer.dto.request.JobPublishRequest;
import com.example.smartcareer.dto.response.JobResponse;
import com.example.smartcareer.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobService {
    
    Job publishJob(JobPublishRequest request, Long enterpriseId, Long hrId);
    
    Job updateJob(Long id, Job job);
    
    void deleteJob(Long id);
    
    Job getJobById(Long id);
    
    Page<Job> getJobs(Pageable pageable);
    
    Page<Job> searchJobs(String keyword, Pageable pageable);
    
    Page<Job> filterJobs(JobFilterRequest request, Pageable pageable);
    
    List<Job> getJobsByEnterprise(Long enterpriseId);
    
    List<Job> getJobsByHr(Long hrId);
    
    Page<Job> getJobsByCategory(String category, Pageable pageable);
    
    Page<Job> getJobsByLocation(String location, Pageable pageable);
    
    void incrementViewCount(Long jobId);
    
    void incrementApplyCount(Long jobId);
    
    JobResponse convertToResponse(Job job);
}
