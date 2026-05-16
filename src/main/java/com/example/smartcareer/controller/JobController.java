package com.example.smartcareer.controller;

import com.example.smartcareer.dto.request.JobFilterRequest;
import com.example.smartcareer.dto.request.JobPublishRequest;
import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.dto.response.JobResponse;
import com.example.smartcareer.entity.Job;
import com.example.smartcareer.service.JobService;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    
    @GetMapping("/{id}")
    public ApiResponse<JobResponse> getJobById(@PathVariable Long id) {
        jobService.incrementViewCount(id);
        Job job = jobService.getJobById(id);
        return ApiResponse.success(jobService.convertToResponse(job));
    }
    
    @GetMapping("/list")
    public ApiResponse<Page<JobResponse>> getJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "publishTime") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        
        Sort sort = order.equalsIgnoreCase("asc") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Job> jobs = jobService.getJobs(pageable);
        Page<JobResponse> responses = jobs.map(jobService::convertToResponse);
        
        return ApiResponse.success(responses);
    }
    
    @GetMapping("/search")
    public ApiResponse<Page<JobResponse>> searchJobs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishTime").descending());
        Page<Job> jobs = jobService.searchJobs(keyword, pageable);
        Page<JobResponse> responses = jobs.map(jobService::convertToResponse);
        
        return ApiResponse.success(responses);
    }
    
    @GetMapping("/filter")
    public ApiResponse<Page<JobResponse>> filterJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) Integer salaryMin,
            @RequestParam(required = false) Integer salaryMax,
            @RequestParam(defaultValue = "publishTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        JobFilterRequest request = new JobFilterRequest();
        request.setKeyword(keyword);
        request.setCategory(category);
        request.setLocation(location);
        request.setEducation(education);
        request.setExperience(experience);
        request.setJobType(jobType);
        request.setSalaryMin(salaryMin);
        request.setSalaryMax(salaryMax);
        request.setSortBy(sortBy);
        request.setSortOrder(sortOrder);
        
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Job> jobs = jobService.filterJobs(request, pageable);
        Page<JobResponse> responses = jobs.map(jobService::convertToResponse);
        
        return ApiResponse.success(responses);
    }
    
    @PostMapping("/filter")
    public ApiResponse<Page<JobResponse>> filterJobsPost(
            @RequestBody JobFilterRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        String sortBy = request.getSortBy() != null ? request.getSortBy() : "publishTime";
        String sortOrder = request.getSortOrder() != null ? request.getSortOrder() : "desc";
        
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Job> jobs = jobService.filterJobs(request, pageable);
        Page<JobResponse> responses = jobs.map(jobService::convertToResponse);
        
        return ApiResponse.success(responses);
    }
    
    @GetMapping("/enterprise/{enterpriseId}")
    public ApiResponse<List<JobResponse>> getJobsByEnterprise(@PathVariable Long enterpriseId) {
        List<Job> jobs = jobService.getJobsByEnterprise(enterpriseId);
        List<JobResponse> responses = jobs.stream()
                .map(jobService::convertToResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(responses);
    }
    
    @PutMapping("/{id}")
    public ApiResponse<JobResponse> updateJob(
            @PathVariable Long id,
            @RequestBody Job job) {
        Job updatedJob = jobService.updateJob(id, job);
        return ApiResponse.success("职位更新成功", jobService.convertToResponse(updatedJob));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ApiResponse.success("职位删除成功", null);
    }
}
