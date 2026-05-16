package com.example.smartcareer.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("JOB_SEEKER")
public class JobSeeker extends User {
    
    @Column(length = 50)
    private String realName;
    
    @Column(length = 10)
    private String gender;
    
    private LocalDate birthday;
    
    @Column(length = 100)
    private String education;
    
    @Column(length = 50)
    private String school;
    
    @Column(length = 50)
    private String major;
    
    private Integer workYears;
    
    @Column(columnDefinition = "TEXT")
    private String resume;
    
    @Column(columnDefinition = "TEXT")
    private String skills;
    
    @Column(length = 100)
    private String expectedPosition;
    
    @Column(length = 50)
    private String expectedLocation;
    
    private Integer expectedSalaryMin;
    
    private Integer expectedSalaryMax;
    
    @Column(length = 100)
    private String avatar;
    
    public JobSeeker() {
        this.setUserType("JOB_SEEKER");
    }
    
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
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
