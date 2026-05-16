package com.example.smartcareer.controller;

import com.example.smartcareer.dto.request.JobSeekerUpdateRequest;
import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.dto.response.JobSeekerResponse;
import com.example.smartcareer.service.JobSeekerService;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/jobseeker")
public class JobSeekerController {
    
    @Autowired
    private JobSeekerService jobSeekerService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/profile")
    public ApiResponse<JobSeekerResponse> getProfile(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        JobSeekerResponse response = jobSeekerService.getJobSeekerProfile(userId);
        return ApiResponse.success(response);
    }
    
    @PutMapping("/profile")
    public ApiResponse<JobSeekerResponse> updateProfile(
            @Valid @RequestBody JobSeekerUpdateRequest request,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        JobSeekerResponse response = jobSeekerService.updateJobSeekerProfile(userId, request);
        return ApiResponse.success("简历更新成功", response);
    }
    
    @PutMapping("/avatar")
    public ApiResponse<String> updateAvatar(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        String avatarUrl = request.get("avatarUrl");
        jobSeekerService.updateAvatar(userId, avatarUrl);
        return ApiResponse.success("头像更新成功");
    }
    
    @GetMapping("/{id}")
    public ApiResponse<JobSeekerResponse> getJobSeekerById(@PathVariable Long id) {
        JobSeekerResponse response = jobSeekerService.getJobSeekerById(id);
        return ApiResponse.success(response);
    }
    
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractUserId(token);
    }
}
