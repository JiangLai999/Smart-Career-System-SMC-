package com.example.smartcareer.service;

import com.example.smartcareer.dto.request.InterviewRequest;
import com.example.smartcareer.dto.response.InterviewResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface InterviewService {
    
    InterviewResponse createInterview(Long hrId, InterviewRequest request);
    
    InterviewResponse updateInterview(Long interviewId, InterviewRequest request);
    
    InterviewResponse getInterviewById(Long interviewId);
    
    void deleteInterview(Long interviewId);
    
    Page<InterviewResponse> getInterviewsByEnterprise(Long enterpriseId, int page, int size);
    
    Page<InterviewResponse> getInterviewsByEnterpriseAndStatus(Long enterpriseId, String status, int page, int size);
    
    Page<InterviewResponse> getInterviewsByJobSeeker(Long jobSeekerId, int page, int size);
    
    List<InterviewResponse> getInterviewsByJobSeekerAndStatus(Long jobSeekerId, String status);
    
    InterviewResponse confirmInterview(Long interviewId, Long jobSeekerId);
    
    InterviewResponse rejectInterview(Long interviewId, Long jobSeekerId, String reason);
    
    InterviewResponse submitFeedback(Long interviewId, Long hrId, String feedback, String result);
    
    InterviewResponse cancelInterview(Long interviewId, Long operatorId, String reason);
    
    Map<String, Object> getInterviewStatistics(Long enterpriseId);
    
    List<InterviewResponse> getUpcomingInterviews(Long enterpriseId);
}
