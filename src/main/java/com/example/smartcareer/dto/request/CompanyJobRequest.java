package com.example.smartcareer.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class CompanyJobRequest {
    
    @NotBlank(message = "职位名称不能为空")
    private String title;
    
    private String category;
    
    @Min(value = 1, message = "招聘人数至少为1")
    private Integer recruitCount = 1;
    
    @NotBlank(message = "工作地点不能为空")
    private String location;
    
    private String type = "FULL_TIME";
    
    private String salaryRange;
    
    private String salaryDesc;
    
    @NotBlank(message = "学历要求不能为空")
    private String education;
    
    @NotBlank(message = "工作经验不能为空")
    private String experience;
    
    @NotBlank(message = "职位描述不能为空")
    private String description;
    
    private String requirements;
    
    private List<String> skills;
    
    private List<String> benefits;
    
    private String status = "ACTIVE";
    
    private Integer validityDays = 30;

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

    public Integer getRecruitCount() {
        return recruitCount;
    }

    public void setRecruitCount(Integer recruitCount) {
        this.recruitCount = recruitCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getSalaryDesc() {
        return salaryDesc;
    }

    public void setSalaryDesc(String salaryDesc) {
        this.salaryDesc = salaryDesc;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getValidityDays() {
        return validityDays;
    }

    public void setValidityDays(Integer validityDays) {
        this.validityDays = validityDays;
    }
}
