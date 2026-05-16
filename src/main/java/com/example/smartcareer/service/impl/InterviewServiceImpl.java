package com.example.smartcareer.service.impl;

import com.example.smartcareer.dto.request.InterviewRequest;
import com.example.smartcareer.dto.response.InterviewResponse;
import com.example.smartcareer.entity.*;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.repository.*;
import com.example.smartcareer.service.InterviewService;
import com.example.smartcareer.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl implements InterviewService {
    
    @Autowired
    private InterviewRepository interviewRepository;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    
    @Autowired
    private EnterpriseHRRepository hrRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    @Override
    @Transactional
    public InterviewResponse createInterview(Long hrId, InterviewRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        EnterpriseHR hr = hrRepository.findById(hrId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        Enterprise enterprise = hr.getEnterprise();
        
        JobSeeker jobSeeker = application.getJobSeeker();
        Job job = application.getJob();
        
        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setJobSeeker(jobSeeker);
        interview.setJob(job);
        interview.setEnterprise(enterprise);
        interview.setHr(hr);
        interview.setInterviewTime(request.getInterviewTime());
        interview.setLocation(request.getLocation());
        interview.setInterviewType(request.getInterviewType());
        interview.setInterviewLink(request.getInterviewLink());
        interview.setInterviewer(request.getInterviewer());
        interview.setInterviewerPosition(request.getInterviewerPosition());
        interview.setContactPhone(request.getContactPhone());
        interview.setNotes(request.getNotes());
        interview.setStatus("PENDING");
        
        Interview saved = interviewRepository.save(interview);
        
        application.setStatus("INTERVIEW");
        applicationRepository.save(application);
        
        // 发送面试通知给求职者
        try {
            Long userId = jobSeeker.getId();
            String interviewTimeStr = request.getInterviewTime() != null 
                ? request.getInterviewTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                : "";
            notificationService.sendInterviewNotification(
                userId,
                saved.getId(),
                job.getTitle(),
                interviewTimeStr,
                request.getLocation()
            );
        } catch (Exception e) {
            // 通知发送失败不影响面试创建
        }
        
        return convertToResponse(saved);
    }
    
    @Override
    @Transactional
    public InterviewResponse updateInterview(Long interviewId, InterviewRequest request) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        interview.setInterviewTime(request.getInterviewTime());
        interview.setLocation(request.getLocation());
        interview.setInterviewType(request.getInterviewType());
        interview.setInterviewLink(request.getInterviewLink());
        interview.setInterviewer(request.getInterviewer());
        interview.setInterviewerPosition(request.getInterviewerPosition());
        interview.setContactPhone(request.getContactPhone());
        interview.setNotes(request.getNotes());
        
        Interview saved = interviewRepository.save(interview);
        return convertToResponse(saved);
    }
    
    @Override
    public InterviewResponse getInterviewById(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        return convertToResponse(interview);
    }
    
    @Override
    @Transactional
    public void deleteInterview(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        interview.setStatus("CANCELLED");
        interviewRepository.save(interview);
    }
    
    @Override
    public Page<InterviewResponse> getInterviewsByEnterprise(Long enterpriseId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Interview> interviews = interviewRepository.findByEnterpriseId(enterpriseId, pageable);
        return interviews.map(this::convertToResponse);
    }
    
    @Override
    public Page<InterviewResponse> getInterviewsByEnterpriseAndStatus(Long enterpriseId, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Interview> interviews = interviewRepository.findByEnterpriseAndStatus(enterpriseId, status, pageable);
        return interviews.map(this::convertToResponse);
    }
    
    @Override
    public Page<InterviewResponse> getInterviewsByJobSeeker(Long jobSeekerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Interview> interviews = interviewRepository.findByJobSeekerId(jobSeekerId, pageable);
        return interviews.map(this::convertToResponse);
    }
    
    @Override
    public List<InterviewResponse> getInterviewsByJobSeekerAndStatus(Long jobSeekerId, String status) {
        List<Interview> interviews = interviewRepository.findByJobSeekerAndStatus(jobSeekerId, status);
        return interviews.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public InterviewResponse confirmInterview(Long interviewId, Long jobSeekerId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        if (!interview.getJobSeeker().getId().equals(jobSeekerId)) {
            throw new BusinessException(ErrorCode.PERMISSION_DENIED);
        }
        
        interview.setStatus("CONFIRMED");
        interview.setResponseTime(LocalDateTime.now());
        
        Interview saved = interviewRepository.save(interview);
        return convertToResponse(saved);
    }
    
    @Override
    @Transactional
    public InterviewResponse rejectInterview(Long interviewId, Long jobSeekerId, String reason) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        if (!interview.getJobSeeker().getId().equals(jobSeekerId)) {
            throw new BusinessException(ErrorCode.PERMISSION_DENIED);
        }
        
        interview.setStatus("REJECTED");
        interview.setFeedback(reason);
        interview.setResponseTime(LocalDateTime.now());
        
        Interview saved = interviewRepository.save(interview);
        return convertToResponse(saved);
    }
    
    @Override
    @Transactional
    public InterviewResponse submitFeedback(Long interviewId, Long hrId, String feedback, String result) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        interview.setFeedback(feedback);
        interview.setResult(result);
        interview.setStatus("COMPLETED");
        
        Application application = interview.getApplication();
        if ("PASS".equals(result)) {
            application.setStatus("OFFERED");
            applicationRepository.save(application);
            // 发送录用通知给求职者
            try {
                Long userId = interview.getJobSeeker().getId();
                String jobTitle = interview.getJob().getTitle();
                String companyName = interview.getEnterprise().getEnterpriseName();
                notificationService.sendHireNotification(userId, application.getId(), jobTitle, companyName);
            } catch (Exception e) {
                // 通知失败不影响主流程
            }
        } else if ("FAIL".equals(result)) {
            application.setStatus("REJECTED");
            applicationRepository.save(application);
            // 发送拒绝通知给求职者
            try {
                Long userId = interview.getJobSeeker().getId();
                String jobTitle = interview.getJob().getTitle();
                String companyName = interview.getEnterprise().getEnterpriseName();
                notificationService.sendRejectNotification(userId, application.getId(), jobTitle, companyName);
            } catch (Exception e) {
                // 通知失败不影响主流程
            }
        }
        
        Interview saved = interviewRepository.save(interview);
        return convertToResponse(saved);
    }
    
    @Override
    @Transactional
    public InterviewResponse cancelInterview(Long interviewId, Long operatorId, String reason) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
        
        interview.setStatus("CANCELLED");
        String existingNotes = interview.getNotes() != null ? interview.getNotes() : "";
        if (reason != null && !reason.isEmpty()) {
            interview.setNotes(existingNotes.isEmpty() ? "取消原因：" + reason : existingNotes + "\n取消原因：" + reason);
        }
        
        Interview saved = interviewRepository.save(interview);
        return convertToResponse(saved);
    }
    
    @Override
    public Map<String, Object> getInterviewStatistics(Long enterpriseId) {
        Map<String, Object> statistics = new HashMap<>();

        List<Interview> allInterviews = interviewRepository.findByEnterpriseId(enterpriseId);

        // 本月范围
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        List<Interview> monthInterviews = allInterviews.stream()
                .filter(i -> i.getCreateTime() != null && i.getCreateTime().isAfter(monthStart))
                .collect(Collectors.toList());

        statistics.put("totalCount", monthInterviews.size());

        long pendingCount   = monthInterviews.stream().filter(i -> "PENDING".equals(i.getStatus())).count();
        long confirmedCount = monthInterviews.stream().filter(i -> "CONFIRMED".equals(i.getStatus())).count();
        long completedCount = monthInterviews.stream().filter(i -> "COMPLETED".equals(i.getStatus())).count();
        long cancelledCount = monthInterviews.stream().filter(i -> "CANCELLED".equals(i.getStatus())).count();

        statistics.put("pendingCount", pendingCount + confirmedCount);
        statistics.put("confirmedCount", confirmedCount);
        statistics.put("completedCount", completedCount);
        statistics.put("cancelledCount", cancelledCount);

        long passCount = monthInterviews.stream()
                .filter(i -> "COMPLETED".equals(i.getStatus()) && "PASS".equals(i.getResult()))
                .count();
        long failCount = monthInterviews.stream()
                .filter(i -> "COMPLETED".equals(i.getStatus()) && "FAIL".equals(i.getResult()))
                .count();

        statistics.put("passCount", passCount);
        statistics.put("failCount", failCount);

        return statistics;
    }
    
    @Override
    public List<InterviewResponse> getUpcomingInterviews(Long enterpriseId) {
        List<Interview> interviews = interviewRepository.findByEnterpriseId(enterpriseId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        
        return interviews.stream()
                .filter(i -> ("PENDING".equals(i.getStatus()) || "CONFIRMED".equals(i.getStatus())))
                .filter(i -> i.getInterviewTime() != null && i.getInterviewTime().isAfter(now) && i.getInterviewTime().isBefore(tomorrow))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    private InterviewResponse convertToResponse(Interview interview) {
        InterviewResponse response = new InterviewResponse();
        response.setId(interview.getId());
        response.setApplicationId(interview.getApplication().getId());
        response.setJobSeekerId(interview.getJobSeeker().getId());
        response.setJobSeekerName(interview.getJobSeeker().getRealName());
        response.setJobId(interview.getJob().getId());
        response.setJobTitle(interview.getJob().getTitle());
        response.setCompanyName(interview.getEnterprise().getEnterpriseName());
        response.setEnterpriseId(interview.getEnterprise().getId());
        
        if (interview.getHr() != null) {
            response.setHrId(interview.getHr().getId());
            response.setHrName(interview.getHr().getName());
        }
        
        response.setInterviewTime(interview.getInterviewTime());
        response.setLocation(interview.getLocation());
        response.setInterviewType(interview.getInterviewType());
        response.setInterviewLink(interview.getInterviewLink());
        response.setInterviewer(interview.getInterviewer());
        response.setInterviewerPosition(interview.getInterviewerPosition());
        response.setContactPhone(interview.getContactPhone());
        response.setStatus(interview.getStatus());
        response.setNotes(interview.getNotes());
        response.setFeedback(interview.getFeedback());
        response.setResult(interview.getResult());
        response.setCreateTime(interview.getCreateTime());
        response.setUpdateTime(interview.getUpdateTime());
        response.setResponseTime(interview.getResponseTime());
        
        return response;
    }
}
