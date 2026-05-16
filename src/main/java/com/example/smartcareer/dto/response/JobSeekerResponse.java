package com.example.smartcareer.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JobSeekerResponse {
    
    private Long id;
    
    private String username;
    
    private String phone;
    
    private String email;
    
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
    
    private String avatar;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
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
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
