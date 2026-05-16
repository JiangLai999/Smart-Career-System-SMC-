package com.example.smartcareer.dto.response;

public class CompanyStatsResponse {
    
    private Integer activeJobs;
    
    private Integer totalApplications;
    
    private Integer interviewCount;
    
    private Integer pendingCount;
    
    private Integer hiredCount;
    
    private Integer rejectedCount;

    public Integer getActiveJobs() {
        return activeJobs;
    }

    public void setActiveJobs(Integer activeJobs) {
        this.activeJobs = activeJobs;
    }

    public Integer getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(Integer totalApplications) {
        this.totalApplications = totalApplications;
    }

    public Integer getInterviewCount() {
        return interviewCount;
    }

    public void setInterviewCount(Integer interviewCount) {
        this.interviewCount = interviewCount;
    }

    public Integer getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(Integer pendingCount) {
        this.pendingCount = pendingCount;
    }

    public Integer getHiredCount() {
        return hiredCount;
    }

    public void setHiredCount(Integer hiredCount) {
        this.hiredCount = hiredCount;
    }

    public Integer getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(Integer rejectedCount) {
        this.rejectedCount = rejectedCount;
    }
}
