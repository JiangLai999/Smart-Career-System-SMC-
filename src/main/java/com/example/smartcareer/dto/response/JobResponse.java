package com.example.smartcareer.dto.response;

import java.time.LocalDateTime;

public class JobResponse {
    
    private Long id;
    
    private String title;
    
    private String category;
    
    private String location;
    
    private String education;
    
    private String experience;
    
    private Integer salaryMin;
    
    private Integer salaryMax;
    
    private Integer recruitNumber;
    
    private String description;
    
    private String requirement;
    
    private String responsibility;
    
    private String welfare;
    
    private String workType;
    
    private String status;
    
    private LocalDateTime publishTime;
    
    private Integer viewCount;
    
    private Integer applyCount;
    
    private Long enterpriseId;
    
    private String enterpriseName;
    
    private String enterpriseLogo;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public Long getEnterpriseId() {
        return enterpriseId;
    }
    
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    
    public String getEnterpriseName() {
        return enterpriseName;
    }
    
    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
    
    public String getEnterpriseLogo() {
        return enterpriseLogo;
    }
    
    public void setEnterpriseLogo(String enterpriseLogo) {
        this.enterpriseLogo = enterpriseLogo;
    }
}
