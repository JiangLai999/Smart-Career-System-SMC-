package com.example.smartcareer.dto.response;

import java.time.LocalDateTime;

public class ApplicationResponse {
    
    private Long id;
    
    private String status;
    
    private LocalDateTime applyTime;
    
    private LocalDateTime viewTime;
    
    private String coverLetter;
    
    private String hrComment;
    
    private String rejectReason;
    
    private JobResponse job;
    
    private UserResponse jobSeeker;
    
    private String enterpriseName;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getApplyTime() {
        return applyTime;
    }
    
    public void setApplyTime(LocalDateTime applyTime) {
        this.applyTime = applyTime;
    }
    
    public LocalDateTime getViewTime() {
        return viewTime;
    }
    
    public void setViewTime(LocalDateTime viewTime) {
        this.viewTime = viewTime;
    }
    
    public String getCoverLetter() {
        return coverLetter;
    }
    
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
    
    public String getHrComment() {
        return hrComment;
    }
    
    public void setHrComment(String hrComment) {
        this.hrComment = hrComment;
    }
    
    public String getRejectReason() {
        return rejectReason;
    }
    
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
    
    public JobResponse getJob() {
        return job;
    }
    
    public void setJob(JobResponse job) {
        this.job = job;
    }
    
    public UserResponse getJobSeeker() {
        return jobSeeker;
    }
    
    public void setJobSeeker(UserResponse jobSeeker) {
        this.jobSeeker = jobSeeker;
    }
    
    public String getEnterpriseName() {
        return enterpriseName;
    }
    
    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
}
