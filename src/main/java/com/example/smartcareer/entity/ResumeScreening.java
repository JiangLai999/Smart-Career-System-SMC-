package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resume_screening")
public class ResumeScreening {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_id")
    private EnterpriseHR hr;
    
    @Column(columnDefinition = "TEXT")
    private String screeningCriteria;
    
    @Column(columnDefinition = "TEXT")
    private String matchReport;
    
    private Integer matchScore;
    
    @Column(length = 20)
    private String screeningResult;
    
    @Column(columnDefinition = "TEXT")
    private String hrComment;
    
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
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
    
    public Job getJob() {
        return job;
    }
    
    public void setJob(Job job) {
        this.job = job;
    }
    
    public EnterpriseHR getHr() {
        return hr;
    }
    
    public void setHr(EnterpriseHR hr) {
        this.hr = hr;
    }
    
    public String getScreeningCriteria() {
        return screeningCriteria;
    }
    
    public void setScreeningCriteria(String screeningCriteria) {
        this.screeningCriteria = screeningCriteria;
    }
    
    public String getMatchReport() {
        return matchReport;
    }
    
    public void setMatchReport(String matchReport) {
        this.matchReport = matchReport;
    }
    
    public Integer getMatchScore() {
        return matchScore;
    }
    
    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }
    
    public String getScreeningResult() {
        return screeningResult;
    }
    
    public void setScreeningResult(String screeningResult) {
        this.screeningResult = screeningResult;
    }
    
    public String getHrComment() {
        return hrComment;
    }
    
    public void setHrComment(String hrComment) {
        this.hrComment = hrComment;
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
}
