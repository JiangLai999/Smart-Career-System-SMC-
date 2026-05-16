package com.example.smartcareer.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ApplicationRequest {
    
    @NotNull(message = "职位ID不能为空")
    private Long jobId;
    
    @Size(max = 500, message = "求职信最多500个字符")
    private String coverLetter;
    
    private String resumeData;
    
    public Long getJobId() {
        return jobId;
    }
    
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
    
    public String getCoverLetter() {
        return coverLetter;
    }
    
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
    
    public String getResumeData() {
        return resumeData;
    }
    
    public void setResumeData(String resumeData) {
        this.resumeData = resumeData;
    }
}
