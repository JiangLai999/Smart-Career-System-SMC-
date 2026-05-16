package com.example.smartcareer.controller;

import com.example.smartcareer.dto.request.InterviewRequest;
import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.dto.response.InterviewResponse;
import com.example.smartcareer.entity.EnterpriseHR;
import com.example.smartcareer.repository.EnterpriseHRRepository;
import com.example.smartcareer.service.InterviewService;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {
    
    @Autowired
    private InterviewService interviewService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private EnterpriseHRRepository hrRepository;
    
    @PostMapping("/create")
    public ApiResponse<InterviewResponse> createInterview(
            @RequestBody InterviewRequest request,
            HttpServletRequest httpRequest) {
        Long hrId = getCurrentHrId(httpRequest);
        InterviewResponse response = interviewService.createInterview(hrId, request);
        return ApiResponse.success("面试邀请发送成功", response);
    }
    
    @PutMapping("/{interviewId}")
    public ApiResponse<InterviewResponse> updateInterview(
            @PathVariable Long interviewId,
            @RequestBody InterviewRequest request) {
        InterviewResponse response = interviewService.updateInterview(interviewId, request);
        return ApiResponse.success("面试信息更新成功", response);
    }
    
    @GetMapping("/{interviewId}")
    public ApiResponse<InterviewResponse> getInterviewById(@PathVariable Long interviewId) {
        InterviewResponse response = interviewService.getInterviewById(interviewId);
        return ApiResponse.success(response);
    }
    
    @DeleteMapping("/{interviewId}")
    public ApiResponse<String> deleteInterview(@PathVariable Long interviewId) {
        interviewService.deleteInterview(interviewId);
        return ApiResponse.success("面试已取消");
    }
    
    @GetMapping("/enterprise")
    public ApiResponse<Page<InterviewResponse>> getEnterpriseInterviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        Long enterpriseId = getCurrentEnterpriseId(httpRequest);
        Page<InterviewResponse> interviews = interviewService.getInterviewsByEnterprise(enterpriseId, page, size);
        return ApiResponse.success(interviews);
    }
    
    @GetMapping("/enterprise/status/{status}")
    public ApiResponse<Page<InterviewResponse>> getEnterpriseInterviewsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        Long enterpriseId = getCurrentEnterpriseId(httpRequest);
        Page<InterviewResponse> interviews = interviewService.getInterviewsByEnterpriseAndStatus(enterpriseId, status, page, size);
        return ApiResponse.success(interviews);
    }
    
    @GetMapping("/jobseeker")
    public ApiResponse<Page<InterviewResponse>> getJobSeekerInterviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        Page<InterviewResponse> interviews = interviewService.getInterviewsByJobSeeker(userId, page, size);
        return ApiResponse.success(interviews);
    }
    
    @GetMapping("/jobseeker/status/{status}")
    public ApiResponse<List<InterviewResponse>> getJobSeekerInterviewsByStatus(
            @PathVariable String status,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        List<InterviewResponse> interviews = interviewService.getInterviewsByJobSeekerAndStatus(userId, status);
        return ApiResponse.success(interviews);
    }
    
    @PostMapping("/{interviewId}/confirm")
    public ApiResponse<InterviewResponse> confirmInterview(
            @PathVariable Long interviewId,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        InterviewResponse response = interviewService.confirmInterview(interviewId, userId);
        return ApiResponse.success("已确认参加面试", response);
    }
    
    @PostMapping("/{interviewId}/reject")
    public ApiResponse<InterviewResponse> rejectInterview(
            @PathVariable Long interviewId,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        String reason = request.get("reason");
        InterviewResponse response = interviewService.rejectInterview(interviewId, userId, reason);
        return ApiResponse.success("已拒绝面试", response);
    }
    
    @PostMapping("/{interviewId}/feedback")
    public ApiResponse<InterviewResponse> submitFeedback(
            @PathVariable Long interviewId,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        Long hrId = getCurrentHrId(httpRequest);
        String feedback = request.get("feedback");
        String result = request.get("result");
        InterviewResponse response = interviewService.submitFeedback(interviewId, hrId, feedback, result);
        return ApiResponse.success("面试反馈提交成功", response);
    }
    
    @PostMapping("/{interviewId}/cancel")
    public ApiResponse<InterviewResponse> cancelInterview(
            @PathVariable Long interviewId,
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        String reason = request.get("reason");
        InterviewResponse response = interviewService.cancelInterview(interviewId, userId, reason);
        return ApiResponse.success("面试已取消", response);
    }
    
    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getInterviewStatistics(HttpServletRequest httpRequest) {
        Long enterpriseId = getCurrentEnterpriseId(httpRequest);
        Map<String, Object> statistics = interviewService.getInterviewStatistics(enterpriseId);
        return ApiResponse.success(statistics);
    }
    
    @GetMapping("/upcoming")
    public ApiResponse<List<InterviewResponse>> getUpcomingInterviews(HttpServletRequest httpRequest) {
        Long enterpriseId = getCurrentEnterpriseId(httpRequest);
        List<InterviewResponse> interviews = interviewService.getUpcomingInterviews(enterpriseId);
        return ApiResponse.success(interviews);
    }
    
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractUserId(token);
    }
    
    private Long getCurrentHrId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.extractUserId(token);
        EnterpriseHR hr = hrRepository.findById(userId).orElse(null);
        return hr != null ? hr.getId() : userId;
    }
    
    private Long getCurrentEnterpriseId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.extractUserId(token);
        EnterpriseHR hr = hrRepository.findById(userId).orElse(null);
        return hr != null && hr.getEnterprise() != null ? hr.getEnterprise().getId() : null;
    }
}
