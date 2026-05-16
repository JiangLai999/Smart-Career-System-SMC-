package com.example.smartcareer.controller;

import com.example.smartcareer.dto.request.AssessmentRequest;
import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.dto.response.AssessmentResponse;
import com.example.smartcareer.service.AssessmentService;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {
    
    @Autowired
    private AssessmentService assessmentService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/categories")
    public ApiResponse<List<String>> getCategories() {
        List<String> categories = assessmentService.getAssessmentCategories();
        return ApiResponse.success(categories);
    }
    
    @GetMapping("/questions/{category}")
    public ApiResponse<List<AssessmentResponse.QuestionResponse>> getQuestionsByCategory(
            @PathVariable String category) {
        List<AssessmentResponse.QuestionResponse> questions = assessmentService.getQuestionsByCategory(category);
        return ApiResponse.success(questions);
    }
    
    @PostMapping("/submit")
    public ApiResponse<AssessmentResponse> submitAssessment(
            @RequestBody AssessmentRequest request,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        AssessmentResponse response = assessmentService.submitAssessment(userId, request);
        return ApiResponse.success("测评提交成功", response);
    }
    
    @GetMapping("/{assessmentId}")
    public ApiResponse<AssessmentResponse> getAssessmentById(@PathVariable Long assessmentId) {
        AssessmentResponse response = assessmentService.getAssessmentById(assessmentId);
        return ApiResponse.success(response);
    }
    
    @GetMapping("/my")
    public ApiResponse<Page<AssessmentResponse>> getMyAssessments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        Page<AssessmentResponse> assessments = assessmentService.getMyAssessments(userId, page, size);
        return ApiResponse.success(assessments);
    }
    
    @GetMapping("/my/type/{type}")
    public ApiResponse<List<AssessmentResponse>> getAssessmentsByType(
            @PathVariable String type,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        List<AssessmentResponse> assessments = assessmentService.getAssessmentsByType(userId, type);
        return ApiResponse.success(assessments);
    }
    
    @GetMapping("/statistics")
    public ApiResponse<Map<String, Object>> getAssessmentStatistics(HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        Map<String, Object> statistics = assessmentService.getAssessmentStatistics(userId);
        return ApiResponse.success(statistics);
    }
    
    @DeleteMapping("/{assessmentId}")
    public ApiResponse<Void> deleteAssessment(
            @PathVariable Long assessmentId,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        assessmentService.deleteAssessment(userId, assessmentId);
        return ApiResponse.success("测评记录已删除", null);
    }
    
    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.extractUserId(token);
    }
}
