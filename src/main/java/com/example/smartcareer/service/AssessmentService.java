package com.example.smartcareer.service;

import com.example.smartcareer.dto.request.AssessmentRequest;
import com.example.smartcareer.dto.response.AssessmentResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface AssessmentService {
    
    List<String> getAssessmentCategories();
    
    List<AssessmentResponse.QuestionResponse> getQuestionsByCategory(String category);
    
    AssessmentResponse submitAssessment(Long userId, AssessmentRequest request);
    
    AssessmentResponse getAssessmentById(Long assessmentId);
    
    Page<AssessmentResponse> getMyAssessments(Long userId, int page, int size);
    
    List<AssessmentResponse> getAssessmentsByType(Long userId, String type);
    
    Map<String, Object> getAssessmentStatistics(Long userId);
    
    void deleteAssessment(Long userId, Long assessmentId);
}
