package com.example.smartcareer.recommendation;

import com.example.smartcareer.dto.response.JobResponse;
import com.example.smartcareer.entity.Job;
import com.example.smartcareer.entity.JobSeeker;

import java.util.List;

public interface ContentBasedRecommender {
    
    List<Job> recommendJobs(JobSeeker jobSeeker, List<Job> availableJobs, int limit);
    
    double calculateMatchScore(JobSeeker jobSeeker, Job job);
    
    double calculateSkillMatch(String seekerSkills, String jobRequirement);
    
    double calculateEducationMatch(String seekerEducation, String jobEducation);
    
    double calculateExperienceMatch(Integer seekerYears, String jobExperience);
    
    double calculateSalaryMatch(Integer seekerMin, Integer seekerMax, Integer jobMin, Integer jobMax);
    
    double calculateLocationMatch(String seekerLocation, String jobLocation);
}
