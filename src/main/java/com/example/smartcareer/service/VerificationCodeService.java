package com.example.smartcareer.service;

public interface VerificationCodeService {
    
    void sendVerificationCode(String phone, String purpose);
    
    boolean verifyCode(String phone, String code, String purpose);
    
    void invalidateCodes(String phone, String purpose);
}
