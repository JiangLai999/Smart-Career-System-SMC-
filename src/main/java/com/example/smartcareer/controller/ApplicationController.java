package com.example.smartcareer.controller;

import com.example.smartcareer.dto.request.ApplicationRequest;
import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.dto.response.ApplicationResponse;
import com.example.smartcareer.entity.Application;
import com.example.smartcareer.service.ApplicationService;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {
    
    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/apply")
    public ApiResponse<ApplicationResponse> applyForJob(
            @Valid @RequestBody ApplicationRequest request,
            @RequestHeader("Authorization") String token) {
        
        String jwtToken = token.substring(7);
        Long jobSeekerId = jwtUtil.extractUserId(jwtToken);
        
        Application application = applicationService.applyForJob(request, jobSeekerId);
        return ApiResponse.success("简历投递成功", applicationService.convertToResponse(application));
    }
    
    @GetMapping("/{id}")
    public ApiResponse<ApplicationResponse> getApplicationById(@PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        return ApiResponse.success(applicationService.convertToResponse(application));
    }
    
    @GetMapping("/check/{jobId}")
    public ApiResponse<java.util.Map<String, Object>> checkApplicationStatus(
            @PathVariable Long jobId,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.substring(7);
        Long jobSeekerId = jwtUtil.extractUserId(jwtToken);

        java.util.List<com.example.smartcareer.entity.Application> apps =
                applicationService.findByJobSeekerAndJob(jobSeekerId, jobId);
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        if (apps.isEmpty()) {
            result.put("hasApplied", false);
            result.put("status", null);
        } else {
            result.put("hasApplied", true);
            result.put("status", apps.get(0).getStatus());
        }
        return ApiResponse.success(result);
    }
    
    @GetMapping("/my")
    public ApiResponse<Page<ApplicationResponse>> getMyApplications(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        String jwtToken = token.substring(7);
        Long jobSeekerId = jwtUtil.extractUserId(jwtToken);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("applyTime").descending());
        Page<Application> applications = applicationService.getApplicationsByJobSeeker(jobSeekerId, pageable);
        Page<ApplicationResponse> responses = applications.map(applicationService::convertToResponse);
        
        return ApiResponse.success(responses);
    }
    
    @GetMapping("/job/{jobId}")
    public ApiResponse<Page<ApplicationResponse>> getApplicationsByJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("applyTime").descending());
        Page<Application> applications = applicationService.getApplicationsByJob(jobId, pageable);
        Page<ApplicationResponse> responses = applications.map(applicationService::convertToResponse);
        
        return ApiResponse.success(responses);
    }
    
    @PutMapping("/{id}/status")
    public ApiResponse<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        
        Application application = applicationService.updateApplicationStatus(id, status);
        return ApiResponse.success("状态更新成功", applicationService.convertToResponse(application));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ApiResponse.success("申请已撤销", null);
    }
}
