package com.example.smartcareer.service.impl;

import com.example.smartcareer.dto.request.JobSeekerUpdateRequest;
import com.example.smartcareer.dto.response.JobSeekerResponse;
import com.example.smartcareer.entity.JobSeeker;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.repository.JobSeekerRepository;
import com.example.smartcareer.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {
    
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    
    @Override
    public JobSeekerResponse getJobSeekerById(Long id) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return convertToResponse(jobSeeker);
    }
    
    @Override
    public JobSeekerResponse getJobSeekerProfile(Long userId) {
        Optional<JobSeeker> jobSeekerOpt = jobSeekerRepository.findById(userId);
        if (!jobSeekerOpt.isPresent()) {
            JobSeekerResponse response = new JobSeekerResponse();
            response.setId(userId);
            return response;
        }
        return convertToResponse(jobSeekerOpt.get());
    }
    
    @Override
    @Transactional
    public JobSeekerResponse updateJobSeekerProfile(Long userId, JobSeekerUpdateRequest request) {
        JobSeeker jobSeeker = jobSeekerRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        if (request.getRealName() != null) {
            jobSeeker.setRealName(request.getRealName());
        }
        if (request.getGender() != null) {
            jobSeeker.setGender(request.getGender());
        }
        if (request.getBirthday() != null) {
            jobSeeker.setBirthday(request.getBirthday());
        }
        if (request.getEducation() != null) {
            jobSeeker.setEducation(request.getEducation());
        }
        if (request.getSchool() != null) {
            jobSeeker.setSchool(request.getSchool());
        }
        if (request.getMajor() != null) {
            jobSeeker.setMajor(request.getMajor());
        }
        if (request.getWorkYears() != null) {
            jobSeeker.setWorkYears(request.getWorkYears());
        }
        if (request.getResume() != null) {
            jobSeeker.setResume(request.getResume());
        }
        if (request.getSkills() != null) {
            jobSeeker.setSkills(request.getSkills());
        }
        if (request.getExpectedPosition() != null) {
            jobSeeker.setExpectedPosition(request.getExpectedPosition());
        }
        if (request.getExpectedLocation() != null) {
            jobSeeker.setExpectedLocation(request.getExpectedLocation());
        }
        if (request.getExpectedSalaryMin() != null) {
            jobSeeker.setExpectedSalaryMin(request.getExpectedSalaryMin());
        }
        if (request.getExpectedSalaryMax() != null) {
            jobSeeker.setExpectedSalaryMax(request.getExpectedSalaryMax());
        }
        
        JobSeeker saved = jobSeekerRepository.save(jobSeeker);
        return convertToResponse(saved);
    }
    
    @Override
    @Transactional
    public void updateAvatar(Long userId, String avatarUrl) {
        Optional<JobSeeker> jobSeekerOpt = jobSeekerRepository.findById(userId);
        if (jobSeekerOpt.isPresent()) {
            JobSeeker jobSeeker = jobSeekerOpt.get();
            jobSeeker.setAvatar(avatarUrl);
            jobSeekerRepository.save(jobSeeker);
        }
    }
    
    private JobSeekerResponse convertToResponse(JobSeeker jobSeeker) {
        JobSeekerResponse response = new JobSeekerResponse();
        response.setId(jobSeeker.getId());
        response.setUsername(jobSeeker.getUsername());
        response.setPhone(jobSeeker.getPhone());
        response.setEmail(jobSeeker.getEmail());
        response.setRealName(jobSeeker.getRealName());
        response.setGender(jobSeeker.getGender());
        response.setBirthday(jobSeeker.getBirthday());
        response.setEducation(jobSeeker.getEducation());
        response.setSchool(jobSeeker.getSchool());
        response.setMajor(jobSeeker.getMajor());
        response.setWorkYears(jobSeeker.getWorkYears());
        response.setResume(jobSeeker.getResume());
        response.setSkills(jobSeeker.getSkills());
        response.setExpectedPosition(jobSeeker.getExpectedPosition());
        response.setExpectedLocation(jobSeeker.getExpectedLocation());
        response.setExpectedSalaryMin(jobSeeker.getExpectedSalaryMin());
        response.setExpectedSalaryMax(jobSeeker.getExpectedSalaryMax());
        response.setAvatar(jobSeeker.getAvatar());
        response.setStatus(jobSeeker.getStatus());
        response.setCreateTime(jobSeeker.getCreateTime());
        return response;
    }
}
