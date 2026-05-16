package com.example.smartcareer.recommendation;

import com.example.smartcareer.dto.response.JobResponse;
import com.example.smartcareer.entity.Job;
import com.example.smartcareer.entity.JobSeeker;
import com.example.smartcareer.repository.JobSeekerRepository;
import com.example.smartcareer.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationEngine {
    
    @Autowired
    private ContentBasedRecommender contentBasedRecommender;
    
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    
    @Autowired
    private JobService jobService;
    
    public List<JobResponse> recommendJobsForJobSeeker(Long jobSeekerId, int limit) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElse(null);
        
        if (jobSeeker == null) {
            return new ArrayList<>();
        }
        
        try {
            List<Job> recommendedJobs = contentBasedRecommender.recommendJobs(jobSeeker, null, limit);
            
            return recommendedJobs.stream()
                    .map(job -> {
                        try {
                            return jobService.convertToResponse(job);
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(r -> r != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public List<JobResponse> recommendJobsForJobSeeker(JobSeeker jobSeeker, List<Job> availableJobs, int limit) {
        try {
            List<Job> recommendedJobs = contentBasedRecommender.recommendJobs(jobSeeker, availableJobs, limit);
            
            return recommendedJobs.stream()
                    .map(job -> {
                        try {
                            return jobService.convertToResponse(job);
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(r -> r != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
