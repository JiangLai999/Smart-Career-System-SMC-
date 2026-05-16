package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application")
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
    
    @Column(nullable = false, length = 20)
    private String status = "APPLIED";
    
    @Column(nullable = false)
    private LocalDateTime applyTime;
    
    private LocalDateTime viewTime;
    
    private LocalDateTime updateTime;
    
    @Column(columnDefinition = "TEXT")
    private String resumeSnapshot;
    
    @Column(length = 500)
    private String coverLetter;
    
    @Column(columnDefinition = "TEXT")
    private String hrComment;
    
    @Column(length = 50)
    private String rejectReason;
    
    @Column(length = 20)
    private String tag;
    
    @Column(length = 100)
    private String markNote;
    
    private LocalDateTime markTime;
    
    @PrePersist
    protected void onCreate() {
        applyTime = LocalDateTime.now();
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
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getResumeSnapshot() {
        return resumeSnapshot;
    }
    
    public void setResumeSnapshot(String resumeSnapshot) {
        this.resumeSnapshot = resumeSnapshot;
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
    
    public String getTag() {
        return tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public String getMarkNote() {
        return markNote;
    }
    
    public void setMarkNote(String markNote) {
        this.markNote = markNote;
    }
    
    public LocalDateTime getMarkTime() {
        return markTime;
    }
    
    public void setMarkTime(LocalDateTime markTime) {
        this.markTime = markTime;
    }
}
