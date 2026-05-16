package com.example.smartcareer.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class InterviewRequest {
    
    @NotNull(message = "申请ID不能为空")
    private Long applicationId;
    
    @NotNull(message = "面试时间不能为空")
    private LocalDateTime interviewTime;
    
    private String location;
    
    @NotBlank(message = "面试类型不能为空")
    private String interviewType;
    
    private String interviewLink;
    
    private String interviewer;
    
    private String interviewerPosition;
    
    private String contactPhone;
    
    private String notes;
    
    public Long getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
    
    public LocalDateTime getInterviewTime() {
        return interviewTime;
    }
    
    public void setInterviewTime(LocalDateTime interviewTime) {
        this.interviewTime = interviewTime;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getInterviewType() {
        return interviewType;
    }
    
    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }
    
    public String getInterviewLink() {
        return interviewLink;
    }
    
    public void setInterviewLink(String interviewLink) {
        this.interviewLink = interviewLink;
    }
    
    public String getInterviewer() {
        return interviewer;
    }
    
    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }
    
    public String getInterviewerPosition() {
        return interviewerPosition;
    }
    
    public void setInterviewerPosition(String interviewerPosition) {
        this.interviewerPosition = interviewerPosition;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
