package com.example.smartcareer.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class JobPublishRequest {
    
    @NotBlank(message = "职位名称不能为空")
    @Size(max = 100, message = "职位名称最多100个字符")
    private String title;
    
    @Size(max = 50, message = "职位类别最多50个字符")
    private String category;
    
    @NotBlank(message = "工作地点不能为空")
    @Size(max = 50, message = "工作地点最多50个字符")
    private String location;
    
    @Size(max = 50, message = "学历要求最多50个字符")
    private String education;
    
    @Size(max = 50, message = "经验要求最多50个字符")
    private String experience;
    
    private Integer salaryMin;
    
    private Integer salaryMax;
    
    @NotNull(message = "招聘人数不能为空")
    private Integer recruitNumber;
    
    private String description;
    
    private String requirement;
    
    private String responsibility;
    
    private String welfare;
    
    @Size(max = 50, message = "工作类型最多50个字符")
    private String workType;
    
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
}
