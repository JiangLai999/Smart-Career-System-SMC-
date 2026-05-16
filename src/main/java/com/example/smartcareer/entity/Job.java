package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job")
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_id")
    private EnterpriseHR hr;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Column(length = 50)
    private String category;
    
    @Column(length = 50)
    private String location;
    
    @Column(length = 50)
    private String education;
    
    @Column(length = 50)
    private String experience;
    
    private Integer salaryMin;
    
    private Integer salaryMax;
    
    @Column(nullable = false)
    private Integer recruitNumber = 1;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String requirement;
    
    @Column(columnDefinition = "TEXT")
    private String responsibility;
    
    @Column(columnDefinition = "TEXT")
    private String welfare;
    
    @Column(length = 50)
    private String workType;
    
    @Column(nullable = false, length = 20)
    private String status = "PUBLISHED";
    
    private LocalDateTime publishTime;
    
    private LocalDateTime expiryTime;
    
    @Column(nullable = false)
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer viewCount = 0;
    
    private Integer applyCount = 0;
    
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (publishTime == null) {
            publishTime = LocalDateTime.now();
        }
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getEducation() {
        return education;
    }
    
    public void setEducation(String education) {
        this.education = education;
    }
    
    public String getExperience() {
        return experience;
    }
    
    public void setExperience(String experience) {
        this.experience = experience;
    }
    
    public Integer getSalaryMin() {
        return salaryMin;
    }
    
    public void setSalaryMin(Integer salaryMin) {
        this.salaryMin = salaryMin;
    }
    
    public Integer getSalaryMax() {
        return salaryMax;
    }
    
    public void setSalaryMax(Integer salaryMax) {
        this.salaryMax = salaryMax;
    }
    
    public Integer getRecruitNumber() {
        return recruitNumber;
    }
    
    public void setRecruitNumber(Integer recruitNumber) {
        this.recruitNumber = recruitNumber;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getRequirement() {
        return requirement;
    }
    
    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }
    
    public String getResponsibility() {
        return responsibility;
    }
    
    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }
    
    public String getWelfare() {
        return welfare;
    }
    
    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }
    
    public String getWorkType() {
        return workType;
    }
    
    public void setWorkType(String workType) {
        this.workType = workType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getPublishTime() {
        return publishTime;
    }
    
    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }
    
    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }
    
    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
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
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public Integer getApplyCount() {
        return applyCount;
    }
    
    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }
}
