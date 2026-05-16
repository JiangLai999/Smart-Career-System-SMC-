package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview")
public class Interview {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeeker jobSeeker;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_id")
    private EnterpriseHR hr;
    
    @Column(nullable = false)
    private LocalDateTime interviewTime;
    
    @Column(length = 200)
    private String location;
    
    @Column(length = 50)
    private String interviewType;
    
    @Column(length = 200)
    private String interviewLink;
    
    @Column(length = 50)
    private String interviewer;
    
    @Column(length = 20)
    private String interviewerPosition;
    
    @Column(length = 50)
    private String contactPhone;
    
    @Column(nullable = false, length = 20)
    private String status = "PENDING";
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(columnDefinition = "TEXT")
    private String feedback;
    
    @Column(length = 50)
    private String result;
    
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private LocalDateTime responseTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Application getApplication() {
        return application;
    }
    
    public void setApplication(Application application) {
        this.application = application;
    }
    
    public JobSeeker getJobSeeker() {
        return jobSeeker;
    }
    
    public void setJobSeeker(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }
    
    public Job getJob() {
        return job;
    }
    
    public void setJob(Job job) {
        this.job = job;
    }
    
    public Enterprise getEnterprise() {
        return enterprise;
    }
    
    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }
    
    public EnterpriseHR getHr() {
        return hr;
    }
    
    public void setHr(EnterpriseHR hr) {
        this.hr = hr;
    }
    
    public LocalDateTime getInterviewTime() {
        return interviewTime;
    }
    
    public void setInterviewTime(LocalDateTime interviewTime) {
        this.interviewTime = interviewTime;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getInterviewType() {
        return interviewType;
    }
    
    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }
    
    public String getInterviewLink() {
        return interviewLink;
    }
    
    public void setInterviewLink(String interviewLink) {
        this.interviewLink = interviewLink;
    }
    
    public String getInterviewer() {
        return interviewer;
    }
    
    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }
    
    public String getInterviewerPosition() {
        return interviewerPosition;
    }
    
    public void setInterviewerPosition(String interviewerPosition) {
        this.interviewerPosition = interviewerPosition;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public LocalDateTime getResponseTime() {
        return responseTime;
    }
    
    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }
}
