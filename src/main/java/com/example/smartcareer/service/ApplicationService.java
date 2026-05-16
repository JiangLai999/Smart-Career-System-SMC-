package com.example.smartcareer.service;

import com.example.smartcareer.dto.request.ApplicationRequest;
import com.example.smartcareer.dto.response.ApplicationResponse;
import com.example.smartcareer.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplicationService {

    Application applyForJob(ApplicationRequest request, Long jobSeekerId);

    Application updateApplicationStatus(Long id, String status);

    void deleteApplication(Long id);

    Application getApplicationById(Long id);

    Page<Application> getApplicationsByJobSeeker(Long jobSeekerId, Pageable pageable);

    Page<Application> getApplicationsByJob(Long jobId, Pageable pageable);

    Page<Application> getApplicationsByEnterprise(Long enterpriseId, Pageable pageable);

    boolean hasApplied(Long jobSeekerId, Long jobId);

    List<Application> findByJobSeekerAndJob(Long jobSeekerId, Long jobId);

    ApplicationResponse convertToResponse(Application application);
}
