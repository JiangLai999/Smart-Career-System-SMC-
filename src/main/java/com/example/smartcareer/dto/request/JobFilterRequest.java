package com.example.smartcareer.dto.request;

public class JobFilterRequest {
    
    private String keyword;
    
    private String category;
    
    private String location;
    
    private String education;
    
    private String experience;
    
    private String jobType;
    
    private Integer salaryMin;
    
    private Integer salaryMax;
    
    private String sortBy;
    
    private String sortOrder;
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
    
    public String getJobType() {
        return jobType;
    }
    
    public void setJobType(String jobType) {
        this.jobType = jobType;
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
    
    public String getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
    public String getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
