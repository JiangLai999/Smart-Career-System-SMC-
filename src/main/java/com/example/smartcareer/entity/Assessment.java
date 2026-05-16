package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment")
public class Assessment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private JobSeeker jobSeeker;
    
    @Column(length = 50)
    private String assessmentType;
    
    @Column(columnDefinition = "TEXT")
    private String questions;
    
    @Column(columnDefinition = "TEXT")
    private String answers;
    
    @Column(columnDefinition = "TEXT")
    private String result;
    
    @Column(columnDefinition = "TEXT")
    private String skillTags;
    
    @Column(columnDefinition = "TEXT")
    private String careerAdvice;
    
    private Integer score;
    
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
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
    
    public String getAssessmentType() {
        return assessmentType;
    }
    
    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }
    
    public String getQuestions() {
        return questions;
    }
    
    public void setQuestions(String questions) {
        this.questions = questions;
    }
    
    public String getAnswers() {
        return answers;
    }
    
    public void setAnswers(String answers) {
        this.answers = answers;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public String getSkillTags() {
        return skillTags;
    }
    
    public void setSkillTags(String skillTags) {
        this.skillTags = skillTags;
    }
    
    public String getCareerAdvice() {
        return careerAdvice;
    }
    
    public void setCareerAdvice(String careerAdvice) {
        this.careerAdvice = careerAdvice;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
