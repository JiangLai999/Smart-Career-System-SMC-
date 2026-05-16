package com.example.smartcareer.dto.request;

import java.time.LocalDate;

public class JobSeekerUpdateRequest {
    
    private String realName;
    
    private String gender;
    
    private LocalDate birthday;
    
    private String education;
    
    private String school;
    
    private String major;
    
    private Integer workYears;
    
    private String resume;
    
    private String skills;
    
    private String expectedPosition;
    
    private String expectedLocation;
    
    private Integer expectedSalaryMin;
    
    private Integer expectedSalaryMax;
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public LocalDate getBirthday() {
        return birthday;
    }
    
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    public String getEducation() {
        return education;
    }
    
    public void setEducation(String education) {
        this.education = education;
    }
    
    public String getSchool() {
        return school;
    }
    
    public void setSchool(String school) {
        this.school = school;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public Integer getWorkYears() {
        return workYears;
    }
    
    public void setWorkYears(Integer workYears) {
        this.workYears = workYears;
    }
    
    public String getResume() {
        return resume;
    }
    
    public void setResume(String resume) {
        this.resume = resume;
    }
    
    public String getSkills() {
        return skills;
    }
    
    public void setSkills(String skills) {
        this.skills = skills;
    }
    
    public String getExpectedPosition() {
        return expectedPosition;
    }
    
    public void setExpectedPosition(String expectedPosition) {
        this.expectedPosition = expectedPosition;
    }
    
    public String getExpectedLocation() {
        return expectedLocation;
    }
    
    public void setExpectedLocation(String expectedLocation) {
        this.expectedLocation = expectedLocation;
    }
    
    public Integer getExpectedSalaryMin() {
        return expectedSalaryMin;
    }
    
    public void setExpectedSalaryMin(Integer expectedSalaryMin) {
        this.expectedSalaryMin = expectedSalaryMin;
    }
    
    public Integer getExpectedSalaryMax() {
        return expectedSalaryMax;
    }
    
    public void setExpectedSalaryMax(Integer expectedSalaryMax) {
        this.expectedSalaryMax = expectedSalaryMax;
    }
}
