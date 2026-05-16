package com.example.smartcareer.controller;

import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.dto.response.JobResponse;
import com.example.smartcareer.recommendation.RecommendationEngine;
import com.example.smartcareer.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {
    
    @Autowired
    private RecommendationEngine recommendationEngine;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/jobs")
    public ApiResponse<List<JobResponse>> getRecommendedJobs(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "10") int limit) {
        
        String jwtToken = token.substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);
        
        List<JobResponse> recommendedJobs = recommendationEngine.recommendJobsForJobSeeker(userId, limit);
        return ApiResponse.success(recommendedJobs);
    }
}
