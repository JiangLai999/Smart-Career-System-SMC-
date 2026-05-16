package com.example.smartcareer.controller;

import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.entity.Assessment;
import com.example.smartcareer.entity.Interview;
import com.example.smartcareer.repository.ApplicationRepository;
import com.example.smartcareer.repository.AssessmentRepository;
import com.example.smartcareer.repository.InterviewRepository;
import com.example.smartcareer.repository.JobSeekerRepository;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/application")
public class StatsController {
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private InterviewRepository interviewRepository;
    
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    
    @Autowired
    private AssessmentRepository assessmentRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getUserStats(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);
        
        Map<String, Object> stats = new HashMap<>();
        
        try {
            long appliedCount = applicationRepository.countByJobSeekerId(userId);
            stats.put("applied", appliedCount);
        } catch (Exception e) {
            stats.put("applied", 0);
        }

        try {
            LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
            long weeklyCount = applicationRepository.countByJobSeekerIdAndApplyTimeAfter(userId, weekStart);
            stats.put("weeklyApplied", weeklyCount);
        } catch (Exception e) {
            stats.put("weeklyApplied", 0);
        }
        
        try {
            long interviewCount = interviewRepository.countByJobSeekerIdAndStatus(userId, "SCHEDULED");
            stats.put("interviews", interviewCount);
        } catch (Exception e) {
            stats.put("interviews", 0);
        }

        try {
            LocalDateTime now = LocalDateTime.now();
            List<Interview> upcoming = new java.util.ArrayList<>(interviewRepository.findByJobSeekerAndStatus(userId, "SCHEDULED"));
            upcoming.addAll(interviewRepository.findByJobSeekerAndStatus(userId, "CONFIRMED"));
            Optional<Interview> next = upcoming.stream()
                    .filter(i -> i.getInterviewTime() != null && i.getInterviewTime().isAfter(now))
                    .min(Comparator.comparing(Interview::getInterviewTime));
            if (next.isPresent()) {
                String formatted = next.get().getInterviewTime()
                        .format(DateTimeFormatter.ofPattern("MM月dd日 HH:mm"));
                stats.put("nextInterview", formatted);
            } else {
                stats.put("nextInterview", null);
            }
        } catch (Exception e) {
            stats.put("nextInterview", null);
        }
        
        try {
            long viewedCount = applicationRepository.countByJobSeekerIdAndStatus(userId, "VIEWED");
            stats.put("viewed", viewedCount);
        } catch (Exception e) {
            stats.put("viewed", 0);
        }
        
        stats.put("profileComplete", calculateProfileComplete(userId));
        stats.put("assessmentScore", calculateAssessmentScore(userId));
        stats.put("topPercent", calculateTopPercent(userId));
        
        return ApiResponse.success(stats);
    }
    
    private int calculateAssessmentScore(Long userId) {
        try {
            List<Assessment> assessments = assessmentRepository.findByJobSeekerIdOrderByTime(userId);
            if (assessments.isEmpty()) {
                return 0;
            }
            
            Assessment latestAssessment = assessments.get(0);
            Integer score = latestAssessment.getScore();
            return score != null ? score : 0;
        } catch (Exception e) {
            return 0;
        }
    }
    
    private int calculateTopPercent(Long userId) {
        try {
            List<Assessment> allAssessments = assessmentRepository.findAll();
            if (allAssessments.isEmpty()) {
                return 10;
            }
            
            Optional<Assessment> userAssessment = allAssessments.stream()
                    .filter(a -> a.getJobSeeker() != null && a.getJobSeeker().getId().equals(userId))
                    .findFirst();
            
            if (!userAssessment.isPresent() || userAssessment.get().getScore() == null) {
                return 10;
            }
            
            int userScore = userAssessment.get().getScore();
            long higherCount = allAssessments.stream()
                    .filter(a -> a.getScore() != null && a.getScore() > userScore)
                    .count();
            
            int totalWithScore = (int) allAssessments.stream()
                    .filter(a -> a.getScore() != null)
                    .count();
            
            if (totalWithScore == 0) {
                return 10;
            }
            
            int topPercent = (int) Math.ceil((higherCount + 1) * 100.0 / totalWithScore);
            return Math.min(topPercent, 100);
        } catch (Exception e) {
            return 10;
        }
    }
    
    private int calculateProfileComplete(Long userId) {
        int complete = 0;
        
        try {
            Optional<com.example.smartcareer.entity.JobSeeker> jobSeekerOpt = jobSeekerRepository.findById(userId);
            if (jobSeekerOpt.isPresent()) {
                com.example.smartcareer.entity.JobSeeker jobSeeker = jobSeekerOpt.get();
                
                if (jobSeeker.getRealName() != null && !jobSeeker.getRealName().isEmpty()) complete += 20;
                if (jobSeeker.getPhone() != null && !jobSeeker.getPhone().isEmpty()) complete += 15;
                if (jobSeeker.getEmail() != null && !jobSeeker.getEmail().isEmpty()) complete += 15;
                if (jobSeeker.getEducation() != null && !jobSeeker.getEducation().isEmpty()) complete += 15;
                if (jobSeeker.getSchool() != null && !jobSeeker.getSchool().isEmpty()) complete += 10;
                if (jobSeeker.getSkills() != null && !jobSeeker.getSkills().isEmpty()) complete += 15;
                if (jobSeeker.getResume() != null && !jobSeeker.getResume().isEmpty()) complete += 10;
            }
        } catch (Exception e) {
        }
        
        return complete;
    }
}
