package com.example.smartcareer.service;

import com.example.smartcareer.dto.request.JobSeekerUpdateRequest;
import com.example.smartcareer.dto.response.JobSeekerResponse;

public interface JobSeekerService {
    
    JobSeekerResponse getJobSeekerById(Long id);
    
    JobSeekerResponse getJobSeekerProfile(Long userId);
    
    JobSeekerResponse updateJobSeekerProfile(Long userId, JobSeekerUpdateRequest request);
    
    void updateAvatar(Long userId, String avatarUrl);
}
