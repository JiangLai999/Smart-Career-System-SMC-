package com.example.smartcareer.dto.request;

import javax.validation.constraints.NotBlank;

public class VerifyCodeRequest {
    
    @NotBlank(message = "手机号不能为空")
    private String phone;
    
    @NotBlank(message = "验证码不能为空")
    private String code;
    
    @NotBlank(message = "用途不能为空")
    private String purpose;
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getPurpose() {
        return purpose;
    }
    
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
