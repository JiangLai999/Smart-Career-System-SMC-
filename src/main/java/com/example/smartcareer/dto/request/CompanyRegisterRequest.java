package com.example.smartcareer.dto.request;

import javax.validation.constraints.NotBlank;

public class CompanyRegisterRequest {
    
    @NotBlank(message = "企业名称不能为空")
    private String companyName;
    
    @NotBlank(message = "营业执照号码不能为空")
    private String businessLicense;
    
    @NotBlank(message = "行业类别不能为空")
    private String industry;
    
    @NotBlank(message = "企业规模不能为空")
    private String scale;
    
    private String description;
    
    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;
    
    private String contactPosition;
    
    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;
    
    private String contactEmail;
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPosition() {
        return contactPosition;
    }

    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
