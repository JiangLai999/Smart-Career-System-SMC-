package com.example.smartcareer.recommendation;

import com.example.smartcareer.entity.Job;
import com.example.smartcareer.entity.JobSeeker;
import com.example.smartcareer.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ContentBasedRecommenderImpl implements ContentBasedRecommender {
    
    @Autowired
    private JobRepository jobRepository;
    
    @Override
    public List<Job> recommendJobs(JobSeeker jobSeeker, List<Job> availableJobs, int limit) {
        if (availableJobs == null || availableJobs.isEmpty()) {
            availableJobs = jobRepository.findByStatus("PUBLISHED");
        }
        
        Map<Job, Double> jobScores = new HashMap<>();
        
        for (Job job : availableJobs) {
            double score = calculateMatchScore(jobSeeker, job);
            if (score > 0) {
                jobScores.put(job, score);
            }
        }
        
        return jobScores.entrySet().stream()
                .sorted(Map.Entry.<Job, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    @Override
    public double calculateMatchScore(JobSeeker jobSeeker, Job job) {
        double totalScore = 0.0;
        double totalWeight = 0.0;
        
        double skillWeight = 0.35;
        double educationWeight = 0.20;
        double experienceWeight = 0.20;
        double salaryWeight = 0.15;
        double locationWeight = 0.10;
        
        if (jobSeeker.getSkills() != null && job.getRequirement() != null) {
            double skillScore = calculateSkillMatch(jobSeeker.getSkills(), job.getRequirement());
            totalScore += skillScore * skillWeight;
            totalWeight += skillWeight;
        }
        
        if (jobSeeker.getEducation() != null && job.getEducation() != null) {
            double educationScore = calculateEducationMatch(jobSeeker.getEducation(), job.getEducation());
            totalScore += educationScore * educationWeight;
            totalWeight += educationWeight;
        }
        
        if (jobSeeker.getWorkYears() != null && job.getExperience() != null) {
            double experienceScore = calculateExperienceMatch(jobSeeker.getWorkYears(), job.getExperience());
            totalScore += experienceScore * experienceWeight;
            totalWeight += experienceWeight;
        }
        
        if (jobSeeker.getExpectedSalaryMin() != null && jobSeeker.getExpectedSalaryMax() != null
                && job.getSalaryMin() != null && job.getSalaryMax() != null) {
            double salaryScore = calculateSalaryMatch(
                    jobSeeker.getExpectedSalaryMin(), jobSeeker.getExpectedSalaryMax(),
                    job.getSalaryMin(), job.getSalaryMax()
            );
            totalScore += salaryScore * salaryWeight;
            totalWeight += salaryWeight;
        }
        
        if (jobSeeker.getExpectedLocation() != null && job.getLocation() != null) {
            double locationScore = calculateLocationMatch(jobSeeker.getExpectedLocation(), job.getLocation());
            totalScore += locationScore * locationWeight;
            totalWeight += locationWeight;
        }
        
        return totalWeight > 0 ? totalScore / totalWeight : 0.0;
    }
    
    @Override
    public double calculateSkillMatch(String seekerSkills, String jobRequirement) {
        if (seekerSkills == null || jobRequirement == null) {
            return 0.0;
        }
        
        String[] skills = seekerSkills.toLowerCase().split("[,，、\\s]+");
        String requirement = jobRequirement.toLowerCase();
        
        int matchCount = 0;
        for (String skill : skills) {
            if (skill.trim().length() > 0 && requirement.contains(skill.trim())) {
                matchCount++;
            }
        }
        
        return skills.length > 0 ? (double) matchCount / skills.length : 0.0;
    }
    
    @Override
    public double calculateEducationMatch(String seekerEducation, String jobEducation) {
        if (seekerEducation == null || jobEducation == null) {
            return 0.5;
        }
        
        Map<String, Integer> educationLevel = new HashMap<>();
        educationLevel.put("博士", 5);
        educationLevel.put("硕士", 4);
        educationLevel.put("本科", 3);
        educationLevel.put("大专", 2);
        educationLevel.put("高中", 1);
        
        int seekerLevel = educationLevel.getOrDefault(seekerEducation, 0);
        
        for (Map.Entry<String, Integer> entry : educationLevel.entrySet()) {
            if (jobEducation.contains(entry.getKey())) {
                int requiredLevel = entry.getValue();
                if (seekerLevel >= requiredLevel) {
                    return 1.0;
                } else if (seekerLevel >= requiredLevel - 1) {
                    return 0.7;
                } else {
                    return 0.3;
                }
            }
        }
        
        return 0.5;
    }
    
    @Override
    public double calculateExperienceMatch(Integer seekerYears, String jobExperience) {
        if (seekerYears == null || jobExperience == null) {
            return 0.5;
        }
        
        try {
            if (jobExperience.contains("不限") || jobExperience.contains("应届")) {
                return 1.0;
            }
            
            int minYears = 0;
            int maxYears = Integer.MAX_VALUE;
            
            String[] parts = jobExperience.split("[\\-~至]");
            if (parts.length >= 1) {
                minYears = Integer.parseInt(parts[0].replaceAll("\\D", ""));
            }
            if (parts.length >= 2) {
                maxYears = Integer.parseInt(parts[1].replaceAll("\\D", ""));
            } else {
                if (jobExperience.contains("以上") || jobExperience.contains("及以上")) {
                    maxYears = Integer.MAX_VALUE;
                }
            }
            
            if (seekerYears >= minYears && seekerYears <= maxYears) {
                return 1.0;
            } else if (seekerYears >= minYears - 1) {
                return 0.8;
            } else if (seekerYears >= minYears - 2) {
                return 0.6;
            } else {
                return 0.3;
            }
        } catch (Exception e) {
            return 0.5;
        }
    }
    
    @Override
    public double calculateSalaryMatch(Integer seekerMin, Integer seekerMax, Integer jobMin, Integer jobMax) {
        if (seekerMin == null || seekerMax == null || jobMin == null || jobMax == null) {
            return 0.5;
        }
        
        double seekerMid = (seekerMin + seekerMax) / 2.0;
        double jobMid = (jobMin + jobMax) / 2.0;
        
        double overlap = Math.min(seekerMax, jobMax) - Math.max(seekerMin, jobMin);
        
        if (overlap >= 0) {
            return 1.0;
        }
        
        double gap = Math.abs(seekerMid - jobMid);
        double maxSalary = Math.max(seekerMax - seekerMin, jobMax - jobMin);
        
        double score = 1.0 - (gap / maxSalary);
        return Math.max(0.0, Math.min(1.0, score));
    }
    
    @Override
    public double calculateLocationMatch(String seekerLocation, String jobLocation) {
        if (seekerLocation == null || jobLocation == null) {
            return 0.5;
        }
        
        if (seekerLocation.equals(jobLocation)) {
            return 1.0;
        }
        
        if (seekerLocation.contains(jobLocation) || jobLocation.contains(seekerLocation)) {
            return 0.9;
        }
        
        if (seekerLocation.substring(0, 2).equals(jobLocation.substring(0, 2))) {
            return 0.7;
        }
        
        return 0.3;
    }
}
